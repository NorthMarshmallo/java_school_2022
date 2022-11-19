package ru.croc.task10;
import ru.croc.task10.exceptions.*;

import java.time.LocalDateTime;


public class AuctionLot {
    volatile private String userName;
    volatile private double currentCost;
    private LocalDateTime auctionEnd;

    //создать с установкой начальной цены и времени окончания торгов
    public AuctionLot(double startCost, LocalDateTime auctionEnd){
        this.auctionEnd = auctionEnd;
        this.currentCost = startCost;
    }

    public synchronized void makeBid(String userName, double suggestedCost, LocalDateTime bidTime){
        if (bidTime.isBefore(this.auctionEnd)){
            if (suggestedCost > this.currentCost){
                this.userName = userName;
                this.currentCost = suggestedCost;
                System.out.println("Bid was accepted. User " + userName + " is now a leader with " + this.currentCost + " bid.");
            }
            else {
                //если нужно обрабатывать исключение вне метода, однако в этом случае придется ставить lock, чтобы и исключение выводило актуальную для User информацию
                //проще сразу обрабатывать, сделав метод в совокупности с выводом ошибки атомарной операцией
                //throw new LowSuggestException(suggestedCost, this.currentCost);
                System.out.println(new LowSuggestException(suggestedCost, this.currentCost).getMessage());
            }
        }
        else {
            //throw new AuctionEndedException(this.auctionEnd);
            System.out.println(new AuctionEndedException(this.auctionEnd).getMessage());
        }
    }

    public String getWinner(LocalDateTime requestTime) throws AuctionNotOverException{
        if (requestTime.isBefore(this.auctionEnd)){
            throw new AuctionNotOverException();
        }
        else {
            return this.userName;
        }
    }
    public LocalDateTime getAuctionEnd(){
        return this.auctionEnd;
    }

}
