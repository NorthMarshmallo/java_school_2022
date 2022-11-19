package ru.croc.task10.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AuctionEndedException extends Exception{
    private String message;
    public AuctionEndedException(LocalDateTime endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.message = "The auction has been already ended at " + endTime.format(formatter);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
