package ru.croc.project;

import ru.croc.project.threads.AchievementsSenderThread;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    //у сервера будет свой сокет
    private ServerSocket serverSocket = null;
    private String baseFile;
    //количество max threads для executor pool
    private final Integer capacity;
    public Server(Integer port, Integer capacity){

        this.capacity = capacity;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Failed to create server");
            closeServer();
            System.exit(0);
        }

    }

    public static void main(String[] args){


        //для указанного порта открываем сервер
        Server server = new Server(9000, 20);
        //создаем базу или задаем уже созданную
        server.getDatabase("jdbc:h2:./src/ru/croc/project/database/ServerDatabase");
        System.out.println("Сервер и база данных (по окончанию работы программы сохранится) подключены.\n" +
                "Как только появится первое подключение от клиента, сервер начнет принимать сообщения о" +
                " действиях и посылать достижения, если они есть.");
        server.run();

    }

    public void getDatabase(String baseFile){

        this.baseFile = baseFile;

        //база сохраняется после работы сервера, если ее нет по адресу, то создастся новая
        try (Connection connection = DriverManager.getConnection(baseFile, "sa","");
                Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS USER_METRIC " +
                    "(id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "days_active INTEGER NOT NULL, " +
                    "tests_passed INTEGER NOT NULL, " +
                    "words_learned INTEGER NOT NULL, " +
                    "films_watched INTEGER NOT NULL, " +
                    "texts_read INTEGER NOT NULL)");

            //связана по ключу с метриками, если пользователь удален, то запись здесь также будет удалена
            statement.execute("CREATE TABLE IF NOT EXISTS ACHIEVEMENT " +
                    "(id INTEGER PRIMARY KEY NOT NULL, " +
                    "newbie INTEGER NOT NULL, " +
                    "wanderer INTEGER NOT NULL, " +
                    "hardworking INTEGER NOT NULL, " +
                    "truth_seeker INTEGER NOT NULL, " +
                    "test_slayer INTEGER NOT NULL, " +
                    "bookworm INTEGER NOT NULL, " +
                    "cinemaddict INTEGER NOT NULL, " +
                    "FOREIGN KEY (id) REFERENCES USER_METRIC(id) " +
                    "ON DELETE CASCADE)");

            statement.execute("CREATE TABLE IF NOT EXISTS USER_POINTS " +
                    "(id INTEGER PRIMARY KEY NOT NULL, " +
                    "points INTEGER NOT NULL, " +
                    "FOREIGN KEY (id) REFERENCES USER_METRIC(id) " +
                    "ON DELETE CASCADE)");

        } catch (Exception e){
            System.out.println("Failed to reach tables.");
            e.printStackTrace();
            System.exit(0);
        }


    }


    public void run(){

        //для отсчета дня с начала работы сервера
        long timeStart = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(capacity);

        try {
            while (!serverSocket.isClosed()) {
                //thread прекратит работу по окончанию посыла действий от клиента
                Socket socket = serverSocket.accept();
                pool.submit(new AchievementsSenderThread(socket,baseFile,timeStart));
            }
        } catch (Exception e){
            System.out.println("Failed to accept socket");
            e.printStackTrace();
            closeServer();
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e){
            System.out.println("Failed to close server");
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
