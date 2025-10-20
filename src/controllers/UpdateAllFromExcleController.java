package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.progress.RingProgressIndicator;

public class UpdateAllFromExcleController implements Initializable {

    @FXML
    private VBox vbox;
    @FXML
    private ListView<String> showArea;
    @FXML
    private TextField excleFileUrl;
    File execlfile = null;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private File getExcleFile(ActionEvent event) {
        Window stage = null;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("Excel files(*.xls)", "*.XLS");
        fileChooser.getExtensionFilters().addAll(ext1);
        execlfile = fileChooser.showOpenDialog(stage);
        excleFileUrl.setText(execlfile.getPath());
        return execlfile;
    }

    @FXML
    private void updateFromjExcle(ActionEvent event) throws IOException {
        Alert alert = FormValidation.confirmationDilog("تنبيه", "يجب ان يكون ترتيب ملف الاكسل كتالي :" + "\n" + "الرقم العسكري - الرتبة - الاسم - رقم الهوية - رقم الجوال- التخصص" + "\n" + "هل تريد المتابعة ؟");
        if (execlfile == null) {
            FormValidation.showAlert(null, "الرجاء تحديد ملف الاكسل", Alert.AlertType.ERROR);
        } else {
            if (alert.getResult() == ButtonType.YES) {
                RingProgressIndicator rpi = new RingProgressIndicator();
                rpi.setRingWidth(200);
                rpi.makeIndeterminate();
                stackPane.getChildren().addAll(rpi);
                // When creating the thread, pass the showArea reference
                GetUpdate getUpdate = new GetUpdate(rpi, execlfile, showArea);
                getUpdate.start();
            }
        }
    }

    @FXML
    private void colose(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

//    public class GetUpdate extends Thread {
//
//        RingProgressIndicator rpi;
//        private File execlfile;
//        int progrss = 0;
//        FileInputStream fis = null;
//        int t = 0;
//
//        public GetUpdate(RingProgressIndicator rpi, File execlfile) {
//            this.rpi = rpi;
//            this.execlfile = execlfile;
//        }
//
//        @Override
//        public void run() {
//            try {
//                for (int i = 0; i <= 10; i++) {
//                    progrss = i;
//                    Thread.sleep(100);
//                    Platform.runLater(() -> {
//                        rpi.setProgress(progrss);
//                    });
//                }
//
//                try {
//                    fis = new FileInputStream(execlfile);
//                    HSSFWorkbook workbook = new HSSFWorkbook(fis);
//                    HSSFSheet sheet = workbook.getSheetAt(0);
//                    Iterator rows = sheet.rowIterator();
//                    while (rows.hasNext()) {
//                        HSSFRow row = (HSSFRow) rows.next();
//                        Iterator cells = row.cellIterator();
//                        List data = new ArrayList();
//                        while (cells.hasNext()) {
//                            HSSFCell cell = (HSSFCell) cells.next();
//                            cell.setCellType(CellType.STRING);
//                            data.add(cell);
//                        }
//                        String militryid = data.get(0).toString();
//                        String rank = data.get(1).toString();
//                        String name = data.get(2).toString();
//                        String personalid = data.get(3).toString();
//                        String phonnumber = data.get(4).toString();
//                        String specialty = data.get(5).toString();
//                        boolean milataryidNotnull = FormValidation.cellNotNull(militryid, "الرقم العسكري يجب الا يحتوي على قيمة فارغة");
//                        boolean rankNotnull = FormValidation.cellNotNull(rank, "الرتبة يجب الا يحتوي على قيمة فارغة");
//                        boolean nameNotnull = FormValidation.cellNotNull(name, "الاسم يجب الا يحتوي على قيمة فارغة");
//                        boolean personalidNotnull = FormValidation.cellNotNull(personalid, "رقم الهوية يجب الا يحتوي على قيمة فارغة");
//                        boolean specialtyNotnull = FormValidation.ifCellNull(specialty);
////                        String[] updatdata = null;
////                        String[] insertdata = null;
//                        if (specialtyNotnull) {
//                            specialty = "null";
//                        }
//                       
//                        while (milataryidNotnull && rankNotnull && nameNotnull && personalidNotnull) {
//                            updatdata = new String[]{name, rank, personalid, phonnumber, specialty};
//                            insertdata = new String[]{militryid, personalid, name, rank, phonnumber, specialty};
//                        }
//
//                        String[] updatdata = {name, rank, personalid, phonnumber, specialty};;
//                        String[] insertdata = {militryid, personalid, name, rank, phonnumber, specialty};;
//                        boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + militryid + "'");
//
//                        if (milataryidExisting) {
//                            showArea.getItems().add(updatdata[1] + "    |     " + updatdata[0]);
//                            t = DatabaseAccess.updatNames("personaldata", "`NAME`=?,`RANK`=?,`PERSONALID`=?,`PHONNUMBER`=?,`SPECIALTY`=?", updatdata, "MILITARYID='" + militryid + "'");
//                            System.out.println("التحديث :    " + updatdata[1] + "    |     " + updatdata[0]);
//                        } else {
//                            showArea.getItems().add(insertdata[3] + "    |     " + insertdata[2]);
//                            t = DatabaseAccess.insert("personaldata", "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`PHONNUMBER`,`SPECIALTY`", "?,?,?,?,?,?", insertdata);
//                            System.out.println("الاضافة:     " + insertdata[3] + "    |     " + insertdata[2]);
//                        }
//                    }
//
//                } catch (IOException ex) {
//                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//                } finally {
//                    if (fis != null) {
//                        try {
//                            fis.close();
//                        } catch (IOException ex) {
//                            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//                        }
//                    }
//                }
//
//                for (int i = 10; i <= 100; i++) {
//                    progrss = i;
//                    Thread.sleep(100);
//                    Platform.runLater(() -> {
//                        rpi.setProgress(progrss);
//                    });
//                }
//
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (t > 0) {
//                            rpi.setVisible(false);
//                            FormValidation.showAlert(null, "تم تحديث البيانات", Alert.AlertType.INFORMATION);
//                        } else {
//                            rpi.setVisible(false);
//                            FormValidation.showAlert(null, "حدثت مشكلة", Alert.AlertType.ERROR);
//                        }
//                    }
//                });
//
//            } catch (InterruptedException ex) {
//                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//            }
//
//        }
//
//    }
    public class GetUpdate extends Thread {

        private RingProgressIndicator rpi;
        private File excelFile;
        private ListView<String> showArea;
        private int progress = 0;
        private int totalRows = 0;
        private int processedRows = 0;
        private int successfulOperations = 0;

        public GetUpdate(RingProgressIndicator rpi, File excelFile, ListView<String> showArea) {
            this.rpi = rpi;
            this.excelFile = excelFile;
            this.showArea = showArea;
        }

        @Override
        public void run() {
            try {
                // Initial progress
                updateProgress(10);

                // Process Excel file
                processExcelFile();

                // Final progress
                updateProgress(100);

                // Show final result on FX thread
                Platform.runLater(() -> {
                    rpi.setVisible(false);
                    if (successfulOperations > 0) {
                        FormValidation.showAlert(null, "تم تحديث البيانات بنجاح. عدد العمليات: " + successfulOperations, Alert.AlertType.INFORMATION);
                    } else {
                        FormValidation.showAlert(null, "لم يتم إجراء أي عمليات", Alert.AlertType.WARNING);
                    }
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    rpi.setVisible(false);
                    FormValidation.showAlert(null, "حدث خطأ: " + ex.getMessage(), Alert.AlertType.ERROR);
                });
            }
        }

        private void processExcelFile() throws IOException, InterruptedException {
            try (FileInputStream fis = new FileInputStream(excelFile);
                    HSSFWorkbook workbook = new HSSFWorkbook(fis)) {

                HSSFSheet sheet = workbook.getSheetAt(0);
                totalRows = sheet.getPhysicalNumberOfRows();

                Iterator<Row> rows = sheet.rowIterator();
                while (rows.hasNext()) {
                    HSSFRow row = (HSSFRow) rows.next();

                    // Skip header row if needed (assuming row 0 is header)
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    processRow(row);
                    processedRows++;

                    // Update progress based on actual processing
                    int calculatedProgress = 10 + (int) ((processedRows / (double) totalRows) * 80);
                    updateProgress(calculatedProgress);

                    // Small delay to prevent UI overload
                    Thread.sleep(10);
                }
            }
        }

        private void processRow(HSSFRow row) {
            try {
                List<String> data = new ArrayList<>();
                Iterator<Cell> cells = row.cellIterator();

                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    cell.setCellType(CellType.STRING);
                    data.add(cell.getStringCellValue());
                }

                // Skip if not enough columns
                if (data.size() < 6) {
                    Platform.runLater(() -> {
                        showArea.getItems().add("سطر " + row.getRowNum() + ": عدد الأعمدة غير كافي");
                    });
                    return;
                }

                String militaryId = data.get(0);
                String rank = data.get(1);
                String name = data.get(2);
                String personalId = data.get(3);
                String phoneNumber = data.get(4);
                String specialty = data.get(5);

//                 Validate required fields
                if (!isValidField(militaryId, "الرقم العسكري")
                        || !isValidField(rank, "الرتبة")
                        || !isValidField(name, "الاسم")
                        || !isValidField(phoneNumber, "رقم الجوال")
                        || !isValidField(personalId, "رقم الهوية")) {
                    return;
                }

                // Check if military ID exists
                boolean militaryIdExists = checkMilitaryIdExists(militaryId);

                int result;
                if (militaryIdExists) {
                    // Update existing record
                    String[] updateData = {name, rank, personalId, phoneNumber, specialty};
                    result = DatabaseAccess.updateNames("personaldata",
                            "`NAME`=?,`RANK`=?,`IDNUMBER`=?,`PHONNUMBER`=?,`SPECIALTY`=?",
                            updateData, "MILITARYID='" + militaryId + "'");
                } else {
                    // Insert new record
                    String[] insertData = {militaryId, personalId, name, rank, phoneNumber, specialty};
                    result = DatabaseAccess.insert("personaldata",
                            "`MILITARYID`,`IDNUMBER`,`NAME`,`RANK`,`PHONNUMBER`,`SPECIALTY`",
                            "?,?,?,?,?,?", insertData);
                }

                if (result > 0) {
                    successfulOperations++;
                    final String displayText = militaryIdExists
                            ? "تم التحديث: " + rank + " | " + name
                            : "تم الإضافة: " + rank + " | " + name;

                    Platform.runLater(() -> {
                        showArea.getItems().add(displayText);
                    });
                }

            } catch (Exception e) {
                // Log error but don't show alert for each row to avoid flooding the UI
                System.err.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
            }
        }

        private boolean isValidField(String value, String fieldName) {
            if (value == null || value.trim().isEmpty()) {
                final String errorMessage = fieldName + " لا يمكن أن يكون فارغًا في السطر";
                Platform.runLater(() -> {
                    FormValidation.showAlert(null, errorMessage, Alert.AlertType.WARNING);
                });
                return false;
            }
            return true;
        }

        private boolean checkMilitaryIdExists(String militaryId) {
            try {
                // This should be a simple database check without UI updates
                return FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + militaryId + "'");
            } catch (Exception e) {
                System.err.println("Error checking military ID: " + e.getMessage());
                return false;
            }
        }

        private void updateProgress(int progress) {
            Platform.runLater(() -> {
                rpi.setProgress(progress);
            });
        }
    }

}
