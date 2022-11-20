package ru.croc.task9;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class SearchPassword implements Callable<String> {
    private final String firstLetter;
    private final String secondLetter;
    private final String thirdLetter;
    private final String passwordHash;
    private ExecutorService pool;
    public SearchPassword(String firstLetter, String secondLetter, String thirdLetter, String passwordHash, ExecutorService pool){
        this.firstLetter = firstLetter;
        this.passwordHash = passwordHash;
        this.secondLetter = secondLetter;
        this.thirdLetter = thirdLetter;
        this.pool = pool;
    }

    @Override
    public String call() throws Exception {
        String password, hash;
        //for (char thiLet = 'a'; thiLet <= 'z'; thiLet++) {
        for (char forLet = 'a'; forLet <= 'z'; forLet++) {
            for (char fifLet = 'a'; fifLet <= 'z'; fifLet++) {
                for (char sixLet = 'a'; sixLet <= 'z'; sixLet++) {

                    for (char sevLet = 'a'; sevLet <= 'z'; sevLet++) {
                       password = this.firstLetter + this.secondLetter + this.thirdLetter + forLet + fifLet + sixLet + sevLet;
                       hash = HashUtils.hashPassword(password);
                       //System.out.println(password);
                       if (hash.equals(this.passwordHash)){
                           System.out.println("Your found password: " + password);
                           this.pool.shutdownNow();
                           return password;
                       }
                    }

                }
            }
        }
        //System.out.println("done");
        return null;
    }
}
