package icu.nyat.kusunoki.motd;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import icu.nyat.kusunoki.NyatLib;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import icu.nyat.kusunoki.motd.shared.StatusPing;
import icu.nyat.kusunoki.motd.shared.StatusPingListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PingEventPaper implements Listener, StatusPingListener {
    private final NyatLib plugin;

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        handle(wrap(event));
    }

    private StatusPing wrap(PaperServerListPingEvent event) {
        return new StatusPing() {

            @Override
            public String getVersionName() {
                return event.getVersion();
            }

            @Override
            public void setVersionName(String name) {
                event.setVersion(name);
            }

            @Override
            public int getVersionProtocol() {
                return event.getProtocolVersion();
            }

            @Override
            public void setVersionProtocol(int protocol) {
                event.setProtocolVersion(protocol);
            }



            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                return event.getClient().getProtocolVersion();
            }

            @Override
            public Optional<InetSocketAddress> getClientVirtualHost() throws UnsupportedOperationException {
                return Optional.ofNullable(event.getClient().getVirtualHost());
            }
        };
    }
}
