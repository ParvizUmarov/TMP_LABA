package app.server.repository;

import app.IO;
import app.Settings;
import app.server.entity.TokenDB;

import java.sql.*;

public class TokenRepository {
    private IO logger = new IO();

    public TokenDB getUserToken(String mail){
       TokenDB token = null;
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();

                var request = "SELECT * FROM \"token\" WHERE mail = '"+mail+"';";
                ResultSet rs = st.executeQuery(request);
                logger.debug("request: " + request);
                while (rs.next()) {
                    token = new TokenDB(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("mail"),
                            rs.getString("token")
                    );

                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return token;

    }

    public boolean deleteByToken(String token){
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                Statement st = conn.createStatement();
                var request = "DELETE FROM \"token\" WHERE token = '"+token+"';";
                st.execute(request);
                System.out.println(request);
                logger.debug("request: " + request);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }

}
