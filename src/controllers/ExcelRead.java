package controllers;

import Validation.FormValidation;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelRead {

    public static void main(String[] args) throws Exception {
        String filename = "C:\\Users\\y50\\Documents\\formation.xls";
        FileInputStream fis = null;

        Connection con = DatabaseConniction.dbConnector();
        try {
            fis = new FileInputStream(filename);
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
                String[] alldata = {rank, militryid, name, personalid, unit};
                PreparedStatement psm = null;
                String jdbc_insert_sql = "UPDATE personaldata SET"
                            + "`NAME`=?,`RANK`=?,`UNIT`=? WHERE MILITARYID='" + militryid + "' ";
                    psm = con.prepareStatement(jdbc_insert_sql);
                    psm.setString(1, name);
                    psm.setString(2, rank);
                    psm.setString(3, unit);
                    psm.executeUpdate();
//                boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + militryid + "'");
//                if (milataryidExisting) {
//                    DatabaseAccess.updat("personaldata", "`NAME`=?,`RANK`=?,`UNIT`=?", alldata, "MILITARYID='" + militryid + "'");
////                    String jdbc_insert_sql = "UPDATE personaldata SET"
////                            + "`NAME`=?,`RANK`=?,`UNIT`=? WHERE MILITARYID='" + militryid + "' ";
////                    psm = con.prepareStatement(jdbc_insert_sql);
////                    psm.setString(1, name);
////                    psm.setString(2, rank);
////                    psm.setString(3, unit);
////                    psm.executeUpdate();
//                } else {
//                    DatabaseAccess.insert("personaldata", "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`", "?,?,?,?,?", alldata);
////                    String jdbc_insert_sql = "INSERT INTO personaldata"
////                            + "(`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`) VALUES"
////                            + "(?,?,?,?,?)";
////                    psm = con.prepareStatement(jdbc_insert_sql);
////                    psm.setString(1, militryid);
////                    psm.setString(2, personalid);
////                    psm.setString(3, name);
////                    psm.setString(4, rank);
////                    psm.setString(5, unit);
////                    psm.executeUpdate();
//                }
            }
            System.out.println("تم حفظ البيانات في قاعدة البيانات");
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (fis != null) {
                fis.close();

            }
//            con.close();
        }
//        showExelData(sheetData);
//        insertData(sheetData);
    }

    private static void showExelData(List sheetData) {

        for (int i = 0; i < sheetData.size(); i++) {
            List list = (List) sheetData.get(i);
            for (int j = 0; j < list.size(); j++) {
                Cell cell = (Cell) list.get(j);
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    System.out.print(cell.getNumericCellValue());
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    System.out.print(cell.getRichStringCellValue());
                } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    System.out.print(cell.getBooleanCellValue());
                }
                if (j < list.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("");
        }
    }
}
