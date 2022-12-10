package ru.croc.task17.pojo;

public class Customer {
    private final String name;
    private int spent;

    public Customer(String name, int spent){

        this.name = name;
        this.spent = spent;

    }

    public void updateSpent(int productPrice){

        this.spent += productPrice;

    }

    @Override
    public String toString() {
        return "'" + name + "'," + spent;
    }

}
