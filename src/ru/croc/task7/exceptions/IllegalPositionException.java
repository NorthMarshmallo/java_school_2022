package ru.croc.task7.exceptions;

public class IllegalPositionException extends Exception{

    private String message;

    public IllegalPositionException(int x, int y){
        this.message = "Got your position as: (" + x + ", " + y +
                "). Both of coordinates should be integer numbers in range 0-7." ;
    }
    public IllegalPositionException(String wrongInput){
        this.message = "Unexpected input: \"" + wrongInput +"\". Change to the following format: " +
        "<'a' to 'h'><1 to 8>";
    }
    @Override
    public String getMessage() {
            return this.message;
    }

}
