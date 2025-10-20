package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AddIdentitiPageController implements Initializable {

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
    private AnchorPane content;
    Stage stage = new Stage();

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
    private void addidentiti(ActionEvent event) {
        String tabelNme = "personaldata";
        String fieldName = "`MILITARYID`, `IDNUMBER`, `NAME`, `RANK`, `PHONNUMBER`, `SPECIALTY`, `NOTE`";
        String[] data = {militaryid.getText(), idNumber.getText(), nameText.getText(), rank.getText(), phonnamber.getText(), specialty.getText(), note.getText()};
        String valuenumbers = "?,?,?,?,?,?,?";

        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(militaryid, "ادخل الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryid, "يقبل ارقام فقط");
        boolean idNumberstatus = FormValidation.textFieldNotEmpty(idNumber, "أدخل رقم الهوية");
        boolean idNumberIsNumber = FormValidation.textFieldTypeNumber(idNumber, "يقبل ارقام فقط");
        boolean nameTextstatus = FormValidation.textFieldNotEmpty(nameText, "أدخل الاسم الرباعي");
        boolean rankstatus = FormValidation.textFieldNotEmpty(rank, "أدخل الرتبة");
        boolean phonnamberstatus = FormValidation.textFieldNotEmpty(phonnamber, "أدخل رقم الجوال");
        boolean phonnambIsNumber = FormValidation.textFieldTypeNumber(phonnamber, "يقبل ارقام فقط");
        boolean specialtystatus = FormValidation.textFieldNotEmpty(specialty, "أدخل التخصص");
        boolean militaryIDExisting = FormValidation.ifexisting("personaldata", "MILITARYID", "MILITARYID = '" + militaryid.getText() + "'", "تم ادخال بيانته مسبقا");

        if (miliataryidstatus && militaryIDNumber && idNumberstatus && idNumberIsNumber && nameTextstatus && rankstatus && phonnamberstatus && phonnambIsNumber && specialtystatus && militaryIDExisting) {
            try {
                int t = DatabaseAccess.insert(tabelNme, fieldName, valuenumbers, data);
                if (t > 0) {
                    FormValidation.showAlert(null, "تم حفظ البيانات بنجاح : ", Alert.AlertType.INFORMATION);
                }
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void editidentiti(ActionEvent event) {
        String tabelNme = "personaldata";
        String fieldName = "`IDNUMBER`=?, `NAME`=?, `RANK`=? , `PHONNUMBER`=?, `SPECIALTY`=?, `NOTE`=?";
        String[] data = {idNumber.getText(), nameText.getText(), rank.getText(), phonnamber.getText(), specialty.getText(), note.getText()};

        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(militaryid, "ادخل الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryid, "يقبل ارقام فقط");
        boolean idNumberstatus = FormValidation.textFieldNotEmpty(idNumber, "أدخل رقم الهوية");
        boolean idNumberIsNumber = FormValidation.textFieldTypeNumber(idNumber, "يقبل ارقام فقط");
        boolean nameTextstatus = FormValidation.textFieldNotEmpty(nameText, "أدخل الاسم الرباعي");
        boolean rankstatus = FormValidation.textFieldNotEmpty(rank, "أدخل الرتبة");
        boolean phonnamberstatus = FormValidation.textFieldNotEmpty(phonnamber, "أدخل رقم الجوال");
        boolean phonnambIsNumber = FormValidation.textFieldTypeNumber(phonnamber, "يقبل ارقام فقط");
        boolean specialtystatus = FormValidation.textFieldNotEmpty(specialty, "أدخل التخصص");

        if (miliataryidstatus && militaryIDNumber && idNumberstatus && idNumberIsNumber && nameTextstatus && rankstatus && phonnamberstatus && phonnambIsNumber && specialtystatus) {
            try {
                DatabaseAccess.updat(tabelNme, fieldName, data, " `MILITARYID` ='" + militaryid.getText() + "'");
                FormValidation.showAlert(null, "تم تحديث البيانات بنجاح : ", Alert.AlertType.INFORMATION);
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void deleteidentiti(ActionEvent event) {
        String tabelNme = "personaldata";
        try {
            DatabaseAccess.delete(tabelNme, " `MILITARYID` ='" + militaryid.getText() + "'");
            FormValidation.showAlert(null, "تم حذف البيانات بنجاح ", Alert.AlertType.INFORMATION);
            clearField();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void clearField() {
        militaryid.setText(null);
        idNumber.setText(null);
        nameText.setText(null);
        phonnamber.setText(null);
        specialty.setText(null);
        note.setText(null);
        rank.setText(null);
    }

    @FXML
    private void getIdentityData(KeyEvent event) {
        try {
            ResultSet rs = DatabaseAccess.select("personaldata", "MILITARYID = '" + militaryid.getText() + "'");
            if (rs.next()) {
                idNumber.setText(rs.getString("IDNUMBER"));
                nameText.setText(rs.getString("NAME"));
                phonnamber.setText(rs.getString("PHONNUMBER"));
                specialty.setText(rs.getString("SPECIALTY"));
                note.setText(rs.getString("NOTE"));
                rank.setText(rs.getString("RANK"));
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void addImage(ActionEvent event) {
        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(militaryid, "ادخل الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryid, "يقبل ارقام فقط");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(ext1, ext2);
        File imagefile = fileChooser.showOpenDialog(stage);

       
        if (miliataryidstatus && militaryIDNumber) {
            try {
                DatabaseAccess.updat("personaldata", " PERSONALIMAGE=? ", "`MILITARYID` ='" + militaryid.getText() + "'", imagefile);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }

    }

}
