package controllers;

import Validation.FormValidation;
import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.mysql.jdbc.Statement;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class DatabaseAccess {

    static Config config = new Config();
    static String[] data = new String[5];

    public static int insert(String tapleName, String fildName, String valueNamber, String[] data) throws IOException {
        int lastId = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + " )";
        try {
            PreparedStatement psm = con.prepareStatement(guiry, Statement.RETURN_GENERATED_KEYS);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
            } else {
                JOptionPane.showMessageDialog(null, "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }
            ResultSet rs = psm.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return lastId;
    }

    public static int insert(String tapleName, String fildName, String valueNamber, String[] data, File imagefile) throws IOException {
        int lastId = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + ")";
        try {
            PreparedStatement psm = con.prepareStatement(guiry, Statement.RETURN_GENERATED_KEYS);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream(e + 1, fin, len);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
            } else {
                FormValidation.showAlert("", "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }
            ResultSet rs = psm.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            con.close();
            psm.close();
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return lastId;
    }

    public static com.itextpdf.text.Image getCoursImage(String militaryid, String coursid) {
        com.itextpdf.text.Image image = null;
        try {
            if (militaryid == null || coursid == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT COURSIMAGE FROM coursesdata WHERE MILITARYID = '" + militaryid + "'AND COURSID = '" + coursid + "'");
                if (rs.next()) {
                    ArrayList images = new ArrayList();
                    images.add(rs.getBytes("COURSIMAGE"));
                    byte[] scaledInstance = (byte[]) images.get(0);
                    image = com.itextpdf.text.Image.getInstance(scaledInstance);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة للشهادة", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException | BadElementException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return image;
    }

    public static byte[] getCoursPDF(String militaryid, String coursid) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (militaryid == null || coursid == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT COURSIMAGE FROM coursesdata WHERE MILITARYID = '" + militaryid + "'AND COURSID = '" + coursid + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("COURSIMAGE");
                    pdfByte = new byte[image.available()];
                    image.read(pdfByte);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة ", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, "لا توجد صورة ", Alert.AlertType.ERROR);
        }
        return pdfByte;
    }

    public static void getAllCoursImage(String militaryid) {
        com.itextpdf.text.Image image = null;
        ArrayList images = new ArrayList();
        try {
            if (militaryid == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT COURSIMAGE FROM coursesdata WHERE MILITARYID = '" + militaryid + "'");
                while (rs.next()) {
                    images.add(rs.getBytes("COURSIMAGE"));
                }
                if (images.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "لا توجد صور");
                } else {
                    byte[] scaledInstance = (byte[]) images.get(0);
                    image = Image.getInstance(scaledInstance);
                    Document document = new Document(image);
                    File f = new File("C:\\Program Files\\TrainingData\\pdf");
                    f.mkdir();
                    String path = f.getPath() + "\\showImage.pdf";
                    com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(path));
                    document.open();
                    for (Iterator localIterator = images.iterator(); localIterator.hasNext();) {
                        Object myimage = localIterator.next();
                        byte[] scaledInstance1 = (byte[]) myimage;
                        image = Image.getInstance(scaledInstance1);
                        document.setPageSize(image);
                        document.newPage();
                        image.setAbsolutePosition(0.0F, 0.0F);
                        document.add(image);
                    }
                    document.close();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            File myFile = new File(path);
                            Desktop.getDesktop().open(myFile);
                        } catch (IOException ex) {
                            FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
                        }
                    }
                }
                rs.close();
            }
        } catch (IOException | SQLException | BadElementException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        } catch (DocumentException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static int insert(String tapleName, String fildName, String valueNamber, File imagefile) throws IOException {
        int lastId = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + ")";
        try {
            PreparedStatement psm = con.prepareStatement(guiry, Statement.RETURN_GENERATED_KEYS);

            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream(1, fin, len);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
            } else {
                FormValidation.showAlert("", "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }
            ResultSet rs = psm.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            con.close();
            psm.close();
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return lastId;
    }

    public static ResultSet select(String tapleName) throws IOException {
        ResultSet rs = null;
        String guiry = "SELECT * FROM " + tapleName;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getData(String quiry) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getItems(String tapleName) throws IOException {
        ResultSet rs = null;
        String guiry = "SELECT * FROM " + tapleName;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet select(String tapleName, String condation) throws IOException {
        ResultSet rs = null;
        String guiry = "SELECT * FROM " + tapleName + " " + "WHERE" + " " + condation;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getCourses(String miliid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,personaldata.PERSONALID,coursesdata.COURSID,"
                + "coursnames.CORSNAME,coursesdata.COURSNUMBER,coursesdata.COURSPLASE,coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                + "WHERE personaldata.MILITARYID = '" + miliid + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getCourses() throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,personaldata.PERSONALID,coursesdata.COURSID,"
                + "coursnames.CORSNAME,coursesdata.COURSNUMBER,coursesdata.COURSPLASE,coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                + "WHERE personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getIdentiti(String query) throws IOException {
        ResultSet rs = null;
        PreparedStatement psm = null;
        Connection con = DatabaseConniction.dbConnector();
        try {
            psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesId(String coursid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursesdata.COURSPLASE,coursesdata.STARTDATE,coursesdata.ENDDATE FROM personaldata,coursesdata "
                + "WHERE coursesdata.COURSID = '" + coursid + "' AND personaldata.MILITARYID = coursesdata.MILITARYID  ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesPlace(String coursplace) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursnames.CORSNAME,coursesdata.COURSPLASE FROM personaldata,coursesdata,coursnames "
                + "WHERE coursesdata.COURSPLASE LIKE '" + "%" + coursplace + "%" + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesPlaceAndCoursName(String coursplace, String coursid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursnames.CORSNAME,coursesdata.COURSPLASE FROM personaldata,coursesdata,coursnames "
                + "WHERE coursesdata.COURSID = '" + coursid + "' AND coursesdata.COURSPLASE LIKE '" + "%" + coursplace + "%" + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesPlaceAndUint(String uint, String coursid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursnames.CORSNAME,coursesdata.COURSPLASE FROM personaldata,coursesdata,coursnames "
                + "WHERE coursesdata.COURSID = '" + coursid + "' AND personaldata.UNIT = '" + uint + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyUint(String uint) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT MILITARYID FROM personaldata WHERE  personaldata.UNIT = '" + uint + "' ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + "WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
                FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void updat(String tapleName, String fildNameAndValue, int[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " " + "WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setInt(i, data[i - 1]);
            }
            psm.executeUpdate();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void updat(String tapleName, String fildNameAndValue, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " WHERE" + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            psm.executeUpdate();

        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition, File imagefile) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);

            }
            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream(e + 1, fin, len);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
                FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void delete(String tapleName, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "DELETE FROM " + tapleName + " WHERE " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            Alert alert = FormValidation.confirmationDilog("تاكيد الحذف", "سوف يتم حذف السجل هل تريد المتابعة");
            if (alert.getResult() == ButtonType.YES) {
                psm.executeUpdate();
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void delete(String quiry) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(quiry);
            Alert alert = FormValidation.confirmationDilog("تاكيد الحذف", "سوف يتم حذف السجل هل تريد المتابعة");
            if (alert.getResult() == ButtonType.YES) {
                psm.executeUpdate();
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void insertImage(String tapleName, String condition) throws IOException {
        try {
            Imaging imaging = new Imaging("arshef", 0);
            String path = config.getImagePath();
            Result result = imaging.scan(Request.fromJson(
                    "{"
                    + "\"output_settings\" : [ {"
                    + "  \"type\" : \"save\","
                    + "  \"format\" : \"png\","
                    + "  \"save_path\" : \"" + path + "\\\\${TMS}${EXT}\""
                    + "} ]"
                    + "}"), "select", false, false);

            BufferedImage imgefile = result.getImage(0);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imgefile, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            Connection con = DatabaseConniction.dbConnector();
            String quiry = "UPDATE " + tapleName + " SET `COURSIMAGE` =? WHERE " + " " + condition;
            try {
                PreparedStatement psm = con.prepareStatement(quiry);
                psm.setBlob(1, is);
                int t = psm.executeUpdate();
                if (t > 0) {
                } else {
                    FormValidation.showAlert(null, "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى", Alert.AlertType.ERROR);
                }
                con.close();
                psm.close();
                is.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }

        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
