package controllers;

import Validation.FormValidation;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;

public class ExporteExcelSheet {

    String[] feild;
    String[] titel;
    String[] personaltitel;
    String[] personalData;
    HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet();
    Font font = workBook.createFont();

    public ExporteExcelSheet(ResultSet rs, String[] feild, String[] titel) {
        this.feild = feild;
        this.titel = titel;
    }

    public ExporteExcelSheet() {
      font.setFontHeightInPoints((short) 14); 
    }

    public ExporteExcelSheet(ResultSet rs, String[] feild, String[] titel, String[] personaltitel, String[] personalData) {
        this.feild = feild;
        this.titel = titel;
        this.personaltitel = personaltitel;
        this.personalData = personalData;
    }

    public ArrayList<Object[]> getTableData(ResultSet rs, String[] feild) throws IOException {
        ArrayList<Object[]> tableDataList = null;
        tableDataList = new ArrayList<>();
        try {
            while (rs.next()) {
                Object[] objArray = new Object[feild.length];
                for (int i = 0; i < feild.length; i++) {
                    if (rs.getString(feild[i]) == null) {
                        objArray[i] = "---";
                    } else {
                        objArray[i] = rs.getString(feild[i]);
                    }
                }
                tableDataList.add(objArray);
            }

        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

        return tableDataList;
    }

    CellStyle setTitelStyle(int columnnum) {
        CellStyle headerstyle = workBook.createCellStyle();
        headerstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerstyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerstyle.setAlignment(HorizontalAlignment.CENTER);
        headerstyle.setBorderBottom((short) 0);
        headerstyle.setBorderTop((short) 0);
        headerstyle.setBorderRight((short) 0);
        headerstyle.setBorderLeft((short) 0);
        headerstyle.setBorderLeft((short) 0);
        System.out.println(columnnum); 
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnnum-1));
        headerstyle.setFont(font);
        return headerstyle;
    }

    public CellStyle setHederStyle() {
        CellStyle headerstyle = workBook.createCellStyle();
        headerstyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerstyle.setAlignment(HorizontalAlignment.CENTER);
        headerstyle.setBorderBottom((short) 2);
        headerstyle.setBorderTop((short) 2);
        headerstyle.setBorderRight((short) 2);
        headerstyle.setBorderLeft((short) 2);
        headerstyle.setFont(font);
        return headerstyle;
    }

    public CellStyle setContentStyle() {
        CellStyle style = workBook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom((short) 2);
        style.setBorderTop((short) 2);
        style.setBorderRight((short) 2);
        style.setBorderLeft((short) 2);
        style.setFont(font);
        return style;
    }

    public void ceratHeader(String[] titel, int rownum, CellStyle style) {
        Row row = sheet.createRow(rownum);
        for (int i = 0; i < titel.length; i++) {
            Cell cell = row.createCell((short) i);
            cell.setCellValue(titel[i]);
            cell.setCellStyle(style);
        }
    }

    public void ceratContent(ArrayList<Object[]> dataList, String[] feild, int rownum, CellStyle style) {
        short rowNo = (short) rownum;
        for (Object[] objects : dataList) {
            Row row = sheet.createRow(rowNo);
            for (int i = 0; i < feild.length; i++) {
                Cell cell = row.createCell((short) i);
                cell.setCellValue(objects[i].toString());
                cell.setCellStyle(style);
            }
            rowNo++;
        }
    }

    public void writeFile(String fileName, int columnnum) {
        String file = fileName + ".xls";
        try {
            sheet.setRightToLeft(true);
            for (int i = 0; i < columnnum; i++) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream fos = new FileOutputStream(file);
            workBook.write(fos);
            fos.flush();
        } catch (FileNotFoundException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public void doExport(ArrayList<Object[]> dataList, String fileName) {
        if (dataList != null && !dataList.isEmpty()) {
            sheet.setRightToLeft(true);
            sheet.setDefaultColumnWidth(20);

            ceratHeader(personaltitel, 0, setHederStyle());
            ceratHeader(personalData, 1, setContentStyle());
            ceratHeader(titel, 2, setHederStyle());
            ceratContent(dataList, feild, 3, setContentStyle());

            String file = fileName + ".xls";
            try {
                FileOutputStream fos = new FileOutputStream(file);
                workBook.write(fos);
                fos.flush();
            } catch (FileNotFoundException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

}
