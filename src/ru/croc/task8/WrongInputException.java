package ru.croc.task8;

public class WrongInputException extends Exception{

    private String message;
    public WrongInputException(String message){
        this.message = "Programm has got the following: \"" + message + "\". Please enter a double, using symbol "
        + "\",\" to highlight decimal part.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
