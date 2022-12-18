package ru.croc.project.threads;

import ru.croc.project.Achievement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class AchievementsGetterThread implements Runnable{

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private List<Achievement> listOfAchievements;

    public AchievementsGetterThread(Socket socket, ObjectInputStream objectInputStream){

        this.socket = socket;
        this.objectInputStream = objectInputStream;

    }
    @Override
    public void run() {

        while((!socket.isClosed()) && (socket!=null)) {

            try {

                listOfAchievements = (List<Achievement>) objectInputStream.readObject();
                listOfAchievements.forEach(achievement -> System.out.println(achievement.getMessage()));

            } catch (SocketException se){
                System.out.println("Connection to the server ended.");
                closeEverything();

            } catch (Exception e) {
                System.out.println("Can't correctly receive messages from server. Try again later.");
                e.printStackTrace();
                closeEverything();
                break;
            }

        }
    }

    public void closeEverything(){

        try {

            if (objectInputStream != null)
                objectInputStream.close();
            if (socket != null)
                socket.close();
        } catch (IOException ie){
            ie.printStackTrace();
        }

    }

}
