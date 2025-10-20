package controllers;

import Validation.FormValidation;
import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import com.mysql.jdbc.Statement;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.imageio.ImageIO;

public class DatabaseAccess {

    static Config config = new Config();
    static String[] data = new String[5];

    public static int insert(String tapleName, String fildName, String valueNamber, String[] data) throws IOException {
        int t = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + " )";
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            t = psm.executeUpdate();
            if (t > 0) {
            } else {
                FormValidation.showAlert(null, "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى", Alert.AlertType.ERROR);
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return t;

    }

    public static int insert(String tapleName, String fildName, String valueNamber, String[] data, File imagefile) throws IOException {
        int t = 0;
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
            t = psm.executeUpdate();
            if (t > 0) {
            } else {
                FormValidation.showAlert("", "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }

            con.close();
            psm.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return t;
    }

    public static byte[] getPdfFile(String id, String tableName) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (id == null ) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT DOCUMENTIMAGE FROM " +tableName+ " WHERE ID = "+ id + " ");
                 if (rs.next()) {
                    image = rs.getBinaryStream("DOCUMENTIMAGE");
                    if (image == null) {
                        FormValidation.showAlert(null, "لا توجد صورة", Alert.AlertType.ERROR);
                    } else {
                        pdfByte = new byte[image.available()];
                        image.read(pdfByte);
                    }
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return pdfByte;
    }
    public static byte[] getPdfFile(String id, String tableName,String documentType) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (id == null ) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                
                ResultSet rs = DatabaseAccess.getData("SELECT DOCUMENTIMAGE FROM " +tableName+ " WHERE ID = "+ id + " AND DOCUMENTTYPE = "+ documentType + "");
                if (rs.next()) {
                    image = rs.getBinaryStream("DOCUMENTIMAGE");
                    pdfByte = new byte[image.available()];
                    image.read(pdfByte);
                } else {
                    FormValidation.showAlert(null, "صورة الملف غير موجودة", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return pdfByte;
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

    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " " + "WHERE" + " " + condition;

        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
                // FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static int updateNames(String tapleName, String fildNameAndValue, String[] data, String condition) throws IOException {
        int t = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " " + "WHERE" + " " + condition;

        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            t = psm.executeUpdate();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return t;
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

    public static void updat(String tapleName, String condition, File imagefile) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET DOCUMENTIMAGE =? WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
           
            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream( 1, fin, len);
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
    public static void updat(String tapleName, String fildeNamem,String condition, File imagefile) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET "+ fildeNamem +" WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
           
            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream( 1, fin, len);
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
            Imaging imaging = new Imaging("IDENTITY", 0);
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
            String quiry = "UPDATE " + tapleName + " SET `DOCUMENTIMAGE` =? WHERE " + " " + condition;
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
