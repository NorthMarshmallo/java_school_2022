package ru.croc.task2;

public class ArithmProg {

    public static void main(String[] args){
        int currentElem = Integer.parseInt(args[0]), difference = Integer.parseInt(args[1]),
                numOfElem = Integer.parseInt(args[2]);
        int sumAP = 0;
        for (int i = 0; i < numOfElem; i++){
            sumAP += currentElem;
            currentElem += difference;
        }
        System.out.println("Sum: " + sumAP);
    }
}
