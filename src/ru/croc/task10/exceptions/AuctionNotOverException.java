package ru.croc.task10.exceptions;

public class AuctionNotOverException extends Exception{
    private String message;
    public AuctionNotOverException(){
        this.message = "Auction is still on. There is no winner yet, you can be one.";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
