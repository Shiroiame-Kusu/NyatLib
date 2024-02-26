package icu.nyat.kusunoki.motd.shared;

import icu.nyat.kusunoki.NyatLib;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import static icu.nyat.kusunoki.utils.NyatLibYAMLPraser.getStringByInputStream;
import static org.bukkit.Bukkit.getServer;

import org.bukkit.plugin.Plugin;

public interface StatusPingListener {

    public static final Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    String afterIcon = "                                                                            ";

    default void handle(StatusPing ping) {
        Set<UUID> vanished = new HashSet<>();
        String SubMCVersion = NyatLib.BrandSubVersion;
        ping.setVersionName("Nyatwork " + SubMCVersion);
        ping.setVersionProtocol(-1);
        List<String> supportedProtocols = new ArrayList<>();

        try {
            supportedProtocols.add("-1");
        }catch (Exception ex){
            NyatLibLogger.logERROR(ex.toString());
        }

        if (!supportedProtocols.isEmpty()) {
            List<Integer> protocols = supportedProtocols.stream().map(Integer::parseInt).collect(Collectors.toList());

            if (protocols.contains(ping.getClientProtocol())) {
                ping.setVersionProtocol(ping.getClientProtocol());
            } else {
                ping.setVersionProtocol(-1);
            }
        }
    }
}
