package org.zells.qi.node.connecting.socket;

import org.zells.qi.node.connecting.Server;
import org.zells.qi.node.connecting.Signal;
import org.zells.qi.node.parsing.SignalParser;
import org.zells.qi.node.parsing.SignalPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Server {

    private final String host;
    private final int port;
    private boolean running;
    private ServerSocket server;

    private final SignalParser parser = new SignalParser();
    private final SignalPrinter printer = new SignalPrinter();

    public SocketServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(SignalListener listener) {
        try {
            server = new ServerSocket(port);
            running = true;

            (new Thread(() -> {
                while (running) {
                    try {
                        Socket socket = server.accept();

                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        Signal signal = parser.parse(in.readLine());
                        System.err.println(port + " <<< " + signal);
                        Signal response = listener.receives(signal);
                        System.err.println(port + " >>> " + response);

                        out.println(printer.print(response));

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
    public void stop() {
        running = false;

        if (server == null) {
            return;
        }

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
