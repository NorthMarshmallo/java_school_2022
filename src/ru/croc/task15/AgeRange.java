package ru.croc.task15;

public class AgeRange {

    private int minAge;
    private int maxAge;

    public AgeRange(int minAge, int maxAge){
        this.maxAge = maxAge;
        this.minAge = minAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public String toString() {
        return this.minAge + "-" + this.maxAge;
    }
}
