package app.server.repository;

import app.IO;
import app.Settings;
import app.server.entity.Barber;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class BarberRepository {
    private IO logger = new IO();

    public Collection<Barber> getAllBarber(){
        Collection<Barber> allBarbers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"barber\" ORDER BY id ASC LIMIT 5 ";
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);
                while (rs.next()) {
                    allBarbers.add(new Barber(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("birthday"),
                            rs.getString("phone"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            Boolean.parseBoolean(rs.getString("auth_state")),
                            Integer.parseInt(rs.getString("work_experience")),
                            Integer.parseInt(rs.getString("salon_id")),
                            Integer.parseInt(rs.getString("service_id"))
                            ));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return allBarbers;
    }

    public boolean changeAuthState(String mail, boolean state){
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "UPDATE \"barber\" SET auth_state = '"+state+"' WHERE mail = '"+mail+"';";
                st.execute(request);
                logger.debug("request: " + request);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Barber getBarberByName(String name){
        Barber barber = null;
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"barber\" WHERE name = '"+name+"'";
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);
                while (rs.next()) {
                    barber = new Barber(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("birthday"),
                            rs.getString("phone"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            Boolean.parseBoolean(rs.getString("auth_state")),
                            Integer.parseInt(rs.getString("work_experience")),
                            Integer.parseInt(rs.getString("salon_id")),
                            Integer.parseInt(rs.getString("service_id"))
                    );

                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return barber;
    }


    public boolean registerBarber(Barber barber){
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "INSERT INTO barber(name, surname, birthday, phone, mail, work_experience, password, auth_state, salon_id, service_id)" +
                        " VALUES('"+barber.name()+ "'," +
                        " '"+barber.surname()+ "'," +
                        " '"+barber.birthday()+ "'," +
                        " '"+barber.phone()+ "'," +
                        " '"+barber.mail()+ "'," +
                        " "+barber.workExperience()+ "," +
                        " '"+barber.password()+ "'," +
                        " '"+barber.authState()+ "'," +
                        " "+barber.salonId()+ "," +
                        " "+barber.serviceId()+
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



}
