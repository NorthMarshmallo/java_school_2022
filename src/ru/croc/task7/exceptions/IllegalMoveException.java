package ru.croc.task7.exceptions;

public class IllegalMoveException extends Exception{
    private String message;

    public IllegalMoveException(String pos1, String pos2){
        this.message = "Horse don't move like that: " + pos1 + " -> " + pos2;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
