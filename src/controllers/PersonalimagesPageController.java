package controllers;

import Serveces.PeronalImageItemListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import modeles.PersonalImagesModel;

public class PersonalimagesPageController implements Initializable {

    @FXML
    private DatePicker expraronDate;
    @FXML
    private ComboBox<String> documentType;
    @FXML
    private VBox vbox;
    String militariid;
    ObservableList<PersonalImagesModel> PersonalImagesModelObject = FXCollections.observableArrayList();
    ObservableList<String> documentTypelist = FXCollections.observableArrayList("جواز ", "تأشيرة", "هوية ", "صورة ", "اخرى");
    PeronalImageItemListener mylistener;
    PersonalImagesModel personalImagesModel;
    String id;
    @FXML
    private TextField documentName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(documentTypelist, documentType);
    }

    @FXML
    private void addDocument(ActionEvent event) {
        String tabelNme = "personalimages";
        String fieldName = "MILITARYID,DOCUMENTNAME, DOCUMENTTYPE, EXPIRATIONDATE";
        String exprarondate;
        if (expraronDate.getValue() == null) {
            exprarondate = null;
        } else {
            exprarondate = expraronDate.getValue().toString();
        }
        String[] data = {militariid, documentName.getText(), documentType.getValue(), exprarondate};
        String valuenumbers = "?,?,?,?";

        boolean documentNamestatus = FormValidation.textFieldNotEmpty(documentName, "مسمى الوثيقة مطلوب");
        boolean documentTypestatus = FormValidation.comboBoxNotEmpty(documentType, "نوع الوثيقة مطلوب");
        if (documentNamestatus && documentTypestatus) {
            try {
                int t = DatabaseAccess.insert(tabelNme, fieldName, valuenumbers, data);
                if (t > 0) {
                    FormValidation.showAlert(null, "تم حفظ البيانات بنجاح : ", Alert.AlertType.INFORMATION);
                }
                refreshData();
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void editDocument(ActionEvent event) {
        String tabelNme = "personalimages";
        String fieldName = "DOCUMENTNAME=?,DOCUMENTTYPE=?, EXPIRATIONDATE=?";
        String exprarondate;
        if (expraronDate.getValue() == null) {
            exprarondate = null;
        } else {
            exprarondate = expraronDate.getValue().toString();
        }
        String[] data = {documentName.getText(), documentType.getValue(), exprarondate};

        boolean documentNamestatus = FormValidation.textFieldNotEmpty(documentName, "مسمى الوثيقة مطلوب");
        boolean documentTypestatus = FormValidation.comboBoxNotEmpty(documentType, "نوع الوثيقة مطلوب");
        if (documentNamestatus && documentTypestatus) {
            try {
                DatabaseAccess.updat(tabelNme, fieldName, data, "ID = '" + id + "'");
                refreshData();
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        String tabelNme = "personalimages";
        try {
            DatabaseAccess.delete(tabelNme, "ID = '" + id + "'");
            refreshData();
            clearField();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public void setData(String militariid) {
        this.militariid = militariid;
        refreshData();
    }

    private void clearField() {
        documentType.setValue(null);
        expraronDate.setValue(null);
    }

    private void refreshData() {
        try {
            PersonalImagesModelObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT ID, MILITARYID,DOCUMENTNAME, DOCUMENTTYPE, EXPIRATIONDATE FROM personalimages WHERE `MILITARYID` = '" + militariid + "' "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewdata(ResultSet data) {
        PersonalImagesModelObject.addAll(getData(data));
        if (PersonalImagesModelObject.size() > 0) {
            mylistener = new PeronalImageItemListener() {
                @Override
                public void onClickListener(PersonalImagesModel personalImagesModel) {
                    setChosendata(personalImagesModel);
                }

                private void setChosendata(PersonalImagesModel personalImagesModel) {
                    documentName.setText(personalImagesModel.getDocumentName());
                    documentType.setValue(personalImagesModel.getDocumentType());
                    expraronDate.setValue(LocalDate.parse(personalImagesModel.getExpirationDate()));
                    id = personalImagesModel.getID();
                }
            };

        }
        try {
            for (PersonalImagesModel personalImagesModel : PersonalImagesModelObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/personalimagesItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                PersonalimagesItemController personalimagesItemController = fxmlLoader.getController();
                personalimagesItemController.setData(personalImagesModel, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<PersonalImagesModel> getData(ResultSet rs) {
        ObservableList<PersonalImagesModel> PersonalImagesModelList = FXCollections.observableArrayList();
        PersonalImagesModel personalImagesModel;
        try {
            int squnce = 0;
            while (rs.next()) {//ID, MILITARYID, DOCUMENTTYPE, EXPIRATIONDATE, DOCUMENTIMAGE
                squnce++;
                personalImagesModel = new PersonalImagesModel();
                personalImagesModel.setSquens(squnce);
                personalImagesModel.setID(rs.getString("ID"));
                personalImagesModel.setMilitaryId(rs.getString("MILITARYID"));
                personalImagesModel.setDocumentName(rs.getString("DOCUMENTNAME"));
                personalImagesModel.setDocumentType(rs.getString("DOCUMENTTYPE"));
                personalImagesModel.setExpirationDate(rs.getString("EXPIRATIONDATE"));
                PersonalImagesModelList.add(personalImagesModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return PersonalImagesModelList;

    }

    @FXML
    private void clear(ActionEvent event) {
        clearField();
    }
}
