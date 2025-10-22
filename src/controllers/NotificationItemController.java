package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class NotificationItemController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private Label militaryidText;
    @FXML
    private Label nameText;
    @FXML
    private Label documentNameText;
    @FXML
    private Label expirationDateText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void cilck(MouseEvent event) {
    }

    public void setData(Notification notification) {
        militaryidText.setText(notification.getMilitaryid());
        nameText.setText(notification.getPersonName());
        documentNameText.setText(notification.getDocumentName());
        expirationDateText.setText(notification.getExpirationDateStr());
    }
}
