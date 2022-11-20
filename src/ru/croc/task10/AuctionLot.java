package ru.croc.task10;
import ru.croc.task10.exceptions.*;

import java.time.LocalDateTime;

import java.util.concurrent.locks.ReentrantLock;


public class AuctionLot {
    volatile private String userName;
    volatile private double currentCost;
    private LocalDateTime auctionEnd;
    private static final Object lock = new Object();

    private static final ReentrantLock locker = new ReentrantLock();

    //создать с установкой начальной цены и времени окончания торгов
    public AuctionLot(double startCost, LocalDateTime auctionEnd){
        this.auctionEnd = auctionEnd;
        this.currentCost = startCost;
    }

    public void makeBid(String userName, double suggestedCost, LocalDateTime bidTime){
        if (bidTime.isBefore(this.auctionEnd)){
            // потенциальная ставка
            if (suggestedCost > this.currentCost){
                locker.lock();
                //проверка не изменилось ли положение дел к моменту, когда запись позволена
                if (suggestedCost > this.currentCost) {
                    this.userName = userName;
                    this.currentCost = suggestedCost;
                    System.out.println("Bid was accepted. User " + userName + " is now a leader with " + this.currentCost + " bid.");
                }
                locker.unlock();
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
