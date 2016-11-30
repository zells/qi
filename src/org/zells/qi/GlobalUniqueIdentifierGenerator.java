package org.zells.qi;

abstract class GlobalUniqueIdentifierGenerator {

    private static GlobalUniqueIdentifierGenerator generator;

    static void setGenerator(GlobalUniqueIdentifierGenerator generator) {
        GlobalUniqueIdentifierGenerator.generator = generator;
    }

    static String generate() {
        return generator.next();
    }

    abstract String next();
}
