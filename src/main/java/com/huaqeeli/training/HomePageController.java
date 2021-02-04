package com.huaqeeli.training;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomePageController implements Initializable {

    @FXML
    private BorderPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void close(MouseEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void personalData(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/personalDataPage"));

    }

    @FXML
    private void trainingData(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/trainingDataPage"));
    }

}
