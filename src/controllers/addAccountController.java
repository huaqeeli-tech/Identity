package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class addAccountController implements Initializable {
    
    @FXML
    private TextField militaryid;
    @FXML
    private HBox content;
    @FXML
    private ComboBox<String> userType;
    
    ObservableList<String> userTypelist = FXCollections.observableArrayList("مدير", "مستخدم");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(userTypelist, userType);
    }
    
    @FXML
    private void colose(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void addaccount(ActionEvent event) {
        String tabelNme = "userdata";
        String fieldName = "`MILITARYID`,`USERTYPE`,`USERNAME`,`PASSWORD`,`PASSWORDSTATE`";
        String[] data = {militaryid.getText(), userType.getValue(), militaryid.getText(), "123456", "default"};
        String valuenumbers = "?,?,?,?,?";
        
        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(militaryid, "ادخل الرقم العسكري");
        boolean userTypestatus = FormValidation.comboBoxNotEmpty(userType, "اختر نوع المستخدم");
        boolean militaryIDExisting = FormValidation.ifexisting("userdata", "MILITARYID", "MILITARYID = '" + militaryid.getText() + "'", "بوجد حساب مسبق يمكنك تغير كلمة المرور والدخول مجددا");
        boolean militaryIDNotExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID = '" + militaryid.getText() + "'","لا يوجد بيانات الرجاء اضافة البيانات اولا في خانة البيانات الشخصية");
        
        if (miliataryidstatus && userTypestatus && militaryIDExisting && militaryIDNotExisting) {
            try {
                int t = DatabaseAccess.insert(tabelNme, fieldName, valuenumbers, data);
                if (t > 0) {
                    FormValidation.showAlert(null, "تم إنشاء الحساب بنجاح للمستخدم : " + militaryid.getText() + "كلمة المرور التلقائي :123456", Alert.AlertType.INFORMATION);
                }
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void editAccount(ActionEvent event) {
        String tabelNme = "userdata";
        String fieldName = "`USERTYPE`=?";
        String[] data = {userType.getValue()};
        
        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(militaryid, "ادخل الرقم العسكري");
        boolean userTypestatus = FormValidation.comboBoxNotEmpty(userType, "اختر نوع المستخدم");
        if (miliataryidstatus && userTypestatus) {
            try {
                DatabaseAccess.updat(tabelNme, fieldName, data, "`MILITARYID`='" + militaryid.getText() + "'");
                FormValidation.showAlert(null, "تم تحديث البيانات ", Alert.AlertType.INFORMATION);
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void deleteAccount(ActionEvent event) {
        String tabelNme = "userdata";
        try {
            DatabaseAccess.delete(tabelNme, "`MILITARYID`='" + militaryid.getText() + "'");
            FormValidation.showAlert(null, "تم حذف الحساب بنجاح ", Alert.AlertType.INFORMATION);
            clearField();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void clearField() {
        militaryid.setText(null);
        userType.setValue(null);
    }
    
}
