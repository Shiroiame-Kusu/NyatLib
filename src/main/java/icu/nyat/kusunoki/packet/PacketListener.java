package icu.nyat.kusunoki.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import icu.nyat.kusunoki.NyatLib;
import icu.nyat.kusunoki.NyatLibCore;

public class PacketListener extends PacketAdapter {
    private final NyatLibCore updater;
    private String def = null;

    public PacketListener(NyatLib plugin, NyatLibCore updater) {
        super(plugin, ListenerPriority.LOW, PacketType.Play.Server.CUSTOM_PAYLOAD);
        this.updater = updater;
    }

    // Don't remove it
    @Override
    public void onPacketReceiving(PacketEvent event) {

    }

    // Don't remove it
    @Override
    public void onPacketSending(PacketEvent event) {

    }
}