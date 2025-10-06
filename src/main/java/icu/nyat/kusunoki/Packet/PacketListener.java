package icu.nyat.kusunoki.Packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import icu.nyat.kusunoki.NyatLib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class PacketListener extends PacketAdapter {
    private String def = null;

    public PacketListener(NyatLib plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.CUSTOM_PAYLOAD);
    }

    // Don't remove it
    @Override
    public void onPacketReceiving(PacketEvent event) {

    }

    // Don't remove it
    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();

        try {
            MinecraftKey channel = packet.getMinecraftKeys().read(0);
            String allChannel = channel.getFullKey();
            if (allChannel.equals(NyatLib.BRAND)) {
                event.setCancelled(true);

                ByteBuf buf = ((ByteBuf) packet.getModifier().read(1));
                String name = buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8).toString();
                if (def == null) def = name;
                if (!def.equals(name)) return;

                if (NyatLib.brandUpdater != null) {
                    NyatLib.brandUpdater.send(event.getPlayer());
                }
            }
        } catch (Throwable ignored) {
        }
    }
}