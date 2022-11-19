package ru.croc.task10.exceptions;

public class LowSuggestException extends Exception{
    private String message;

    public LowSuggestException(double suggest, double current){
        this.message = "Your last bid " + suggest + " was rejected. Current minimum bid is "
                + (current + 0.1);
    }
    @Override
    public String getMessage() {
        return message;
    }
}
