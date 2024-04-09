package icu.nyat.kusunoki;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.netty.WirePacket;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.MinecraftKey;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import io.papermc.lib.PaperLib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class NyatLibCore extends BukkitRunnable{
    //private final NyatLib plugin;
    private final List<String> brand;
    private final long period;
    public int size() {
        return brand.size();
    }
    public Class<?> pdscl;
    public Constructor<?> constructor_PacketPlayOutCustomPayload;
    public Object instance_MinecraftKey_Brand;

    private final ProtocolManager manager;
    private int index = 0;
    private ScheduledFuture<?> task;
    private static boolean CompatibleMode =true;


    public NyatLibCore(List<String> brand, long period, ProtocolManager manager) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //this.plugin = plugin;

        this.brand = brand;
        this.period = period;
        this.manager = manager;
        /*String serverVersion = String.valueOf(MinecraftVersion.getCurrentVersion());

        NyatLibLogger.logINFO(serverVersion);
        String[] versionParts = serverVersion.split(" ")[2].split("\\.");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);
        int revision = Integer.parseInt(versionParts[2]);*/
        this.pdscl = Class.forName("net.minecraft.network.PacketDataSerializer");
        try{
            Class<?> class_PacketPlayOutCustomPayload = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutCustomPayload");
            this.constructor_PacketPlayOutCustomPayload = class_PacketPlayOutCustomPayload.getConstructors()[0];
            this.instance_MinecraftKey_Brand = class_PacketPlayOutCustomPayload.getField("a").get(null);
        }catch (Exception ex){
            ex.printStackTrace();
            NyatLibLogger.logERROR("Looks like you're using Nyatlib >= 1.20.2");
            CompatibleMode = false;
        }





    }

    @Override
    public void run() {
        this.broadcast();
        this.start();
    }

    public void start() {
        /*System.out.println(Bukkit.getServer().getName());
        if(Bukkit.getServer().getName().equals("Folia")){
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    new UpdateBrandTask();
                }
            };
            runnable.runTaskTimer(this.plugin,period,period);

        }else{
            task = Bukkit.getServer().getScheduler().runTaskTimer(this.plugin, new UpdateBrandTask(), period, period);
        }*/
        task = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new UpdateBrandTask(), period, period, TimeUnit.MILLISECONDS);



    }

    public void stop() {
        if (task != null) task.cancel(true);
    }

    public void broadcast() {
        if(CompatibleMode){
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                setPlayerBrand(player, brand.get(index));
            }
        }else{
            Bukkit.getOnlinePlayers().forEach(this::send);
        }

    }
    public void send(Player player) {


        ByteBuf pds = (ByteBuf) getPacketDataSerializer();
        if (pds == null) return;
        writeString(pds, NyatLib.BRAND);
        writeString(pds, brand.get(index));

        byte[] data = new byte[pds.readableBytes()];
        for (int i = 0; i < data.length; i++) data[i] = pds.getByte(i);

        try {
            WirePacket customPacket = new WirePacket(PacketType.Play.Server.CUSTOM_PAYLOAD, data);
            manager.sendWirePacket(player, customPacket);
        } catch (Throwable ignored) {
        }
    }
    private void writeString(Object buf, String data) {
        try {
            Method writeString = pdscl.getDeclaredMethod("a", String.class);
            writeString.invoke(buf, data);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }
    }
    private Object getPacketDataSerializer() {
        try {
            Constructor<?> pdsco = pdscl.getConstructor(ByteBuf.class);
            return pdsco.newInstance(Unpooled.buffer());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    public Object createPacketDataSerializer(String arg) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        byte[] bytes = arg.getBytes();
        ByteBuf byteBuf = Unpooled.buffer(bytes.length + 1);
        byteBuf.writeByte(bytes.length);
        byteBuf.writeBytes(bytes);
        return this.pdscl.getConstructor(ByteBuf.class).newInstance(byteBuf);
    }

    public Object createPacketPlayOutCustomPayload(String arg) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.constructor_PacketPlayOutCustomPayload.newInstance(this.instance_MinecraftKey_Brand, this.createPacketDataSerializer(arg));
    }

    public void setPlayerBrand(Player player, String brand) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD, createPacketPlayOutCustomPayload(brand));
            manager.sendServerPacket(player, packet);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            NyatLibLogger.logERROR(e.getMessage());
            NyatLibLogger.logINFO("flag");
        }
    }

    private class UpdateBrandTask implements Runnable {

        @Override
        public void run() {
            broadcast();
            ++index;
            if (index >= brand.size()) index = 0;
        }
    }
}
