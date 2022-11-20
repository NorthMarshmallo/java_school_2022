package ru.croc.task11;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EchoServerThread extends Thread{
    //для работы с клиентом серверу нужны его сокет, имя(получим первым при чтении буфера), буферы, которые будут с ним
    //работать + сервер передаст список клиентов, которым можно посылать сообщения
    protected ArrayList<Socket> clientList;
    volatile protected Socket socket;
    private String username;
    private BufferedReader r;
    private BufferedWriter w;

    public EchoServerThread(Socket socket, ArrayList<Socket> clientList){
        this.socket = socket;
        this.clientList = clientList;
    }
    private static final Object lock = new Object();

    @Override
    public void run() {

        String output;

        //начинаем работать с сокетом, сообщаем, что он подключен и откуда
        System.out.println ("Incoming connection from: "
                + socket.getRemoteSocketAddress( ));

        sendToSocket("You entered the chat. Enter the message or enter blank spot if you want to disconnect from server.");
        //для начала прочитаем имя, чтобы сообщить всем кто подключился
        try {

            r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = r.readLine();
            sendToAll("User " + this.username + " entered the chat.");

        } catch (Exception e) {
            //в случае неполадок будем закрывать соединение
            System.out.println("Server failed to receive username on connection " + socket.getRemoteSocketAddress());
            closeEverything(socket, r, w);
        }

        String message = null;

        try {
            //будем работать с клиентом пока соединение не закрыли
            while ((!socket.isClosed()) && (!(message = r.readLine()).equals(""))){
                //чтобы одновременно обрабатывалось одно сообщение и все они шли в нужном порядке
                synchronized (lock) {
                    //вывод будет состоять из времени/ника/сообщения, чтобы как сам клиент, так и сервер и остальные
                    //могли убедиться, что сообщение дошло и узнать, когда оно было обработано на сервере
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    output = LocalDateTime.now().format(formatter) + " [" + username + "]: " + message;
                    sendToAll(output);
                }
            }
            closeEverything(socket, r, w);

        } catch (Exception e){
            closeEverything(socket, r, w);
            //System.out.println("Server failed to receive message from " + this.username);
            }
        }


    public void removeClient(){
        clientList.remove(this.socket);
        sendToAll("User " + this.username + " left the chat.");
    }

    //метод вывода на экран и посыла сообщения всем клиентам, присутствующим в списке

    public void sendToSocket(String message) {
        try {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            w.write(message + '\n');
            w.flush();
        } catch (Exception e) {
            System.out.println("Server didn't send message to " + this.username);
            closeEverything(socket, r, w);
        }
    }

    public void sendToAll(String message){
        System.out.println(message);
        for (Socket sc : clientList) {
            if (sc != null) {
                try {
                    BufferedWriter w = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    w.write(message + '\n');
                    w.flush();
                } catch (Exception e) {
                    System.out.println("Server didn't send message of " + this.username + " to other clients");
                    closeEverything(socket, r, w);
                }
            } else {
                break;
            }
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClient();
        try{
            if (bufferedReader!=null){
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
            if (socket!=null){
                socket.close();
            }
        }
        catch(Exception e) {
            System.out.println("Server failed to safely close connection to the user " + this.username);
        }
    }
}
