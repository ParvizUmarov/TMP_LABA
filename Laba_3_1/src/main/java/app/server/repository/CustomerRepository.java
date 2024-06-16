package app.server.repository;

import app.IO;
import app.Settings;
import app.server.entity.Customer;

import java.sql.*;

public class CustomerRepository {
    private IO logger = new IO();

    public boolean registerCustomer(Customer customer){
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "INSERT INTO customer(name, surname, birthday, phone, mail, password, auth_state)" +
                        " VALUES('"+customer.name()+ "'," +
                               " '"+customer.surname()+ "'," +
                               " '"+customer.birthday()+ "'," +
                               " '"+customer.phone()+ "'," +
                               " '"+customer.mail()+ "'," +
                               " '"+customer.password()+ "'," +
                               " "+customer.authState()+
                        " );";
                logger.println(request);
                st.execute(request);
                logger.debug("request: " + request);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Customer getCustomerByName(String name){
        Customer customer = null;
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"customer\" WHERE name = '"+name+"'";
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);
                while (rs.next()) {
                    customer = new Customer(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("birthday"),
                            rs.getString("phone"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            Boolean.parseBoolean(rs.getString("auth_state"))
                    );

                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    public boolean changeAuthState(String mail, boolean state){
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "UPDATE \"customer\" SET auth_state = '"+state+"' WHERE mail = '"+mail+"';";
                st.execute(request);
                logger.debug("request: " + request);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
