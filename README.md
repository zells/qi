# qi [![Build Status](https://travis-ci.org/zells/qi.svg?branch=master)](https://travis-ci.org/zells/qi)

This is a proof-of-concept implementation of the programming model described [here][blog]. The purpose of this project is to demonstrate how the model can be used for communication between objects.

[blog]: http://blog.rtens.org/a-unified-computing-model.html


## Download

The project is built with [gradle].

    git clone https://github.com/zells/qi.git
    cd qi
    ,/gradlew check

[gradle]: https://gradle.org/


## Usage

There are two applications to test the model: a *Client* for sending and receiving messages and a *Channel* for forwarding messages to several clients.

### Client

To build and start a client on port `42421` use the following commands

    gradle buildClient
    java -jar build/Client.jar 42421

You can now send messages using the syntax `receiver message`. Messages sent to `°.42421` are displayed.

    > 42421 Hello

Start a second client on a different port, connect to the first client and send a message to it

    java -jar build/Client.jar 42422
    > connect localhost:42421
    > 42421 HelloYou

## Channel

To build and start a channel on port `42420` use these commands

    gradle buildChannel
    java -jar build/Channel.jar 42420

Clients can now connect and subscribe to the channel

    > connect localhost:42420
    > 42420.subscribe 42421
    > 42420.subscribe 42422

Messages sent to the channel are forwarded to all subscribed clients.

## Library

To build your own application, bundle the *Node* and use it's API as done in the [Client] and [Channel].

    gradle buildNode

[Client]: https://github.com/zells/qi/blob/master/apps/src/main/java/org/zells/qi/apps/Client.java
[Channel]: https://github.com/zells/qi/blob/master/apps/src/main/java/org/zells/qi/apps/Channel.java