package org.zells.qi.node.connecting.socket;

import org.zells.qi.node.connecting.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class IncomingSocketChannel implements Channel {

    private final String host;
    private final int port;
    private boolean running;
    private final ServerSocket server;
    private SignalListener listener;

    public IncomingSocketChannel(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            server = new ServerSocket(port);
            running = true;

            (new Thread(() -> {
                while (running) {
                    try {
                        Socket socket = server.accept();

                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        String signal = in.readLine();

                        String response = listener.receives(signal);

                        out.println(response);

                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        if (running) {
                            e.printStackTrace();
                        }
                    }
                }
            })).start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to open port " + port, e);
        }
    }

    @Override
    public String send(String signal) {
        throw new RuntimeException("Cannot send on an incoming channel");
    }

    @Override
    public void listen(SignalListener listener) {
        this.listener = listener;
    }

    @Override
    public void close() {
        running = false;
        try {
            server.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public String getConnection() {
        return host + ":" + port;
    }
}
