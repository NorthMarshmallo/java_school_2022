package ru.croc.task18.dao;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.croc.task17.pojo.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDaoImpl implements ProductDao{

    private Connection connection;

    public ProductDaoImpl(Connection connection){

        this.connection = connection;

    }
    @Override
    public Product findProduct(String article) {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT p " +
                "WHERE p.article = ?")) {

            statement.setString(1,article);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Product foundProduct = new Product(rs.getString("article"), rs.getString("title"),
                        rs.getInt("price"));
                foundProduct.setId(rs.getInt("id"));
                return foundProduct;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    @Override
    public Product createProduct(Product product) throws JdbcSQLIntegrityConstraintViolationException {

        Product createdProduct = null;
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                "PRODUCT (article,title,price) VALUES ( ?,?,? )")) {

            statement.setString(1,product.getArticle());
            statement.setString(2,product.getTitle());
            statement.setInt(3,product.getPrice());
            statement.executeUpdate();
            createdProduct = findProduct(product.getArticle());

        } catch (JdbcSQLIntegrityConstraintViolationException doubleKey){
            throw doubleKey;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return createdProduct;

    }
    @Override
    public Product updateProduct(Product product) {

        Product updatedProduct = null;
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE PRODUCT SET (article,title,price) = (" + product + ") WHERE article = ?"
        )) {

            String article = product.getArticle();
            statement.setString(1,article);
            statement.executeUpdate();
            updatedProduct = findProduct(article);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return updatedProduct;

    }

    @Override
    public void deleteProduct(String article) {

        /* P.s.: упоминания о заказе удаляются, стоимость уже выданных заказов
        и потраченная пользователем сумма остаются, тк деньги уже потрачены, а
        удалить требуют только упоминания о товарах; аналогично при обновлении
        информации о товаре ранее выданные заказы остаются с той же стоимостью */


        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM PRODUCT WHERE article = ? ; " +
                        "DELETE FROM CUSTOMER_ORDER " +
                        "WHERE order_num NOT IN " +
                        "(SELECT record.order_num FROM ORDER_PRODUCT record)"
        )){

            /* удалит запись из таблицы продуктов, из таблицы связи продуктов с заказами
            все удалится благодаря on delete cascade, заданном при создании;
            в завершении удалит запись из таблицы заказов, если продуктов в
            заказе не останется после всех удалений */

            statement.setString(1,article);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
