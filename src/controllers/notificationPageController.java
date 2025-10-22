package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class notificationPageController implements Initializable {

    @FXML
    private VBox vbox;
    private ObservableList<Notification> notifications;
    @FXML
    private HBox content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setNotifications(ObservableList<Notification> notifications) {
        this.notifications = notifications;
        try {
            for (Notification notification : notifications) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/notificationItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                NotificationItemController notificationItemController = fxmlLoader.getController();
                notificationItemController.setData(notification);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void colose(ActionEvent event) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cilck(MouseEvent event) {
    }

}
