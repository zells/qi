package org.zells.qi.deliver;

public abstract class GlobalUniqueIdentifierGenerator {

    private static GlobalUniqueIdentifierGenerator generator;

    public static void setGenerator(GlobalUniqueIdentifierGenerator generator) {
        GlobalUniqueIdentifierGenerator.generator = generator;
    }

    static String generate() {
        return generator.next();
    }

    protected abstract String next();
}
