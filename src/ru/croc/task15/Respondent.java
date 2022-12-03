package ru.croc.task15;

public class Respondent implements Comparable<Respondent>{

    private final String name;
    private final int age;

    public Respondent(String name, int age){
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Respondent o) {
        int firstCheck = Integer.compare(o.getAge(),this.age);
        if (firstCheck != 0) {
            return firstCheck;
        } else {
            return this.name.compareTo(o.getName());
        }
    }

    @Override
    public String toString() {
        return this.name + " (" + String.valueOf(this.age) + ")";
    }
}
