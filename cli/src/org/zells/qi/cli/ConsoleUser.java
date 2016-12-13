package org.zells.qi.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUser extends User implements Runnable {

    public ConsoleUser() {
        (new Thread(this)).start();
    }

    @Override
    public void tell(String output) {
        System.out.println(output);
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        try {
            System.out.print("> ");
            while ((input = reader.readLine()) != null) {
                hear(input);
                System.out.print("> ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
