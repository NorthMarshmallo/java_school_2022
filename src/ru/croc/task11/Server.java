package ru.croc.task11;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
public class Server{
    //у сервера будет свой сокет и лист клиентов
    private static ServerSocket server = null;
    private static Socket[] clientList;


    public static void main(String[] args){
        //для указанного порта открываем сервер
        try {
            server = new ServerSocket(9000);
        } catch (Exception e){
            System.out.println("Failed to create server");
            closeServer();
            return;
        }
        ArrayList<Socket> clientList = new ArrayList<>();
        try {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                clientList.add(socket);
                new EchoServerThread(socket, clientList).start();
            }
        } catch (Exception e){
            System.out.println("Failed to accept socket");
            closeServer();
        }
    }

    public static void closeServer() {
        try {
            if (server != null) {
                server.close();
            }
        } catch (Exception e){
            System.out.println("Failed to close server");
        }
    }
}
