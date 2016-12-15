# qi [![Build Status](https://travis-ci.org/zells/qi.svg?branch=master)](https://travis-ci.org/zells/qi)

This is a proof-of-concept implementation of the programming model described [here][blog]. The purpose of this project is to demonstrate how the model can be used for communication between objects.

[blog]: http://blog.rtens.org/a-unified-computing-model.html


## Installation

The project is built with [gradle].

    git clone https://github.com/zells/qi.git
    cd qi
    ./gradlew check

[gradle]: https://gradle.org/


## Usage

This project is a library for building distributed applications. The following command will pack all dependencies of the `Node` module into `build/qi-node.jar`.

    ./gradlew buildNode

The [`Chatter`][chatter] application demonstrate how to use this library.


## Concepts

The project is split into four modules.

### Model

Implements the model mentioned above. The concept behind most of the classes are described [here][blog]. One exception is `Courier` which defines the public interface of a `Cell`.

### Node

The `Node` connects a local model to other models, forming a single, distributed model. The `Cell` and the `Node` classes form the programming interface to client applications. Please refer to the [`Chatter`][chatter] classes for a usage example.

### CLI

The `CommandLineInterface` allows its user to send arbitrary messages to arbitrary cells and to display messages to the user.

### Apps

The `Chatter` application can *connect* with other nodes, create *users* for 1-to-1 communication and *topics* that users can subscribe to for n-to-n communication.

To use it, build the application, start a Chatter node on post 42421, and choose a name.

    ./gradlew buildApps
    java -jar build/chatter.jar 42421
    > iam Alice

The last command sends the message `Alice` to the cell `iam` which creates the cell `user.Alice` which displays all received messages to the user. To send a message to Alice, start a second Chatter, choose another name, connect to the first node and say "hello".

    java -jar build/chatter.jar 42422
    > iam Bob
    > connect localhost:42421
    > user.Alice HelloThere

Open a third Chatter, choose a name and connect to the second one. Then open a topic, subscribe Alice and Bob to it and publish a message on it. The message will be received by both users. Note that there is no direct connection between Charlie and Alice.

    java -jar build/chatter.jar 42423
    > iam Charlie
    > connect localhost:42422
    > open foo
    > topic.foo.subscribe Alice
    > topic.foo.subscribe Bob
    > topic.foo HelloAll

[chatter]: https://github.com/zells/qi/blob/master/apps/src/main/java/org/zells/qi/apps/Channel.java


## Documentation ##

This project is a work-in-progress and it's documentation consists currently of this document as well as the [article describing the model][blog]. If you have any question or comment, please don't hesitate to [contact me].

[contact me]: https://github.com/rtens


## Contribution ##

Any kind of contribution will be much appreciated. Not just code but also comments and general remarks. Just drop me a line or open a [new issue].

[new issue]: https://github.com/zells/qi/issues/new