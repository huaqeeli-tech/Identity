/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalendarPicker;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;

public class HijriDatePicker extends DatePicker {
    private boolean displayAsHijri = false;
    private boolean updating = false; // لمنع التكرار
    
    public HijriDatePicker() {
        super();
        
        // تعيين التاريخ الحالي بشكل آمن
        super.setValue(LocalDate.now());
        
        // تحديث العرض عند تغيير القيمة
        valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!updating && newValue != null) {
                updateDisplay();
            }
        });
    }
    
    public void setDisplayAsHijri(boolean hijri) {
        this.displayAsHijri = hijri;
        updateDisplay();
    }
    
    public boolean isDisplayAsHijri() {
        return displayAsHijri;
    }
    
    private void updateDisplay() {
        if (updating || getValue() == null) {
            return;
        }
        
        try {
            updating = true;
            
            if (displayAsHijri) {
                // تحويل إلى هجري وعرضه
                HijrahDate hijriDate = DateConverter.toHijri(getValue());
                if (hijriDate != null) {
                    String hijriText = DateConverter.getHijriArabicText(hijriDate);
                    TextField textField = getEditor();
                    if (textField != null) {
                        textField.setText(hijriText);
                    }
                }
            } else {
                // عرض الميلادي
                String gregorianText = getValue().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                TextField textField = getEditor();
                if (textField != null) {
                    textField.setText(gregorianText);
                }
            }
        } catch (Exception e) {
            System.err.println("خطأ في تحديث العرض: " + e.getMessage());
        } finally {
            updating = false;
        }
    }
    
    // دالة آمنة لتعيين القيمة مع التحديث
    public void setDateWithUpdate(LocalDate value) {
        if (updating) {
            return;
        }
        
        updating = true;
        try {
            super.setValue(value);
            updateDisplay();
        } finally {
            updating = false;
        }
    }
    
    // الحصول على التاريخ الهجري الحالي
    public HijrahDate getHijriValue() {
        if (getValue() == null) {
            return null;
        }
        return DateConverter.toHijri(getValue());
    }
    
    // تعيين قيمة هجرية
    public void setHijriValue(int year, int month, int day) {
        try {
            HijrahDate hijriDate = DateConverter.ofHijri(year, month, day);
            LocalDate gregorianDate = DateConverter.toGregorian(hijriDate);
            setDateWithUpdate(gregorianDate);
        } catch (Exception e) {
            System.err.println("تاريخ هجري غير صحيح: " + e.getMessage());
        }
    }
}