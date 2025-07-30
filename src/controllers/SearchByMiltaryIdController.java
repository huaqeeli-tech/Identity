package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import modeles.CoursesModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class SearchByMiltaryIdController implements Initializable {

    String milatryId = null;
    @FXML
    private Label milataryid;
    @FXML
    private Label name;
    @FXML
    private Label rank;
    @FXML
    private Label unit;
    @FXML
    private TableView<CoursesModel> searchTable;
    @FXML
    private TableColumn<?, ?> squnce_col;
    @FXML
    private TableColumn<?, ?> corsname_col;
    @FXML
    private TableColumn<?, ?> coursnum_col;
    @FXML
    private TableColumn<?, ?> coursplace_col;
    @FXML
    private TableColumn<?, ?> coursDuration_col;
    @FXML
    private TableColumn<?, ?> startdate_col;
    @FXML
    private TableColumn<?, ?> enddate_col;
    @FXML
    private TableColumn<?, ?> estimate_col;
    @FXML
    private TableColumn<CoursesModel, String> image_col;
    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();
    com.itextpdf.text.Image pdfimage = null;
    String miltaryID = null;
    String coursID = null;
    String pmilitaryid = null;
    String pname = null;
    String prank = null;
    String punit = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // getPersonaldata(milatryId);
    }

    public void setMiltaryId(String milataryid) {
        milatryId = milataryid;
        getTableRow(searchTable);
        refreshcoursesTableView(milatryId);
    }

    public String getMilataryid() {
        return milataryid.getText();
    }

    public void setMilataryid(String milataryid) {
        this.milataryid.setText(milatryId);
    }

    public String getName() {
        return name.getText();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getRank() {
        return rank.getText();
    }

    public void setRank(String rank) {
        this.rank.setText(rank);
    }

    public String getUnit() {
        return unit.getText();
    }

    public void setUnit(String unit) {
        this.unit.setText(unit);
    }

    private void refreshcoursesTableView(String miliid) {
        coursList.clear();
        coursesTableView(miliid);
    }

    private void coursesTableView(String miliid) {
        try {
            ResultSet rs = DatabaseAccess.getCourses(milatryId);
            int squance = 0;
            while (rs.next()) {
                squance++;
                coursList.add(new CoursesModel(
                        squance,
                        rs.getString("coursnames.CORSNAME"),
                        rs.getString("coursesdata.COURSNUMBER"),
                        rs.getString("coursesdata.COURSPLASE"),
                        rs.getString("coursesdata.COURSDURATION"),
                        rs.getString("coursesdata.STARTDATE"),
                        rs.getString("coursesdata.ENDDATE"),
                        rs.getString("coursesdata.COURSESTIMATE")
                ));
                milataryid.setText(rs.getString("personaldata.MILITARYID"));
                name.setText(rs.getString("personaldata.NAME"));
                rank.setText(rs.getString("personaldata.RANK"));
                unit.setText(rs.getString("personaldata.UNIT"));
                miltaryID = rs.getString("personaldata.MILITARYID");
                coursID = rs.getString("coursesdata.COURSID");
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        squnce_col.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        corsname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));
        coursnum_col.setCellValueFactory(new PropertyValueFactory<>("coursNumber"));
        coursplace_col.setCellValueFactory(new PropertyValueFactory<>("coursplace"));
        coursDuration_col.setCellValueFactory(new PropertyValueFactory<>("coursDuration"));
        startdate_col.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        enddate_col.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        estimate_col.setCellValueFactory(new PropertyValueFactory<>("estimate"));
        Callback<TableColumn<CoursesModel, String>, TableCell<CoursesModel, String>> cellFactory
                = (final TableColumn<CoursesModel, String> param) -> {
                    final TableCell<CoursesModel, String> cell = new TableCell<CoursesModel, String>() {

                final Button btn = new Button();

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            try {
                                if (miltaryID == null || coursID == null) {
                                    FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
                                } else {
                                    pdfimage = DatabaseAccess.getCoursImage(miltaryID, coursID);
//                                            pdfimage = DatabaseAccess.getCoursImage(miltaryID);
                                    ShowPdf.writePdf(pdfimage);
                                    pdfimage = null;
                                    miltaryID = null;
                                    coursID = null;
                                }
                            } catch (Exception ex) {
                                FormValidation.showAlert(null, "لا توجد صورة", Alert.AlertType.ERROR);
                            }
                        });
                        btn.setStyle("-fx-font-family: 'URW DIN Arabic';"
                                + "    -fx-font-size: 10px;"
                                + "    -fx-background-color: #769676;"
                                + "    -fx-background-radius: 10;"
                                + "    -fx-text-fill: #FFFFFF;"
                                + "    -fx-effect: dropshadow(three-pass-box,#3C3B3B, 20, 0, 5, 5); ");
                        Image image = new Image("/images/pdf.png");
                        ImageView view = new ImageView(image);
                        btn.setGraphic(view);
                        setGraphic(btn);
                        setText(null);
                    }

                }
            };
                    return cell;
                };
        image_col.setCellFactory(cellFactory);

        searchTable.setItems(coursList);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<CoursesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        coursID = list.get(0).getCoursId(list.get(0).getCoursname());
                        ResultSet rs = DatabaseAccess.select("coursesdata", "MILITARYID = '" + milatryId + "'AND COURSID = '" + coursID + "'");
                        if (rs.next()) {
                            miltaryID = rs.getString("MILITARYID");
                        }
                    } catch (SQLException | IOException ex) {
                        FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }

    @FXML
    private void printData(ActionEvent event) {
        try {
//            String reportSrcFile = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\courseByid.jrxml";
            String reportSrcFile = "C:\\Program Files\\TrainingData\\reports\\courseByid.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("milataryId", milatryId);

            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void getExcelSheet(ActionEvent event) throws SQLException {
        try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName(milatryId);
            fileChooser.setInitialFileName( "بيان دورات" + milatryId );
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.getData("SELECT personaldata.MILITARYID,coursesdata.COURSID, coursnames.CORSNAME,coursesdata.COURSPLASE,"
                    + "coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                    + "WHERE coursesdata.MILITARYID = '" + milatryId + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ");
            String[] feild = {"CORSNAME", "COURSPLASE", "COURSDURATION", "STARTDATE", "ENDDATE","COURSESTIMATE"};
            String[] personalfeild = {milataryid.getText(), name.getText(), rank.getText(), unit.getText()};
            String[] titel = {"اسم الدورة", "مكان انعقادها", "مدتها", "تاريخ بداية الدورة", "تاريخ نهاية الدورة","التقدير"};
            String[] personaltitel = {"الرقم العسكري", "الاسم", "الرتبة", "الوحدة"};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(personaltitel, 0, exporter.setTitelStyle(feild.length));
                exporter.ceratHeader(personalfeild, 1, exporter.setContentStyle());
                exporter.ceratHeader(titel, 2, exporter.setHederStyle());
                exporter.ceratContent(dataList, feild, 3, exporter.setContentStyle());
                exporter.writeFile(savefile,feild.length);
            } else {
                FormValidation.showAlert(null, "There is no data available in the table to export", Alert.AlertType.ERROR);
            }
            rs.close();
            //rs1.close();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printAllData(ActionEvent event) {
        DatabaseAccess.getAllCoursImage(milataryid.getText());
    }

    private void getPersonaldata(String milataryid) {
        try {
            ResultSet rs = DatabaseAccess.select("personaldata", "MILITARYID='" + milataryid + "'");
            if (rs.next()) {
                pmilitaryid = rs.getString("MILITARYID");
                pname = rs.getString("NAME");
                prank = rs.getString("RANK");
                punit = rs.getString("UNIT");
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

}
