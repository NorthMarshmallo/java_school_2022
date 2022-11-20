package ru.croc.task9;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args){
        int threadNum = Integer.valueOf(args[0]);
        //int threadNum = 30000;
        String passwordHash = args[1];
        //String passwordHash = "F30C88152120E44647FF5CFFA7762174";
        //String passwordHash = "40682260CC011947FC2D0B1A927138C5";
        //System.out.println(HashUtils.hashPassword("dogovor"));
        try {
            searchPasswordParallel(threadNum, passwordHash);
        } catch(Exception e){
            e.getStackTrace();
        }

    }

    public static String searchPasswordParallel(int threadNum, String passwordHash) throws Exception {

        Long output;
        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        for (char firLet = 'a'; firLet <= 'z'; firLet++) {
            for (char secLet = 'a'; secLet <= 'z'; secLet++) {
                for (char thiLet = 'a'; thiLet <= 'z'; thiLet++) {
                    //System.out.println(Character.toString(firLet) + secLet + Character.toString(thiLet));
                    Future<String> res = pool.submit(new SearchPassword(Character.toString(firLet), Character.toString(secLet),
                            Character.toString(thiLet), passwordHash, pool));
                    if (pool.isShutdown()){
                        break;
                    }
                    //System.out.println(output);
                }
                /*System.out.println(
                        "Pool size is now " +
                                ((ThreadPoolExecutor) pool).getActiveCount()
                );*/
                }
        }
        return null;
    }
}

