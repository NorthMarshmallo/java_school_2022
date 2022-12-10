package ru.croc.task17;

public class WebStore {

    //"src\\ru\\croc\\task17\\Orders.csv"
    public static void main(String[] args){

        DatabaseCreator dbc = new DatabaseCreator();
        dbc.createDatabase(args[0],
                "jdbc:h2:~/ru/croc/task17/Database/WebStoreDb");
        dbc.printTables();
    }
}
