package controllers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class NewClass {

    public static void main(String[] args) throws IOException {
        // إنشاء ملف Excel جديد
        Workbook workbook = new XSSFWorkbook();

        // إنشاء ورقة جديدة
        Sheet sheet = workbook.createSheet("تنسيق Excel");

        // إنشاء سطر جديد
        Row row = sheet.createRow(0);

        // إنشاء خلية جديدة وتنسيقها
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle);

        // حفظ الملف
        try {
            FileOutputStream fileOut = new FileOutputStream("تنسيق_Excel.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("تم إنشاء وتنسيق ملف Excel بنجاح.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
