package ru.croc.task18;

import ru.croc.task17.pojo.*;
import ru.croc.task17.DatabaseCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

public class WebStore18 {

    private static WebStoreDao wsDao;
    private static DatabaseCreator dbc;

    public static void main(String[] args){

        String url = "jdbc:h2:~/ru/croc/task17/Database/WebStoreDb";
        dbc = new DatabaseCreator();
        dbc.createDatabase("src\\ru\\croc\\task17\\Orders.csv",
                url);
        List<String> tables = Arrays.asList("CUSTOMER","PRODUCT","ORDERS","ORDERSPRODUCTS");
        dbc.printTables(tables);

        try(Connection connection = DriverManager.getConnection(url,"sa","")) {
            wsDao = new WebStoreDao(connection,dbc);
            System.out.println("Результат поиска продуктов:");
            checkFindProduct("T2");
            checkFindProduct("T8");
            System.out.println("Результат создания записи о продуктах:");
            checkCreateProduct(new Product("T8","Наушники",100));
            checkCreateProduct(new Product("T5","Беспроводная гарнитура",200));
            System.out.println("Результат обновления информации о продуктах:");
            checkUpdateProduct(new Product("T8","Наушники-вкладыши",90));
            checkUpdateProduct(new Product("T6","Пылесос",300));
            checkDeleteProduct("T5");
            checkDeleteProduct("T4");
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void checkFindProduct(String art){
        Product product = wsDao.findProduct(art);
        if (product != null)
            System.out.println(product.databaseInfo());
        else
            System.out.println("Продукта с артикулом " + art + " на данный момент в таблице нет.");
    }

    private static void checkCreateProduct(Product product){
        try {
            System.out.println(wsDao.createProduct(product).databaseInfo());
        } catch (Exception e){
            System.out.println("Товар с артикулом " + product.getArt() + " уже существует.");
        }
    }

    private static void checkUpdateProduct(Product product){
            Product updatedProduct = wsDao.updateProduct(product);
            if (updatedProduct != null)
                System.out.println(wsDao.updateProduct(product).databaseInfo());
            else System.out.println("Попытка обновить товар с незарегистрированным артикулом " + product.getArt() +
                    ". Попробуйте сначала создать его.");

    }

    private static void checkDeleteProduct(String art){
        wsDao.deleteProduct(art);
        List<String> tables = Arrays.asList("PRODUCT","ORDERSPRODUCTS","ORDERS");
        dbc.printTables(tables);
    }

}
