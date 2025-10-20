
package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class NotificationController {
    @FXML private TableView<Notification> notificationTable;
    
    private ObservableList<Notification> notifications;

    public void setNotifications(ObservableList<Notification> notifications) {
        this.notifications = notifications;
        notificationTable.setItems(notifications);
    }

    @FXML
    private void handleClose() {
        ((Stage) notificationTable.getScene().getWindow()).close();
    }

    @FXML
    private void handleRefresh() {
        // يمكنك إضافة منطق التحديث هنا إذا needed
        MainController mainController = new MainController();
        mainController.loadNotifications();
    }
}
