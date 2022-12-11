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

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
