package ru.croc.task16.exceptions;

public class NotInBaseException extends Exception{

    @Override
    public String getMessage() {
        return "База не смогла предоставить вам подходящего водителя. Попробуйте ввести корректную информацию, " +
                "указанную в списках классов комфорта и удобств.";
    }
}
