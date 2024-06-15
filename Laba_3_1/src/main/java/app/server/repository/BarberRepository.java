package app.server.repository;

import app.Settings;
import app.server.entity.Barber;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class BarberRepository {

    public Collection<Barber> getAllBarber(){
        Collection<Barber> allBarbers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                System.out.println("Successful connection to db");
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"barber\" ORDER BY id ASC LIMIT 5 ";
                ResultSet rs = st.executeQuery(request);
                System.out.println("request: " + request);
                while (rs.next()) {
                    int id = Integer.parseInt(rs.getString("id"));
                    boolean authState = Boolean.parseBoolean(rs.getString("auth_state"));
                    int workExperience = Integer.parseInt(rs.getString("work_experience"));
                    allBarbers.add(new Barber(
                            id,
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("birthday"),
                            rs.getString("phone"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            authState,
                            workExperience
                            ));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return allBarbers;
    }

}
