package ru.croc.task7;
import ru.croc.task4.Point;

public class Main {
    public static void main(String[] args){
        try {
            //System.out.println(ChessPosition.parse("a8").toString());
            //ChessPosition checkExeption = new ChessPosition(1,7);
            //System.out.println(checkExeption.toString());
            //для работы не из командной строки
            //String[] movement = new String[] {"a8", "c7", "a6"};
            String[] movement = args;
            System.out.println(ChessPosition.isaHorseMovement(movement));
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
