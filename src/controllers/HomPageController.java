package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomPageController implements Initializable {

    @FXML
    private TextField searchText;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vbox;
    String username;
    String userrank;
    String usertype;
    String userId;
    @FXML
    private Label rankLable;
    @FXML
    private Label usernameLable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openAlerts(MouseEvent event) {
    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void userAccount(ActionEvent event) {
    }

    @FXML
    private void addData(ActionEvent event) {
    }

    @FXML
    private void addFromExcel(ActionEvent event) {
    }

    @FXML
    private void editData(ActionEvent event) {
    }

    @FXML
    private void deleteData(ActionEvent event) {
    }

    public void setData(String userName, String rank, String userType, String userid)  {
        username = userName;
        userrank = rank;
        usertype = userType;
        userId = userid;
        rankLable.setText(userrank);
        usernameLable.setText(username);

    }
}
