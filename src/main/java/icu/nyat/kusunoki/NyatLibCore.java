package icu.nyat.kusunoki;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.injector.netty.WirePacket;
import icu.nyat.kusunoki.utils.NyatLibLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;



import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class NyatLibCore {
    private final List<String> brand;
    private final long period;
    private final Class<?> pdscl;
    private final ProtocolManager manager;
    private int index = 0;
    private ScheduledFuture<?> task;

    public NyatLibCore(List brand, long period, ProtocolManager manager) throws ClassNotFoundException {
        this.brand = brand;
        this.period = period;
        this.manager = manager;

        this.pdscl = Class.forName("net.minecraft.network.PacketDataSerializer");
    }

    public void start() {
        task = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new UpdateBrandTask(), period, period, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (task != null) task.cancel(true);
    }

    public int size() {
        return brand.size();
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public void send(Player player){
        /*String str = brand.get(index)
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName());*/

        ByteBuf pds = (ByteBuf) getPacketDataSerializer();
        if (pds == null) return;
        writeString(pds, NyatLib.BRAND);
        //writeString(pds, Chat.getTranslated(str + "&r"));

        byte[] data = new byte[pds.readableBytes()];
        for (int i = 0; i < data.length; i++) data[i] = pds.getByte(i);

        WirePacket customPacket = new WirePacket(PacketType.Play.Server.CUSTOM_PAYLOAD, data);

        try {
            manager.sendWirePacket(player, customPacket);
        } catch (Exception e) {

            //throw new InvocationTargetException(null);
            NyatLibLogger.logERROR(e.getMessage());
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

    private void writeString(Object buf, String data) {
        try {
            Method writeString = pdscl.getDeclaredMethod("a", String.class);
            writeString.invoke(buf, data);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
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
