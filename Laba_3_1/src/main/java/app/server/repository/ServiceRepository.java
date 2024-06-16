package app.server.repository;

import app.IO;

import app.Settings;
import app.server.entity.Salon;
import app.server.entity.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class ServiceRepository {
    private IO logger = new IO();

    public Collection<Service> getServices() {
        Collection<Service> services = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"services\";";
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);
                while (rs.next()) {
                    services.add(new Service(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("name"),
                            Integer.parseInt(rs.getString("price"))
                    ));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return services;
    }

}
