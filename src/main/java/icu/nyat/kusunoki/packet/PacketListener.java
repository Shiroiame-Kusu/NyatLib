package icu.nyat.kusunoki.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.MinecraftKey;
import icu.nyat.kusunoki.NyatLib;
import icu.nyat.kusunoki.NyatLibCore;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class PacketListener extends PacketAdapter {
    private final NyatLibCore updater;
    private String def = null;

    public PacketListener(NyatLib plugin, NyatLibCore updater) {
        super(plugin, ListenerPriority.LOW, PacketType.Play.Server.CUSTOM_PAYLOAD);
        this.updater = updater;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();

        MinecraftKey channel = packet.getMinecraftKeys().read(0);
        if (channel.getFullKey().equals(NyatLib.BRAND)) {
            event.setCancelled(true);

            ByteBuf buf = ((ByteBuf) packet.getModifier().read(1));
            String name = buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8).toString();
            if (def == null) def = name;
            if (!def.equals(name)) return;

            updater.send(event.getPlayer());
        }
    }
}