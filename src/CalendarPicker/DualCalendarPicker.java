
package CalendarPicker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;

public class DualCalendarPicker extends VBox {
    
    private ComboBox<String> calendarTypeCombo;
    private HijriDatePicker hijriDatePicker;
    private Label displayLabel;
    private LocalDate currentDate;
    
    public DualCalendarPicker() {
        initializeUI();
    }
    
    private void initializeUI() {
        // تهيئة التاريخ الحالي
        currentDate = LocalDate.now();
        
        // إعداد التنسيقات
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("calendar-container");
        
        // إنشاء صندوق الاختيار
        calendarTypeCombo = new ComboBox<>();
        calendarTypeCombo.getItems().addAll("التقويم الهجري", "التقويم الميلادي");
        calendarTypeCombo.setValue("التقويم الميلادي");
        calendarTypeCombo.setPrefWidth(200);
        calendarTypeCombo.setOnAction(e -> switchCalendarType());
        
        // إنشاء منتقي التاريخ المخصص
        hijriDatePicker = new HijriDatePicker();
        hijriDatePicker.setValue(currentDate);
        hijriDatePicker.setPrefWidth(250);
        
        // إضافة مستمع لتغيير التاريخ
        hijriDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentDate = newValue;
                updateDisplay();
            }
        });
        
        // إنشاء التسمية لعرض التاريخ
        displayLabel = new Label();
        displayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        displayLabel.setWrapText(true);
        displayLabel.setAlignment(Pos.CENTER);
        displayLabel.setPrefWidth(350);
        
        // إضافة العناصر إلى الحاوية
        getChildren().addAll(
            new Label("اختر نوع التقويم:"),
            calendarTypeCombo,
            hijriDatePicker,
            displayLabel
        );
        
        // تحديث العرض أول مرة
        updateDisplay();
    }
    
    private void switchCalendarType() {
        String selected = calendarTypeCombo.getValue();
        hijriDatePicker.setDisplayAsHijri("التقويم الهجري".equals(selected));
        updateDisplay();
    }
    
    private void updateDisplay() {
        try {
            String selectedType = calendarTypeCombo.getValue();
            if ("التقويم الهجري".equals(selectedType)) {
                HijrahDate hijriDate = DateConverter.toHijri(currentDate);
                String displayText = "التاريخ الهجري: " + DateConverter.getHijriArabicText(hijriDate);
                displayLabel.setText(displayText);
            } else {
                String displayText = String.format("التاريخ الميلادي: %s", 
                    currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                displayLabel.setText(displayText);
            }
        } catch (Exception e) {
            displayLabel.setText("خطأ في تحويل التاريخ: " + e.getMessage());
        }
    }
    
    // دالة للحصول على التاريخ المحدد حسب نوع التقويم
    public String getSelectedDate() {
        if ("التقويم الهجري".equals(calendarTypeCombo.getValue())) {
            HijrahDate hijriDate = DateConverter.toHijri(currentDate);
            return DateConverter.getHijriArabicText(hijriDate);
        } else {
            return currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        }
    }
    
    // دالة للحصول على التاريخ كميلادي (للمعالجة)
    public LocalDate getSelectedGregorianDate() {
        return currentDate;
    }
    
    // دالة للحصول على التاريخ كهجري
    public HijrahDate getSelectedHijriDate() {
        return DateConverter.toHijri(currentDate);
    }
    
    // تعيين التاريخ الميلادي
    public void setGregorianDate(LocalDate date) {
        this.currentDate = date;
        hijriDatePicker.setValue(date);
        updateDisplay();
    }
    
    // تعيين التاريخ الهجري
    public void setHijriDate(int year, int month, int day) {
        try {
            HijrahDate hijriDate = DateConverter.ofHijri(year, month, day);
            LocalDate gregorianDate = DateConverter.toGregorian(hijriDate);
            setGregorianDate(gregorianDate);
        } catch (Exception e) {
            System.err.println("تاريخ هجري غير صحيح: " + e.getMessage());
        }
    }
    
    // فئة للتشغيل الرئيسي
    public static class MainApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            DualCalendarPicker calendar = new DualCalendarPicker();
            
            Scene scene = new Scene(calendar, 450, 250);
            
            // تحميل ملف CSS
            try {
                scene.getStylesheets().add("calendar-styles.css");
            } catch (Exception e) {
                System.out.println("ملف CSS غير موجود، سيتم استخدام التنسيقات الافتراضية");
            }
            
            primaryStage.setTitle("تقويم هجري/ميلادي - Hijri/Gregorian Calendar");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        
        public static void main(String[] args) {
            launch(args);
        }
    }
}