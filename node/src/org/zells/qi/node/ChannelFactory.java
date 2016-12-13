package org.zells.qi.node;

public interface ChannelFactory {

    Channel forConnection(String connection);
}
