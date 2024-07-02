package icu.nyat.kusunoki.motd;
import icu.nyat.kusunoki.NyatLib;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import icu.nyat.kusunoki.motd.shared.StatusPing;
import icu.nyat.kusunoki.motd.shared.StatusPingListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import java.net.InetSocketAddress;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class PingEventSpigot implements Listener, StatusPingListener {
    private final NyatLib plugin;

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        handle(wrap(event));
    }

    private StatusPing wrap(ServerListPingEvent event) {
        return new StatusPing() {

            @Override
            public String getVersionName() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public void setVersionName(String name) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public int getVersionProtocol() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public void setVersionProtocol(int protocol) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public Optional<InetSocketAddress> getClientVirtualHost() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }
        };
    }
}