package org.zells.qi.node.parsing;

public class Input {

    private char[] input;
    private int position = 0;

    public Input(String input) {
        this.input = input.toCharArray();
    }

    public char current() {
        return input[position];
    }

    public boolean hasNext() {
        return position < input.length;
    }

    public void skip() {
        position++;
    }
}
