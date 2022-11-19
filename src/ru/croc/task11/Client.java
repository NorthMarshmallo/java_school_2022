package ru.croc.task11;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        String message;
        System.out.println("Dear User, please enter your name: ");
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        User user1 = new User(in.next());
        System.out.println("You entered the chat. Enter the message or blank if you want to disconnect from server.");
        int port = 9000;
        try {
            InetSocketAddress adress = new InetSocketAddress("127.0.0.1", port);
            SocketChannel socket = SocketChannel.open(adress);
            //System.out.println(socket.isConnected());
            boolean c = true;
            while (in.hasNextLine()){
            //while (c){
                String input = in.next();
                message = user1.getNickname() + '\n' + input;
                //System.out.println(message);
                //message = "Hi, I'm here. Happy holidays!";
                ByteBuffer buf = ByteBuffer.allocateDirect(1024);
                buf.put(message.getBytes());
                buf.flip();
                socket.write(buf);
                buf.clear();
                //c = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
