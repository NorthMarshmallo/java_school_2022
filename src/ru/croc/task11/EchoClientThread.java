package ru.croc.task11;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class EchoClientThread implements Runnable{
    private Socket socket;
    private BufferedReader input;

    public EchoClientThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            String check = "";
            while((!socket.isClosed()) && (check!=null)) {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                check = input.readLine();
                if (check!=null) {
                    System.out.println(check);
                }
            }
            } catch (Exception e){
                System.out.println("You were disconnected from the server. To connect again enter your username: ");
            }
    }
}