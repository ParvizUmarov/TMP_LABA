package app.server.repository;

import app.IO;
import app.Settings;
import app.server.entity.Order;
import app.server.user.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class OrdersRepository {
    private IO logger = new IO();

    public Collection<Order> getLastOrder(int id, UserType userType){
        Collection<Order> orders = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "";
                if(userType == UserType.CUSTOMER){
                    //request = "SELECT * FROM \"orders\" where customer_id = "+id+" order by time DESC LIMIT 5;;";
                }else{
                    request = "SELECT * FROM \"orders\" where barber_id = "+id+" order by time DESC LIMIT 5;";
                }
                logger.println(request);
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);

                while (rs.next()) {
                    orders.add(new Order(
                            Integer.parseInt(rs.getString("id")),
                            Integer.parseInt(rs.getString("barber_id")),
                            Integer.parseInt(rs.getString("customer_id")),
                            rs.getString("status"),
                            rs.getString("time"),
                            Integer.parseInt(rs.getString("grade"))
                    ));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return orders;

    }
}
