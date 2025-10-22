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
        String query = "SELECT \n"
                + "    `personalimages`.`MILITARYID` as MILITARYID,\n"
                + "    `personalimages`.`DOCUMENTNAME` as DOCUMENTNAME,\n"
                + "    `personalimages`.`DOCUMENTTYPE` as DOCUMENTTYPE,\n"
                + "    `personalimages`.`EXPIRATIONDATE` as EXPIRATIONDATE,\n"
                + "    `personaldata`.`NAME` as NAME\n"
                + "FROM `identity`.`personalimages`, `identity`.`personaldata`\n"
                + "WHERE  `personalimages`.`MILITARYID` = `personaldata`.`MILITARYID`";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            LocalDate now = LocalDate.now();
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String documentType = rs.getString("DOCUMENTTYPE");
                String militaryid = rs.getString("MILITARYID");
                String personName = rs.getString("NAME");
                String documentName = rs.getString("DOCUMENTNAME");
                Date expirationDate = rs.getDate("EXPIRATIONDATE");

                if (expirationDate != null) {
                    LocalDate localExpirationDate = expirationDate.toLocalDate();

                    // تحديد فترة التنبيه حسب نوع الوثيقة
                    boolean shouldNotify = false;

                    if ("جواز".equals(documentType)) {
                        // تنبيه قبل 6 أشهر للجواز
                        LocalDate sixMonthsBefore = localExpirationDate.minusMonths(7);
                        shouldNotify = !now.isBefore(sixMonthsBefore) && !now.isAfter(localExpirationDate);

                    } else if ("تأشيرة".equals(documentType)) {
                        // تنبيه قبل 3 أيام للتأشيرة
                        LocalDate threeDaysBefore = localExpirationDate.minusDays(3);
                        shouldNotify = !now.isBefore(threeDaysBefore) && !now.isAfter(localExpirationDate);

                    } else if ("هوية".equals(documentType)) {
                        // تنبيه قبل 3 أشهر للهوية
                        LocalDate threeMonthsBefore = localExpirationDate.minusMonths(3);
                        shouldNotify = !now.isBefore(threeMonthsBefore) && !now.isAfter(localExpirationDate);
                    }

                    // إضافة التنبيه إذا كان ضمن الفترة المحددة
                    if (shouldNotify) {
                        notifications.add(new Notification(militaryid,personName,documentName,  localExpirationDate));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // يمكنك استخدام نظام logging أفضل هنا
        }

        return notifications;
    }
}
