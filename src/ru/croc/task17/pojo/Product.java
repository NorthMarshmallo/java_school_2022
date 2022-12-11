package ru.croc.task17.pojo;

public class Product {

    private Integer id;
    private final String art;
    private String title;
    private Integer price;

    public Product(String art, String title, int price){

        this.art = art;
        this.title = title;
        this.price = price;

    }

    public String getArt() {
        return art;
    }

    public Integer getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "'" + art + "', '" + title + "', " + price;
    }

    public String databaseInfo() {
        if (id == null)
            return "В базу данных товар пока не занесен";
        else return "Товар находится по id: " + id +
                ". Подробная информация: { артикул: " + art + ", название: " + title + ", цена: " + price + " }";
    }
}
