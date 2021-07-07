package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modeles.NewUintModel;

public class AddNewUintController implements Initializable {

    @FXML
    private VBox content;
    @FXML
    private TextField uintName;
    @FXML
    private TableView<NewUintModel> newUintTable;
    @FXML
    private TableColumn<?, ?> unitid_col;
    @FXML
    private TableColumn<?, ?> uintName_col;
    ObservableList<NewUintModel> uintList = FXCollections.observableArrayList();
    String uintid = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshNewUintTableView();
        getTableRow(newUintTable);
        getTableRowByInterKey(newUintTable);
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    public String getUintName() {
        return uintName.getText();
    }

    public void setUintName(String uintName) {
        this.uintName.setText(uintName);
    }

    @FXML
    private void saveData(ActionEvent event) {
        String tableName = "uint";
        String fieldName = "`UINTNAME`";
        String[] data = {getUintName()};
        String valuenumbers = "?";
        boolean uintNameState = FormValidation.textFieldNotEmpty(uintName, "الرجاء ادخال اسم الوحدة");
        if (uintNameState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshNewUintTableView();
                uintName.setText(null);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void editData(ActionEvent event) {
        String tableName = "uint";
        String fieldName = "`UINTNAME`=?";
        String[] data = {getUintName()};
        boolean uintNameState = FormValidation.textFieldNotEmpty(uintName, "الرجاء ادخال اسم الوحدة");
        boolean uintidState = FormValidation.notNull(uintid, "الرجاء اختر اسم الوحدة من الجدول");
        if (uintNameState && uintidState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + uintid + "'");
                refreshNewUintTableView();
                uintName.setText(null);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void deleteData(ActionEvent event) {
        boolean uintidState = FormValidation.notNull(uintid, "الرجاء اختر اسم الوحدة من الجدول");
        if (uintidState) {
            try {
                DatabaseAccess.delete("uint", "ID = '" + uintid + "' ");
                refreshNewUintTableView();
                uintName.setText(null);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void newUintTableView() {
        try {
            ResultSet rs = DatabaseAccess.select("uint");
            while (rs.next()) {
                uintList.add(new NewUintModel(
                        rs.getString("ID"),
                        rs.getString("UINTNAME")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        unitid_col.setCellValueFactory(new PropertyValueFactory<>("uintId"));
        uintName_col.setCellValueFactory(new PropertyValueFactory<>("uintName"));

        newUintTable.setItems(uintList);
    }

    private void refreshNewUintTableView() {
        uintList.clear();
        newUintTableView();
    }

    private void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<NewUintModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    uintName.setText(list.get(0).getUintName());
                    uintid = list.get(0).getUintId();
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<NewUintModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    uintName.setText(list.get(0).getUintName());
                    uintid = list.get(0).getUintId();
                }
            }
        });
    }
}
