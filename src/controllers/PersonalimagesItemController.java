package controllers;

import Serveces.PeronalImageItemListener;
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
import modeles.PersonalImagesModel;

public class PersonalimagesItemController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private Label squnce;
    @FXML
    private Label documentType;
    @FXML
    private Label expraronDate;
    @FXML
    private Label documentName;
    PeronalImageItemListener mylistener;
    PersonalImagesModel personalImagesModel;
    String id;
    Stage stage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openPdfFile(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getPdfFile(id, "personalimages");
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void getDocumentFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext);
        File imagefile = fileChooser.showOpenDialog(stage);
        try {
            DatabaseAccess.updat("personalimages", "`ID` ='" + id + "'", imagefile);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void scanFile(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("personalimages", " `ID` ='" + id + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cilck(MouseEvent event) {
        mylistener.onClickListener(personalImagesModel);
    }

    void setData(PersonalImagesModel personalImagesModel, PeronalImageItemListener mylistener) {
        this.personalImagesModel = personalImagesModel;
        this.mylistener = mylistener;
        squnce.setText(Integer.toString(personalImagesModel.getSquens()));
        documentName.setText(personalImagesModel.getDocumentName());
        documentType.setText(personalImagesModel.getDocumentType());
        expraronDate.setText(personalImagesModel.getExpirationDate());
        id = personalImagesModel.getID();
    }

}
