package controllers;

import Serveces.ShowUsersPageListener;
import identity.App;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modeles.ShowUsersModel;

public class ShowUsersItemController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private Label militaryID;
    @FXML
    private Label rank;
    @FXML
    private Label specializ;
    @FXML
    private Label name;
    @FXML
    private Label personalID;
    @FXML
    private Label note;
    @FXML
    private Label squnce;
    ShowUsersPageListener mylistener;
    ShowUsersModel showUsersModel;
    @FXML
    private Label phonnumber;
    String militariid;
    String Name;
    String idnumber;
    String Rank;
    String phonaNumber;
    String Specialty;
    String Note;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(ShowUsersModel showUsersModel, ShowUsersPageListener mylistener) {
        squnce.setText(Integer.toString(showUsersModel.getSQUNCE()));
        militaryID.setText(showUsersModel.getMILITARYID());
        personalID.setText(showUsersModel.getIDNUMBER());
        rank.setText(showUsersModel.getRANK());
        name.setText(showUsersModel.getNAME());
        note.setText(showUsersModel.getNOTE());
        phonnumber.setText(showUsersModel.getPHONNUMBER());
        specializ.setText(showUsersModel.getSPECIALTY());
        militariid = showUsersModel.getMILITARYID();
        Name = showUsersModel.getNAME();
        idnumber = showUsersModel.getIDNUMBER();
        Rank = showUsersModel.getRANK();
        phonaNumber = showUsersModel.getPHONNUMBER();
        Specialty = showUsersModel.getSPECIALTY();
        Note = showUsersModel.getNOTE();
    }

    @FXML
    private void cilck(MouseEvent event) {
        mylistener.onClickListener(showUsersModel);
    }

    @FXML
    private void showFile(ActionEvent event) {
        App.lodShowIdentitiPage(militariid, Name, idnumber, Rank, phonaNumber, Specialty, Note);
    }

}
