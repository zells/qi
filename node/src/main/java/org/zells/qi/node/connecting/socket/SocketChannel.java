package org.zells.qi.node.connecting.socket;

import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.parsing.Input;
import org.zells.qi.node.singalling.Signal;
import org.zells.qi.node.singalling.signals.FailedSignal;
import org.zells.qi.node.parsing.SignalParser;
import org.zells.qi.node.parsing.SignalPrinter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketChannel implements Channel {

    private final String host;
    private final int port;

    private final SignalParser parser = new SignalParser();
    private final SignalPrinter printer = new SignalPrinter();

    public SocketChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Signal send(Signal signal) {
        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String print = printer.print(signal);
            out.println(print);
            Signal response = parser.parse(new Input(in.readLine()));

            out.close();
            in.close();
            socket.close();

            return response;
        } catch (Exception e) {
            return new FailedSignal(e.getMessage());
        }
    }

    @Override
    public int hashCode() {
        return host.hashCode() + port;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SocketChannel
                && ((SocketChannel) obj).host.equals(host)
                && ((SocketChannel) obj).port == port;
    }
}
