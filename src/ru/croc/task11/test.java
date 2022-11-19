package ru.croc.task11;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        String message;
        System.out.println("Dear User, please enter your name: ");
        Scanner in = new Scanner(System.in);
        User user1 = new User(in.next());
        System.out.println("You entered the chat. Enter the message or blank if you want to disconnect from server.");
        while (in.hasNextLine()) {
            //while (c){
            String input = in.next();
            message = user1.getNickname() + " " + input;
            System.out.println(message);
        }
    }
}
