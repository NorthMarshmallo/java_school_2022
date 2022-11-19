package ru.croc.task10;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;


public class User implements Runnable{

    private final String name;
    private AuctionLot lot;
    private int coeff;
    private static final Object lock = new Object();
    private CyclicBarrier barrier;

    public User(String userName, AuctionLot lot, int coeff, CyclicBarrier barrier){
        this.name = userName;
        this.lot = lot;
        this.coeff = coeff;
        this.barrier = barrier;
    }
    @Override
    public void run() {
        while (LocalDateTime.now().isBefore(this.lot.getAuctionEnd().plusNanos(10000000))){
            //случайная ставка
            Random r = new Random();
            double bid = (10 + 990 * r.nextDouble()) * this.coeff;
            lot.makeBid(this.name, bid, LocalDateTime.now());
        }
        try{
            barrier.await(20, TimeUnit.SECONDS);
        } catch (Exception e){
            System.out.println("Barrier waited too long");
        }
    }
}
