package org.example.repository;

import org.example.Settings;
import org.example.entity.MessageEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageRepo {

    public void createMessage(MessageEntity message){
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                System.out.println("Successful connection");
                Statement st = conn.createStatement();

                var request = "INSERT INTO \"message\" (username, message, time) " +
                        "VALUES ('"+ message.username() + "', '"+ message.message() + "', '"+ message.time()+"');";
               st.executeUpdate(request);
               System.out.println("request: " + request);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<MessageEntity> getAllMessages() {
        List<MessageEntity> messages = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(
                Settings.url,
                Settings.username,
                Settings.password)) {
            if (conn != null) {
                System.out.println("Successful connection");
                String request = "SELECT * FROM \"message\"";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(request);
                while (rs.next()) {
                    String username = rs.getString("username");
                    String message = rs.getString("message");
                    LocalDateTime time = rs.getTimestamp("time").toLocalDateTime();
                    MessageEntity messageEntity = new MessageEntity(username, message, time);
                    messages.add(messageEntity);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messages;
    }
}
