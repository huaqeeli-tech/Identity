package controllers;

import Validation.FormValidation;
import com.huaqeeli.identity.HomPageController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowIdentitiController implements Initializable {

    @FXML
    private ImageView personalImage;
    @FXML
    private TextField nameText;
    @FXML
    private TextField militaryid;
    @FXML
    private TextField rank;
    @FXML
    private TextField phonnamber;
    @FXML
    private TextField idNumber;
    @FXML
    private TextField specialty;
    @FXML
    private TextField note;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane content;
    String militariid;
    private NotificationService notificationService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void colose(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openMilitaryDecisions(ActionEvent event) {
        try {
            vbox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/DocumentFilePage.fxml"));
            AnchorPane pane = fxmlLoader.load();
            DocumentFilePageController documentFilePageController = fxmlLoader.getController();
            documentFilePageController.setData(militariid, "MilitaryDecisions");
            vbox.getChildren().add(pane);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openAcademicQualifications(ActionEvent event) {
        try {
            vbox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/DocumentFilePage.fxml"));
            AnchorPane pane = fxmlLoader.load();
            DocumentFilePageController documentFilePageController = fxmlLoader.getController();
            documentFilePageController.setData(militariid, "AcademicQualifications");
            vbox.getChildren().add(pane);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openMilitaryCourses(ActionEvent event) {
        try {
            vbox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/DocumentFilePage.fxml"));
            AnchorPane pane = fxmlLoader.load();
            DocumentFilePageController documentFilePageController = fxmlLoader.getController();
            documentFilePageController.setData(militariid, "MilitaryCourses");
            vbox.getChildren().add(pane);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openPromations(ActionEvent event) {
        try {
            vbox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/DocumentFilePage.fxml"));
            AnchorPane pane = fxmlLoader.load();
            DocumentFilePageController documentFilePageController = fxmlLoader.getController();
            documentFilePageController.setData(militariid, "Promations");
            vbox.getChildren().add(pane);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openPersonalData(ActionEvent event) {
        try {
            vbox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/personalimagesPage.fxml"));
            AnchorPane pane = fxmlLoader.load();
            PersonalimagesPageController personalimagesPageController = fxmlLoader.getController();
            personalimagesPageController.setData(militariid);
            vbox.getChildren().add(pane);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public void setData(String militariid, String name, String idnumber, String Rank, String phonaNumber, String Specialty, String Note) {
        militaryid.setText(militariid);
        nameText.setText(name);
        idNumber.setText(idnumber);
        rank.setText(Rank);
        phonnamber.setText(phonaNumber);
        specialty.setText(Specialty);
        note.setText(Note);
        this.militariid = militariid;
        ResultSet rs = null;
        InputStream isimage = null;
        byte[] pdfByte = null;
        OutputStream os = null;
        try {
            rs = DatabaseAccess.getData("SELECT PERSONALIMAGE FROM personaldata WHERE MILITARYID = " + militariid + " ");
            if (rs.next()) {
                byte[] imageData = rs.getBytes("PERSONALIMAGE");
                // تحميل الصورة الأصلية أولاً
                if (imageData != null) {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                    Image originalImage = new Image(inputStream);

                    // ثم تطبيق الأبعاد على ImageView
                    personalImage.setImage(originalImage);
                    personalImage.setFitWidth(130);
                    personalImage.setFitHeight(160);
                    personalImage.setPreserveRatio(false);
                    personalImage.setSmooth(true);
                    inputStream.close();
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
