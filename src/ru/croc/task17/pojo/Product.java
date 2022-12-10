package ru.croc.task17.pojo;

public class Product {

    private final String art;
    private String title;
    private int price;

    public Product(String art, String title, int price){

        this.art = art;
        this.title = title;
        this.price = price;

    }

    public String getArt() {
        return art;
    }

    @Override
    public String toString() {
        return "'" + title + "'," + price;
    }
}
