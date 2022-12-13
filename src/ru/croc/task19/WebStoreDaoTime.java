package ru.croc.task19;

import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task18.WebStoreDao;
import ru.croc.task19.pojo.Courier;
import ru.croc.task19.pojo.OrderTime;

import java.sql.Connection;
import java.util.Map;
import java.util.stream.Collectors;

public class WebStoreDaoTime extends WebStoreDao {

    private Map<Integer, Courier> couriers;
    private Map<Integer, Customer> customersById;
    private Map<Integer, OrderTime> ordersTime;

    public WebStoreDaoTime(Connection connection,DatabaseTimeCreator dbct){
        super(connection,dbct);
        this.couriers = dbct.getCouriersOutput();
        this.customersById = dbct.getCustomersOutput();
        this.ordersTime = dbct.getOrdersTime();
    }

    public Map<Integer,String> getCustomerOrders(Integer customerId) throws Exception{

        try {
            return customersById.get(customerId).getOrders().stream().collect(Collectors.toMap(Order::getOrderNum,
                    v -> "Время заказа: " + ordersTime.get(v.getOrderNum()).getTime().toString().replaceAll("T", " ") +
                            "; Имя курьера: " + ordersTime.get(v.getOrderNum()).getCourier().getFullName()));
        } catch (Exception e){
            throw new Exception(){
                @Override
                public String getMessage() {
                    return "В базе данных нет пользователя с таким ид. Обратитесь к табличке txtTables/CreatedTables.txt";
                }
            };
        }

    }

    public Map<Integer,String> getCourierOrders(Integer courierId) throws Exception{

        try {
            return couriers.get(courierId).getOrders().stream().collect(Collectors.toMap(OrderTime::getOrderNum,
                    v -> "Время заказа: " + v.getTime().toString().replaceAll("T", " ") +
                            "; Имя получателя: " + v.getCustomerName()));
        } catch (Exception e){
            throw new Exception(){
                @Override
                public String getMessage() {
                    return "В базе данных нет курьера с таким ид. Обратитесь к табличке txtTables/CreatedTables.txt";
                }
            };
        }

    }

}
