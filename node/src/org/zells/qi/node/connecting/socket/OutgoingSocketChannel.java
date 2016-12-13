package org.zells.qi.node.connecting.socket;

import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.signals.FailedSignal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OutgoingSocketChannel implements Channel {

    private final String host;
    private final int port;

    public OutgoingSocketChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String send(String signal) {
        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(signal);
            String response = in.readLine();

            out.close();
            in.close();
            socket.close();

            return response;
        } catch (Exception e) {
            return (new FailedSignal(e.getMessage())).toString();
        }
    }

    @Override
    public void listen(SignalListener listener) {
        throw new RuntimeException("Cannot listen to an outgoing channel");
    }

    @Override
    public void close() {
    }

    @Override
    public String getConnection() {
        throw new RuntimeException("Cannot connect to an outgoing channel");
    }
}
