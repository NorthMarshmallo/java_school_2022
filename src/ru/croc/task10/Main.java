package ru.croc.task10;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        LocalDateTime end = LocalDateTime.now().plusNanos(10000000*5);
        AuctionLot lot = new AuctionLot(0, end);
        int c = 0;
        Random coeffs = new Random();
        CyclicBarrier barrier = new CyclicBarrier(51);
        Thread[] t = new Thread[50];
        while (c<50){
            t[c] = new Thread(new User(Integer.toString(c), lot, coeffs.nextInt(100)+1, barrier));
            t[c].start();
            c += 1;
        }
        //Вариант без барьеров
       // for (int i = 0; i < 50; i++){
         //   t[i].join();
        //}
        try{
            barrier.await(50, TimeUnit.SECONDS);
        } catch (Exception e){
            System.out.println("Barrier waited too long");
        }
        try {
            System.out.println("The winner of the auction is User " + lot.getWinner(end));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
