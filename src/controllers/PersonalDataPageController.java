package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modeles.PersonalModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import trainingdata.App;

public class PersonalDataPageController implements Initializable {

    @FXML
    private TextField milataryid;
    @FXML
    private ComboBox<String> rank;
    @FXML
    private TextField name;
    @FXML
    private TextField personalid;
    @FXML
    private ComboBox<String> unit;
    @FXML
    private TableView<PersonalModel> personaltable;
    @FXML
    private TableColumn<?, ?> milataryid_col;
    @FXML
    private TableColumn<?, ?> rank_col;
    @FXML
    private TableColumn<?, ?> name_col;
    @FXML
    private TableColumn<?, ?> personalid_col;
    @FXML
    private TableColumn<?, ?> unit_col;
    @FXML
    private TableColumn<?, ?> BirthDate_col;
    @FXML
    private TableColumn<?, ?> socialStatus_col;
    @FXML
    private TableColumn<?, ?> weight_col;
    @FXML
    private TableColumn<?, ?> Length_col;

    ObservableList<String> rankComboBoxlist = FXCollections.observableArrayList("فريق اول", "فريق", "لواء", "عميد", "عقيد", "مقدم", "رائد", "نقيب", "ملازم أول", "ملازم", "رئيس رقباء", "رقيب أول", "رقيب", "وكيل رقيب", "عريف", "جندي أول", "جندي");
    ObservableList<String> socialStatuslist = FXCollections.observableArrayList("متزوج", "أعزب");
    ObservableList<PersonalModel> personalList = FXCollections.observableArrayList();
    ObservableList<String> uintComboBoxlist = FXCollections.observableArrayList();
    String selectedMilatryid = null;
    Window stage = null;
    File imagefile = null;
    @FXML
    private TextField imageUrl;
    @FXML
    private ComboBox<String> BirthDay;
    @FXML
    private ComboBox<String> BirthMonth;
    @FXML
    private ComboBox<String> BirthYear;
    @FXML
    private ComboBox<String> socialStatus;
    @FXML
    private TextField weight;
    @FXML
    private TextField Length;
    @FXML
    private TextField execlUrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(rankComboBoxlist, rank);
        FillComboBox.fillComboBox(socialStatuslist, socialStatus);
        refreshpersonaltableTableView();
        getTableRow(personaltable);
        getTableRowByInterKey(personaltable);
        AppDate.setDateValue(BirthDay, BirthMonth, BirthYear);
        clearListCombobox();
        refreshListCombobox();
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "personaldata";
        String fieldName = null;
        String[] data = {getMilataryid(), getName(), getRank(), getUnit(), getPersonalid(), getBirthDate(), getSocialStatus(), getWeight(), getLength()};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`MILITARYID`,`NAME`,`RANK`,`UNIT`,`PERSONALID`,`BIRTHDATE`,`SOCIALSTATUS`,`WEIGHT`,`LENGTH`,`PERSONALIMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`MILITARYID`,`NAME`,`RANK`,`UNIT`,`PERSONALID`,`BIRTHDATE`,`SOCIALSTATUS`,`WEIGHT`,`LENGTH`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }
        boolean milataryidState = FormValidation.textFieldNotEmpty(milataryid, "الرجاء ادخال الرقم العسكري");
        boolean milataryidExisting = FormValidation.ifexisting("personaldata", "MILITARYID", "MILITARYID='" + getMilataryid() + "'", "لا يمكن تكرار الرقم العسكري");
        boolean personalidExisting = FormValidation.ifexisting("personaldata", "PERSONALID", "PERSONALID='" + getPersonalid() + "'", "لا يمكن تكرار رقم الهوية ");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean unitState = FormValidation.comboBoxNotEmpty(unit, "الرجاء ادخال الوحده");
        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبه");
        if (rankState && nameState && unitState && milataryidState && milataryidExisting && personalidExisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                refreshpersonaltableTableView();
                clearField(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "personaldata";
        String fieldName = null;
        String[] data = {getMilataryid(), getName(), getRank(), getUnit(), getPersonalid(), getBirthDate(), getSocialStatus(), getWeight(), getLength()};
        if (imagefile != null) {
            fieldName = "`MILITARYID`=?,`NAME`=?,`RANK`=?,`UNIT`=?,`PERSONALID`=? ,`BIRTHDATE`=?,`SOCIALSTATUS`=?,`WEIGHT`=?,`LENGTH`=?,`PERSONALIMAGE`=?";
        } else {
            fieldName = "`MILITARYID`=?,`NAME`=?,`RANK`=?,`UNIT`=?,`PERSONALID`=?,`BIRTHDATE`=?,`SOCIALSTATUS`=?,`WEIGHT`=?,`LENGTH`=?";
        }
        boolean milataryidState = FormValidation.textFieldNotEmpty(milataryid, "الرجاء ادخال الرقم العسكري");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean unitState = FormValidation.comboBoxNotEmpty(unit, "الرجاء ادخال الوحده");
        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبه");
        if (rankState && nameState && unitState && milataryidState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "MILITARYID = '" + selectedMilatryid + "'", imagefile);
                refreshpersonaltableTableView();
                clearField(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + getMilataryid() + "'", "لا توجد بيانات للحذف");
        if (milataryidExisting) {
            try {
                DatabaseAccess.delete("personaldata", "MILITARYID = '" + selectedMilatryid + "'");
                DatabaseAccess.delete("coursesdata", "MILITARYID = '" + selectedMilatryid + "'");
                refreshpersonaltableTableView();
                clearField(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }

    }

    public String getMilataryid() {
        return milataryid.getText();
    }

    public String getRank() {
        return rank.getValue();
    }

    public String getName() {
        return name.getText();
    }

    public String getPersonalid() {
        return personalid.getText();
    }

    public String getUnit() {
        return unit.getValue();
    }

    public String getSocialStatus() {
        return socialStatus.getValue();
    }

    public String getWeight() {
        return weight.getText();
    }

    public String getLength() {
        return Length.getText();
    }

    public String getBirthDate() {
        if (BirthDay.getValue() != null && BirthMonth.getValue() != null && BirthYear.getValue() != null) {
            return AppDate.getDate(BirthDay, BirthMonth, BirthYear);
        }
        return null;
    }

    public void setBirthDate(String date) {
        AppDate.setSeparateDate(BirthDay, BirthMonth, BirthYear, date);
    }

    private void personaltableView() {
        try {
            ResultSet rs = DatabaseAccess.select("personaldata");
            while (rs.next()) {
                personalList.add(new PersonalModel(
                        rs.getString("MILITARYID"),
                        rs.getString("RANK"),
                        rs.getString("NAME"),
                        rs.getString("PERSONALID"),
                        rs.getString("UNIT"),
                        rs.getString("BIRTHDATE"),
                        rs.getString("SOCIALSTATUS"),
                        rs.getString("WEIGHT"),
                        rs.getString("LENGTH")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        milataryid_col.setCellValueFactory(new PropertyValueFactory<>("militaryId"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        personalid_col.setCellValueFactory(new PropertyValueFactory<>("personalid"));
        unit_col.setCellValueFactory(new PropertyValueFactory<>("unit"));
        BirthDate_col.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        socialStatus_col.setCellValueFactory(new PropertyValueFactory<>("socialStatus"));
        weight_col.setCellValueFactory(new PropertyValueFactory<>("weight"));
        Length_col.setCellValueFactory(new PropertyValueFactory<>("lenght"));

        personaltable.setItems(personalList);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<PersonalModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    milataryid.setText(list.get(0).getMilitaryId());
                    rank.setValue(list.get(0).getRank());
                    name.setText(list.get(0).getName());
                    personalid.setText(list.get(0).getPersonalid());
                    unit.setValue(list.get(0).getUnit());
                    selectedMilatryid = (list.get(0).getMilitaryId());
                    setBirthDate(list.get(0).getBirthDate());
                    socialStatus.setValue(list.get(0).getSocialStatus());
                    weight.setText(list.get(0).getWeight());
                    Length.setText(list.get(0).getLenght());
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<PersonalModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        milataryid.setText(list.get(0).getMilitaryId());
                        rank.setValue(list.get(0).getRank());
                        name.setText(list.get(0).getName());
                        personalid.setText(list.get(0).getPersonalid());
                        unit.setValue(list.get(0).getUnit());
                        selectedMilatryid = (list.get(0).getMilitaryId());
                        ResultSet rs = DatabaseAccess.select("personaldata", "MILITARYID= '" + list.get(0).getMilitaryId() + "'");
                        if (rs.next()) {
                            setBirthDate(rs.getString("BIRTHDATE"));
                            socialStatus.setValue(rs.getString("SOCIALSTATUS"));
                            weight.setText(rs.getString("WEIGHT"));
                            Length.setText(rs.getString("LENGTH"));
                        }
                        rs.close();
                    } catch (IOException | SQLException ex) {
                        Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void refreshpersonaltableTableView() {
        personalList.clear();
        personaltableView();
    }

    @FXML
    private void clearField(ActionEvent event) {
        milataryid.setText(null);
        rank.setValue(null);
        name.setText(null);
        personalid.setText(null);
        unit.setValue(null);
        imagefile = null;
        setBirthDate(null);
        socialStatus.setValue(null);
        weight.setText(null);
        Length.setText(null);
        clearListCombobox();
        refreshListCombobox();
    }

    @FXML
    private File getImageUrl(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(ext1, ext2);
        imagefile = fileChooser.showOpenDialog(stage);
        imageUrl.setText(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void getPersonalData(ActionEvent event) {
        try {
            ResultSet rs = DatabaseAccess.select("personaldata", "MILITARYID='" + milataryid.getText() + "'");
            if (rs.next()) {
                rank.setValue(rs.getString("RANK"));
                name.setText(rs.getString("NAME"));
                personalid.setText(rs.getString("PERSONALID"));
                unit.setValue(rs.getString("UNIT"));
                selectedMilatryid = rs.getString("MILITARYID");
                setBirthDate(rs.getString("BIRTHDATE"));
                socialStatus.setValue(rs.getString("SOCIALSTATUS"));
                weight.setText(rs.getString("WEIGHT"));
                Length.setText(rs.getString("LENGTH"));
            } else {
                FormValidation.showAlert(null, "لا توجد بيانات", Alert.AlertType.ERROR);
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNewUintPage(ActionEvent event) {
        App.showFxml("/view/AddNewUint");
    }

    private ObservableList filleUint(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("uint");
            try {
                while (rs.next()) {
                    list.add(rs.getString("UINTNAME"));
                }
                rs.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return list;
    }

    private void refreshListCombobox() {
        unit.setItems(filleUint(uintComboBoxlist));
    }

    public void clearListCombobox() {
        unit.getItems().clear();
    }

    @FXML
    private void updateAll(ActionEvent event) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(getEexelUrl(event));
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();

            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                List data = new ArrayList();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    cell.setCellType(CellType.STRING);
                    data.add(cell);
                }
                String rank = data.get(0).toString();
                String militryid = data.get(1).toString();
                String name = data.get(2).toString();
                String personalid = data.get(3).toString();
                String unit = data.get(4).toString();
                String[] updatdata = {name, rank, unit};
                String[] insertdata = {militryid, personalid, name, rank, unit};
                boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + militryid + "'");
                if (milataryidExisting) {
                    DatabaseAccess.updat("personaldata", "`NAME`=?,`RANK`=?,`UNIT`=?", updatdata, "MILITARYID='" + militryid + "'");
                } else {
                    DatabaseAccess.insert("personaldata", "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`", "?,?,?,?,?", insertdata);
                }
            }
            FormValidation.showAlert(null, "تم تحديث بيانات الهوية", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            FormValidation.showAlert(null, e.toString(), Alert.AlertType.ERROR);
        } finally {
            if (fis != null) {
                fis.close();

            }
        }
    }

    @FXML
    private File getEexelUrl(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("Excel files(*.xls)", "*.XLS");
        fileChooser.getExtensionFilters().addAll(ext1);
        File execlfile = fileChooser.showOpenDialog(stage);
        execlUrl.setText(execlfile.getPath());
        return execlfile;
    }

}
