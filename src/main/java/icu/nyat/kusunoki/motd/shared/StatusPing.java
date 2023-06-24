package icu.nyat.kusunoki.motd.shared;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

public interface StatusPing {


    String getVersionName() throws UnsupportedOperationException;

    void setVersionName(String name) throws UnsupportedOperationException;

    int getVersionProtocol() throws UnsupportedOperationException;

    void setVersionProtocol(int protocol) throws UnsupportedOperationException;

    int getClientProtocol() throws UnsupportedOperationException;

    Optional<InetSocketAddress> getClientVirtualHost() throws UnsupportedOperationException;
}
