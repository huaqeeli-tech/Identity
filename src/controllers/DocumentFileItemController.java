package controllers;

import Serveces.DocumentFileItemListener;
import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeles.DocumentFileModel;

public class DocumentFileItemController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private Label squnce;
    @FXML
    private Label documentTopic;
    String id;
    Stage stage = new Stage();
    DocumentFileModel documentFileModel;
    DocumentFileItemListener mylistener;
    String typeOfDocument;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openPdfFile(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getPdfFile(id, "documentfiles");
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void getDocumentFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext);
        File imagefile = fileChooser.showOpenDialog(stage);
        try {
            DatabaseAccess.updat("documentfiles", "`ID` ='" + id + "'", imagefile);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void scanFile(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("documentfiles", " `ID` ='" + id + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cilck(MouseEvent event) {
        mylistener.onClickListener(documentFileModel);
    }

    void setData(DocumentFileModel documentFileModell, DocumentFileItemListener mylistener) {
        this.documentFileModel = documentFileModell;
        this.mylistener = mylistener;
        squnce.setText(Integer.toString(documentFileModell.getSquens()));
        documentTopic.setText(documentFileModell.getDocumentTopic());
        id = documentFileModell.getID();
        typeOfDocument = documentFileModell.getDocumentType();
    }

}
