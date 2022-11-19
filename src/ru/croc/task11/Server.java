package ru.croc.task11;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

public class Server implements Runnable{
    private static ServerSocketChannel server = null;

    public static void main(String[] args) throws Exception{
        String output;
        int messageLength;
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress (9000));
        while (true) {
            SocketChannel socket = server.accept();
            System.out.println ("Incoming connection from: "
                    + socket.socket().getRemoteSocketAddress( ));
            buf.clear();
            messageLength = socket.read(buf);
            buf.rewind();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < messageLength; i++) {
                sb.append((char) buf.get());
            }
            String[] message = sb.toString().split("\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            output = LocalDateTime.now().format(formatter) + " [" + message[0] + "]: " + message[1];
            System.out.println(output);
        }
    }

    @Override
    public void run() {
        
    }
}
