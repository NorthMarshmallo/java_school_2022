package ru.croc.task18;

import ru.croc.task17.DatabaseCreator;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;
import ru.croc.task18.dao.OrderDaoImpl;
import ru.croc.task18.dao.ProductDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

public class WebStore18 {

    private static ProductDaoImpl productDao;
    private static OrderDaoImpl orderDao;
    private static DatabaseCreator dbc;

    public static void main(String[] args){

        String url = "jdbc:h2:./src/ru/croc/task17/Database/WebStoreDb";
        dbc = new DatabaseCreator();
        dbc.createDatabase("src\\ru\\croc\\task17\\Orders.csv",
                url);
        List<String> tables = Arrays.asList("CUSTOMER","PRODUCT","CUSTOMER_ORDER","ORDER_PRODUCT");
        dbc.printTables(tables, "src\\ru\\croc\\task18\\txtTables\\CreatedTables.txt");

        try(Connection connection = DriverManager.getConnection(url,"sa","")) {

            productDao = new ProductDaoImpl(connection);
            orderDao = new OrderDaoImpl(connection);
            System.out.println("Результат поиска продуктов:");
            checkFindProduct("T2");
            checkFindProduct("T8"); //нет артикула
            System.out.println("Результат создания записи о продуктах:");
            Product T8 = checkCreateProduct(new Product("T8","Наушники",100));
            checkCreateProduct(new Product("T5","Беспроводная гарнитура",200)); //уже существует с таким артикулом
            Product T6 = checkCreateProduct(new Product("T6","Беспроводная гарнитура",200));
            System.out.println("Результат обновления информации о продуктах:");
            checkUpdateProduct(new Product("T10","Наушники-вкладыши",90)); //нет артикула
            checkUpdateProduct(new Product("T5", "Улучшенная видеокарта", 16200));
            System.out.println("Результат удаления информации о продуктах:");
            checkDeleteProduct("T5");
            checkDeleteProduct("T4");
            checkDeleteProduct("T10"); //товара нет, так что просто ничего не удалит
            System.out.println("Результат создания заказов:");
            checkCreateOrder("vasya", Arrays.asList(T8,T6,T6));
            checkCreateOrder("vasya", Arrays.asList(new Product("T80","Nokia",200),T6)); //нет товара
            checkCreateOrder("alena", Arrays.asList(T6,T8)); //нет покупателя
            dbc.printTables(tables,"src\\ru\\croc\\task18\\txtTables\\FinalResult");


        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void checkFindProduct(String article){
        Product product = productDao.findProduct(article);
        if (product != null)
            System.out.println(product.databaseInfo());
        else
            System.out.println("Продукта с артикулом " + article + " на данный момент в таблице нет.");
    }

    private static Product checkCreateProduct(Product product){
        try {
            Product createdProduct = productDao.createProduct(product);
            System.out.println(createdProduct.databaseInfo());
            return createdProduct;
        } catch (Exception e){
            System.out.println("Товар с артикулом " + product.getArticle() + " уже существует.");
            return null;
        }
    }

    private static Product checkUpdateProduct(Product product){
            Product updatedProduct = productDao.updateProduct(product);
            if (updatedProduct != null)
                System.out.println(productDao.updateProduct(product).databaseInfo());
            else System.out.println("Попытка обновить товар с незарегистрированным артикулом " + product.getArticle() +
                    ". Попробуйте сначала создать его.");
            return updatedProduct;

    }

    private static void checkDeleteProduct(String article){
        System.out.print("Результат удаления находится в файле: ");
        productDao.deleteProduct(article);
        List<String> tables = Arrays.asList("PRODUCT","ORDER_PRODUCT","CUSTOMER_ORDER");
        dbc.printTables(tables,"src\\ru\\croc\\task18\\txtTables\\PostDeletion" + article + "Tables.txt");
        System.out.println("txtTables\\PostDeletion" + article + "Tables.txt");
    }

    private static void checkCreateOrder(String userLogin, List<Product> products){
        try{
            Order order = orderDao.createOrder(userLogin, products);
            System.out.println("В базе данных был создан заказ - запись с номером " +
                    order.getOrderTableId() + " в таблице Заказы. Сопутствующие товары находятся " +
                    "в записях таблички соответствия: " + order.getOrderProductTableIds());
        } catch (Exception e){
            System.out.println("Вы ввели некорректные данные для создания заказа. Возможно, пользователя" +
                    " или товара пока нет в базе данных.");
        }
    }

}
