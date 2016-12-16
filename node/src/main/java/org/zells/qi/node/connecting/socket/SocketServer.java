package org.zells.qi.node.connecting.socket;

import org.zells.qi.node.connecting.Server;
import org.zells.qi.node.parsing.Input;
import org.zells.qi.node.singalling.Signal;
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
        } catch (IOException e) {
            throw new RuntimeException("Failed to open port " + port, e);
        }

        running = true;

        (new SocketListener(server, listener)).start();
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

    private class SocketListener extends Thread {

        private ServerSocket server;
        private SignalListener listener;

        SocketListener(ServerSocket server, SignalListener listener) {
            this.server = server;
            this.listener = listener;
        }

        @Override
        public void run() {

            (new Thread(() -> {
                while (running) {
                    try {
                        Socket socket = server.accept();
                        (new SignalWorker(socket, listener)).start();
                    } catch (IOException e) {
                        if (running) {
                            e.printStackTrace();
                        }
                    }
                }
            })).start();
        }
    }

    private class SignalWorker extends Thread {

        private final Socket socket;
        private final SignalListener listener;

        SignalWorker(Socket socket, SignalListener listener) {
            this.socket = socket;
            this.listener = listener;
        }

        @Override
        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Signal signal = parser.parse(new Input(in.readLine()));
                Signal response = listener.receives(signal);

                out.println(printer.print(response));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (in != null) in.close();
                if (out != null) out.close();
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
