package icu.nyat.kusunoki.motd.shared;

import icu.nyat.kusunoki.NyatLib;
import java.util.*;
import icu.nyat.kusunoki.Utils.NyatLibLogger;
import org.bukkit.plugin.Plugin;

public interface StatusPingListener {

    Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    default void handle(StatusPing ping) {
        ping.setVersionName("Nyatwork " + NyatLib.BrandVersion);
        ping.setVersionProtocol(NyatLib.BrandProtocolVersion);

        try {
            NyatLib.ServerSupportedProtocolVersion.add(NyatLib.BrandProtocolVersion);
        }catch (Exception ex){
            NyatLibLogger.logERROR(ex.toString());
        }

        if (!NyatLib.ServerSupportedProtocolVersion.isEmpty()) {
            List<Integer> protocols = NyatLib.ServerSupportedProtocolVersion.stream().toList();

            if (protocols.contains(ping.getClientProtocol())) {
                ping.setVersionProtocol(ping.getClientProtocol());
            } else {
                ping.setVersionProtocol(NyatLib.BrandProtocolVersion);
            }
        }
    }
}
