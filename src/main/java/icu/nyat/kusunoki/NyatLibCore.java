package icu.nyat.kusunoki;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.netty.WirePacket;
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

public class NyatLibCore extends BukkitRunnable{
    private final NyatLib plugin;
    private final List<String> brand;
    private final long period;

    public final Class<?> class_PacketDataSerializer;
    public final Constructor<?> constructor_PacketPlayOutCustomPayload;
    public final Object instance_MinecraftKey_Brand;

    private final ProtocolManager manager;
    private int index = 0;
    private BukkitTask task;


    public NyatLibCore(NyatLib plugin, List<String> brand, long period, ProtocolManager manager) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        this.plugin = plugin;

        this.brand = brand;
        this.period = period;
        this.manager = manager;

        this.class_PacketDataSerializer = Class.forName("net.minecraft.network.PacketDataSerializer");
        Class<?> class_PacketPlayOutCustomPayload = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutCustomPayload");
        //Class<?> class_PacketPlayOutCustomPayload = Class.forName("com.comphenix.protocol.wrappers.CustomPacketPayloadWrapper");
        //Class<?> class_PacketPlayOutCustomPayload = com.comphenix.protocol.wrappers.CustomPacketPayloadWrapper.class;
        this.constructor_PacketPlayOutCustomPayload = class_PacketPlayOutCustomPayload.getConstructors()[0];
        this.instance_MinecraftKey_Brand = class_PacketPlayOutCustomPayload.getField("a").get(null);


    }

    @Override
    public void run() {
        this.broadcast();
        this.start();
    }

    public void start() {
        System.out.println(Bukkit.getServer().getName());
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
        }



    }

    public void stop() {
        if (task != null) task.cancel();
    }

    public void broadcast() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            setPlayerBrand(player, brand.get(index));
        }
    }

    public Object createPacketDataSerializer(String arg) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        byte[] bytes = arg.getBytes();
        ByteBuf byteBuf = Unpooled.buffer(bytes.length + 1);
        byteBuf.writeByte(bytes.length);
        byteBuf.writeBytes(bytes);
        return this.class_PacketDataSerializer.getConstructor(ByteBuf.class).newInstance(byteBuf);
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
