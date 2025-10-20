
package controllers;

import java.io.IOException;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {
    @FXML private Button notificationButton;
    @FXML private Circle notificationBadge;
    @FXML private Label notificationCountLabel;

    private ObservableList<Notification> notifications = FXCollections.observableArrayList();
    private NotificationService notificationService;

    @FXML
    public void initialize() {
        notificationService = new NotificationService();
        setupNotificationSystem();
        loadNotifications();
    }

    private void setupNotificationSystem() {
        // تحديث الإشعارات كل ساعة
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.hours(1), 
            e -> loadNotifications()
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void loadNotifications() {
        List<Notification> expiredSoon = notificationService.getExpiringDocuments();
        notifications.setAll(expiredSoon);
        
        updateNotificationBadge(expiredSoon.size());
    }

    private void updateNotificationBadge(int count) {
        if (count > 0) {
            notificationBadge.setVisible(true);
            notificationCountLabel.setText(String.valueOf(count));
            notificationCountLabel.setVisible(true);
        } else {
            notificationBadge.setVisible(false);
            notificationCountLabel.setVisible(false);
        }
    }

    @FXML
    private void handleNotificationClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/notification-window.fxml"));
            Parent root = loader.load();
            
            NotificationController controller = loader.getController();
            controller.setNotifications(notifications);
            
            Stage stage = new Stage();
            stage.setTitle("الإشعارات - الوثائق المنتهية قريباً");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(notificationButton.getScene().getWindow());
            stage.showAndWait();
            
            // إعادة تحميل الإشعارات بعد إغلاق النافذة
            loadNotifications();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
