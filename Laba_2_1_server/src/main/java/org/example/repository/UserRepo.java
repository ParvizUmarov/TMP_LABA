package org.example.repository;

import org.example.Settings;
import org.example.entity.User;

import java.sql.*;

public class UserRepo {
    private String userName;
    private String userPassword;

    public UserRepo() {}

    public boolean getUserFromDb(User user) {
        userName = "";
        userPassword = "";
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                System.out.println("Successful connection to db");
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"user\" WHERE name = '"+user.username()+"' and password = '"+user.password()+"'";
                ResultSet rs = st.executeQuery(request);
                System.out.println("request: " + request);
                while (rs.next()) {
                    userName =  rs.getString("name");
                    userPassword = rs.getString("password");

                    var userFromDb = new User(userName, userPassword);

                    System.out.println(userFromDb);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(!userName.isEmpty()){
            return true;
        }else {
            return false;
        }

    }
}
