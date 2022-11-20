package ru.croc.task11;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){

        System.out.println("Dear User, please enter your name: ");
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        do{
        String name =  in.next();
        if (name.equals("")){
            System.out.println("Invalid name, please enter another: ");
            continue;
        }
        User user1 = new User(name);
        int port = 9000;
        try (Socket socket = new Socket("127.0.0.1", port)) {
            //поток для обработки поступающих с сервера сообщений
            Thread messageHandling = new Thread(new EchoClientThread(socket));
            messageHandling.start();

            String message;
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //читаем и передаем ник пользователя
            try {
                w.write(user1.getNickname() + '\n');
                w.flush();
            } catch (Exception e) {
                System.out.println("Server doesn't exist anymore. Username wasn't written");
                System.exit(0);
            }
            //цикл для обработки поступающих сообщений, закончится при вводе пустого поля
            while (in.hasNextLine()) {
                String input = in.next();
                if (input.equals("")) {
                    break;
                }
                message = input + '\n';
                try {
                    w.write(message);
                    w.flush();
                } catch (IOException e) {
                    System.out.println("Server doesn't exist anymore. Message wasn't written to server");
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            System.out.println("Server doesn't exist. Creation of sockets failed.");
            System.exit(0);
        }
        } while (true);
    }
}
