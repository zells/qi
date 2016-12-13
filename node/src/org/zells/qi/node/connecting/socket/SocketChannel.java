package org.zells.qi.node.connecting.socket;

import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.Signal;
import org.zells.qi.node.connecting.signals.FailedSignal;
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

            out.println(printer.print(signal));
            Signal response = parser.parse(in.readLine());

            out.close();
            in.close();
            socket.close();

            return response;
        } catch (Exception e) {
            return new FailedSignal(e.getMessage());
        }
    }
}
