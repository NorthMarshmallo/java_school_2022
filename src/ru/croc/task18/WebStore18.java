package ru.croc.task18;

import ru.croc.task17.DatabaseCreator;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WebStore18 {

    private static WebStoreDao wsDao;
    private static DatabaseCreator dbc;

    public static void main(String[] args){

        String url = "jdbc:h2:~/ru/croc/task17/Database/WebStoreDb";
        dbc = new DatabaseCreator();
        dbc.createDatabase("src\\ru\\croc\\task17\\Orders.csv",
                url);
        List<String> tables = Arrays.asList("CUSTOMER","PRODUCT","ORDERS","ORDERSPRODUCTS");
        dbc.printTables(tables, "src\\ru\\croc\\task18\\txtTables\\CreatedTables.txt");

        try(Connection connection = DriverManager.getConnection(url,"sa","")) {
            wsDao = new WebStoreDao(connection,dbc);
            System.out.println("Результат поиска продуктов:");
            checkFindProduct("T2");
            checkFindProduct("T8"); //нет артикула
            System.out.println("Результат создания записи о продуктах:");
            checkCreateProduct(new Product("T8","Наушники",100));
            checkCreateProduct(new Product("T5","Беспроводная гарнитура",200)); //уже существует с таким артикулом
            checkCreateProduct(new Product("T6","Беспроводная гарнитура",200));
            System.out.println("Результат обновления информации о продуктах:");
            checkUpdateProduct(new Product("T10","Наушники-вкладыши",90)); //нет артикула
            checkUpdateProduct(new Product("T5","Улучшенная видеокарта",16200));
            System.out.println("Результат удаления информации о продуктах:");
            checkDeleteProduct("T5");
            checkDeleteProduct("T4");
            checkDeleteProduct("T10"); //товара нет, так что просто ничего не удалит
            System.out.println("Результат создания заказов:");
            Map<String,Product> products = wsDao.getProducts();
            checkCreateOrder("vasya", Arrays.asList(products.get("T8"),products.get("T1"),
                    products.get("T1")));
            checkCreateOrder("vasya", Arrays.asList(products.get("T3"),products.get("T5"))); //нет товара
            checkCreateOrder("alena", Arrays.asList(products.get("T2"),products.get("T5"))); //нет покупателя
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
        System.out.print("Результат удаления находится в файле: ");
        wsDao.deleteProduct(art);
        List<String> tables = Arrays.asList("PRODUCT","ORDERSPRODUCTS","ORDERS");
        dbc.printTables(tables,"src\\ru\\croc\\task18\\txtTables\\PostDeletion" + art + "Tables.txt");
        System.out.println("txtTables\\PostDeletion" + art + "Tables.txt");
    }

    private static void checkCreateOrder(String userLogin, List<Product> products){
        try{
            Order order = wsDao.createOrder(userLogin, products);
            System.out.println("В базе данных был создан заказ - запись с номером " +
                    order.getStrInOrders() + " в таблице Заказы. Сопутствующие товары находятся " +
                    "в записях таблички соответствия: " + order.getStrsInOP());
        } catch (Exception e){
            System.out.println("Вы ввели некорректные данные для создания заказа. Возможно, пользователя" +
                    " или товара пока нет в базе данных.");
        }
    }

}
