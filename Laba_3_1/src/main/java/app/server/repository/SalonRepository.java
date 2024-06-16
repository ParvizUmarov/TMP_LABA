package app.server.repository;

import app.IO;
import app.Settings;
import app.server.entity.Customer;
import app.server.entity.Salon;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SalonRepository {
    private IO logger = new IO();

    public Collection<Salon> getSalons(){
        Collection<Salon> salons = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"salons\" LIMIT 10;";
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);
                while (rs.next()) {
                    salons.add(new Salon(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("address"),
                            rs.getString("images")
                            ));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return salons;
    }

}
