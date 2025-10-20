package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationService {
    private Connection connection;

    public NotificationService() {
        try {
            this.connection = DatabaseConniction.dbConnector();
        } catch (IOException ex) {
            Logger.getLogger(NotificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Notification> getExpiringDocuments() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT MILITARYID, DOCUMENTTYPE, EXPIRATIONDATE FROM personalimages WHERE EXPIRATIONDATE BETWEEN ? AND ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            
            LocalDate now = LocalDate.now();
            LocalDate sixMonthsFromNow = now.plusMonths(6);
            
            stmt.setDate(1, Date.valueOf(now));
            stmt.setDate(2, Date.valueOf(sixMonthsFromNow));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String documentName = rs.getString("DOCUMENTTYPE");
                String personName = rs.getString("MILITARYID");
                Date expirationDate = rs.getDate("EXPIRATIONDATE");
                
                // التحقق من عدم وجود قيم null
                if (expirationDate != null) {
                    LocalDate localExpirationDate = expirationDate.toLocalDate();
                    notifications.add(new Notification(documentName, personName, localExpirationDate));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            // يمكنك استخدام نظام logging أفضل هنا
        }
        
        return notifications;
    }
}