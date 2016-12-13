package org.zells.qi.node.connecting;

import org.zells.qi.node.connecting.socket.SocketChannel;

public class DefaultChannelFactory implements ChannelFactory {

    @Override
    public Channel forConnection(String connection) {
        String[] hostPort = connection.split(":");
        return new SocketChannel(hostPort[0], Integer.parseInt(hostPort[1]));
    }
}
