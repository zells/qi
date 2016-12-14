package org.zells.qi.node.connecting;

public interface ChannelFactory {

    Channel forConnection(String connection);
}
