package ru.croc.task7;

import ru.croc.task7.exceptions.*;

import java.nio.channels.IllegalChannelGroupException;

public class ChessPosition {
    private int x, y; //оставим не final, обозначает позицию какой-то фигуры, которая может двигаться, а не неподвижную
    // клетку на шахматной доске
    public ChessPosition(int x, int y) throws IllegalPositionException {
        if ((x < 0) || (x >= 8)|| (y < 0)|| (y >= 8)){
            throw new IllegalPositionException(x, y);
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        //<номер колонки в виде буквы от 'a' до 'h'><номер строки, начиная с 1>
        return letters[x] + Integer.toString(y + 1);
    }

    static ChessPosition parse(String position) throws IllegalPositionException{
        int x = 0;
        if (position.length() != 2){
            throw new IllegalPositionException(position);
        }
        char letter = position.charAt(0);
        if (letter == 'a'){ x = 0;}
        else if (letter == 'b'){ x = 1;}
        else if (letter == 'c'){ x = 2;}
        else if (letter == 'd'){ x = 3;}
        else if (letter == 'e'){ x = 4;}
        else if (letter == 'f'){ x = 5;}
        else if (letter == 'g'){ x = 6;}
        else if (letter == 'h'){ x = 7;}
        else { throw new IllegalPositionException(position);}
        int y = Character.getNumericValue(position.charAt(1));
        if ((y < 1) || (y >= 9)){
            throw new IllegalPositionException(position);
        } else{
            return new ChessPosition(x, y - 1);
        }
        // y получится 1-8 только для цифр 1-8, от других символов NumericValue будет вне
        // требуемого предела, поэтому сразу проверим на принадлежность отрезку, чтобы вызвать более подходящее
        // исключение и не создавать лишних исключений
    }

    static String isaHorseMovement(String[] positionsStrings) throws IllegalMoveException, IllegalPositionException{
        int n = positionsStrings.length;
        ChessPosition pos1, pos2;
        if (n>0){
            pos1 = ChessPosition.parse(positionsStrings[0]);
            for (int i = 1; i < n; i++){
                pos2 = ChessPosition.parse(positionsStrings[i]);
                if (Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2) != 5) {
                    // проверка с помощью теоремы Пифагора
                    throw new IllegalMoveException(positionsStrings[i-1], positionsStrings[i]);
                }
                pos1 = pos2;
            }
        }
        return "ok";
    }
}

