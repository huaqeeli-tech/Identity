package com.huaqeeli.identity;

import Serveces.ShowUsersPageListener;
import Validation.FormValidation;
import controllers.DatabaseAccess;
import controllers.LoginPageController;
import controllers.Notification;
import controllers.NotificationController;
import controllers.NotificationService;
import controllers.ShowUsersItemController;
import controllers.notificationPageController;
import identity.App;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modeles.ShowUsersModel;

public class HomPageController implements Initializable {

    @FXML
    private Label rankLable;
    @FXML
    private Label usernameLable;
    String username;
    String userrank;
    String usertype;
    String userId;
    public boolean logOut;
    @FXML
    private TextField searchText;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vbox;
    @FXML
    private Button addAccountButton;
    @FXML
    private AnchorPane content;
    ObservableList<ShowUsersModel> ShowUsersObject = FXCollections.observableArrayList();
    private ObservableList<Notification> notifications = FXCollections.observableArrayList();
    private NotificationService notificationService;
    ShowUsersPageListener mylistener;
    ShowUsersModel showUsersModel;
    @FXML
    private Label notificationCountLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notificationService = new NotificationService();
        setupNotificationSystem();
        loadNotifications();
    }

    public void close() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    public void setData(String userName, String rank, String userType, String userid) throws IOException {
        username = userName;
        userrank = rank;
        usertype = userType;
        userId = userid;
        rankLable.setText(userrank);
        usernameLable.setText(username);

    }

    @FXML
    private void userAccount(ActionEvent event) {
        if ("مدير".equals(usertype)) {
            App.showFxmlSpecificxy("/view/addAccount", 850, 95);
        } else {
            FormValidation.showAlert(null, "ليس لديك الصلاحية في الدخول", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addIdentity(ActionEvent event) {
        App.showFxmlSpecificxy("/view/addIdentitiPage", 600, 95);
    }

    @FXML
    private void addFromExcel(ActionEvent event) {
        App.showFxmlSpecificxy("/view/UpdateAllFromExcle", 600, 95);
    }

    @FXML
    private void searchData(ActionEvent event) {
        refreshData(searchText.getText());
    }

    @FXML
    private void loguot(ActionEvent event) {
        try {
            close();
            LoginPageController login = new LoginPageController();
            login.lodLogingPage();
            logOut = true;
        } catch (IOException ex) {
            Logger.getLogger(HomPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshData(String searchtext) {
        try {
            ShowUsersObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT `MILITARYID`, `IDNUMBER`, `NAME`, `RANK`, `PHONNUMBER`, `SPECIALTY`, `NOTE` FROM personaldata WHERE `MILITARYID` = '" + searchtext + "' OR `IDNUMBER` = '" + searchtext + "'  OR `NAME` LIKE '" + "%" + searchtext + "%" + "' "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewdata(ResultSet data) {
        ShowUsersObject.addAll(getData(data));
        if (ShowUsersObject.size() > 0) {
            mylistener = new ShowUsersPageListener() {
                @Override
                public void onClickListener(ShowUsersModel showUsersModel) {
                    setChosendata(showUsersModel);
                }

                private void setChosendata(ShowUsersModel showUsersModel) {

                }
            };

        }
        try {
            for (ShowUsersModel showUsersModel : ShowUsersObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/ShowUsersItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                ShowUsersItemController showUsersItemController = fxmlLoader.getController();
                showUsersItemController.setData(showUsersModel, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

    private ObservableList<ShowUsersModel> getData(ResultSet rs) {
        ObservableList<ShowUsersModel> ShowUsersModelList = FXCollections.observableArrayList();
        ShowUsersModel showUsersModel;
        try {
            int squnce = 0;
            while (rs.next()) {//MILITARYID, IDNUMBER, NAME, RANK, PHONNUMBER, SPECIALTY, NOTE
                squnce++;
                showUsersModel = new ShowUsersModel();
                showUsersModel.setSQUNCE(squnce);
                showUsersModel.setMILITARYID(rs.getString("MILITARYID"));
                showUsersModel.setIDNUMBER(rs.getString("IDNUMBER"));
                showUsersModel.setNAME(rs.getString("NAME"));
                showUsersModel.setRANK(rs.getString("RANK"));
                showUsersModel.setPHONNUMBER(rs.getString("PHONNUMBER"));
                showUsersModel.setSPECIALTY(rs.getString("SPECIALTY"));
                showUsersModel.setNOTE(rs.getString("NOTE"));
                ShowUsersModelList.add(showUsersModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return ShowUsersModelList;
    }

    public void setupNotificationSystem() {
        // تحديث الإشعارات كل ساعة
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.minutes(1),
                e -> loadNotifications()
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void loadNotifications() {
        List<Notification> expiredSoon = notificationService.getExpiringDocuments();
        notifications.setAll(expiredSoon);

        updateNotificationBadge(expiredSoon.size());
    }

    private void updateNotificationBadge(int count) {
        if (count > 0) {
            notificationCountLabel.setText(String.valueOf(count));
            notificationCountLabel.setVisible(true);
        } else {
            notificationCountLabel.setVisible(false);
        }
    }

    @FXML
    private void handleNotificationClick(MouseEvent event) {
        try {
            loadNotifications();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/notificationPage.fxml"));
            Parent root = loader.load();
            notificationPageController controller = loader.getController();
            controller.setNotifications(notifications);

            Stage stage = new Stage();
            stage.setX(10);
            stage.setY(95);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();

            // إعادة تحميل الإشعارات بعد إغلاق النافذة
            loadNotifications();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
