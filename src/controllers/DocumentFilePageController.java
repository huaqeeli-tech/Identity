package controllers;

import Serveces.DocumentFileItemListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modeles.DocumentFileModel;

public class DocumentFilePageController implements Initializable {

    @FXML
    private AnchorPane content;
    @FXML
    private TextField documentTopic;
    @FXML
    private HBox content1;
    @FXML
    private Label expraronDate11;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vbox;
    String militariid;
    String id;
    String typeOfDocument;
    ObservableList<DocumentFileModel> documentFileModellObject = FXCollections.observableArrayList();
    DocumentFileModel documentFileModel;
    DocumentFileItemListener mylistener;
    private TextField documentType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void addDocument(ActionEvent event) {
        String tabelNme = "documentfiles";
        String fieldName = "MILITARYID, DOCUMENTTOPIC, DOCUMENTTYPE";
        String[] data = {militariid, documentTopic.getText(), typeOfDocument};
        String valuenumbers = "?,?,?";

        boolean documentTopicstatus = FormValidation.textFieldNotEmpty(documentTopic, "مسمى الوثيقة مطلوب");

        if (documentTopicstatus) {
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
        String tabelNme = "documentfiles";
        String fieldName = "DOCUMENTTOPIC=?";
        String[] data = {documentTopic.getText()};

        boolean documentTopicstatus = FormValidation.textFieldNotEmpty(documentTopic, "مسمى الوثيقة مطلوب");

        if (documentTopicstatus) {
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
        String tabelNme = "documentfiles";
        try {
            DatabaseAccess.delete(tabelNme, "ID = '" + id + "'");
            refreshData();
            clearField();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        documentTopic.setText(null);
    }

    public void setData(String militariid, String typeOfDocument) {
        this.militariid = militariid;
        this.typeOfDocument = typeOfDocument;
        refreshData();
    }

    private void refreshData() {
        try {
            documentFileModellObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT ID, MILITARYID, DOCUMENTTOPIC, DOCUMENTTYPE  FROM documentfiles WHERE `MILITARYID` = '" + militariid + "' AND DOCUMENTTYPE ='" + typeOfDocument + "'"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

    private void clearField() {
        documentTopic.setText(null);
    }

    private void viewdata(ResultSet data) {
        documentFileModellObject.addAll(getData(data));
        if (documentFileModellObject.size() > 0) {
            mylistener = new DocumentFileItemListener() {

                private void setChosendata(DocumentFileModel documentFileModell) {
                    documentTopic.setText(documentFileModell.getDocumentTopic());
                    id = documentFileModell.getID();
                }

                @Override
                public void onClickListener(DocumentFileModel documentFileModell) {
                    setChosendata(documentFileModell);
                }
            };

        }
        try {
            for (DocumentFileModel documentFileModell : documentFileModellObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/DocumentFileItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                DocumentFileItemController documentFileItemController = fxmlLoader.getController();
                documentFileItemController.setData(documentFileModell, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<DocumentFileModel> getData(ResultSet rs) {
        ObservableList<DocumentFileModel> documentFileModellList = FXCollections.observableArrayList();
        DocumentFileModel documentFileModell;
        try {
            int squnce = 0;
            while (rs.next()) {//ID, MILITARYID, DOCUMENTTOPIC, DOCUMENTTYPE
                squnce++;
                documentFileModell = new DocumentFileModel();
                documentFileModell.setSquens(squnce);
                documentFileModell.setID(rs.getString("ID"));
                documentFileModell.setMilitaryId(rs.getString("MILITARYID"));
                documentFileModell.setDocumentTopic(rs.getString("DOCUMENTTOPIC"));
                documentFileModell.setDocumentType(rs.getString("DOCUMENTTYPE"));
                documentFileModellList.add(documentFileModell);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return documentFileModellList;

    }
}
