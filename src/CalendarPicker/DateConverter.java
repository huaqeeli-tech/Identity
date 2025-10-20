
package CalendarPicker;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateConverter {
    
    // تحويل من ميلادي إلى هجري
    public static HijrahDate toHijri(LocalDate gregorianDate) {
        return HijrahDate.from(gregorianDate);
    }
    
    // تحويل من هجري إلى ميلادي
    public static LocalDate toGregorian(HijrahDate hijriDate) {
        return LocalDate.from(hijriDate);
    }
    
    // تنسيق التاريخ الهجري كنص عربي
    public static String formatHijri(HijrahDate hijriDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return hijriDate.format(formatter);
    }
    
    // الحصول على الاسم العربي للشهر الهجري
    public static String getArabicMonthName(HijrahDate hijriDate) {
        String[] monthNames = {
            "محرم", "صفر", "ربيع الأول", "ربيع الثاني", 
            "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", 
            "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
        };
        
        // استخدام get(ChronoField.MONTH_OF_YEAR) بدلاً من getMonthValue()
        int monthValue = hijriDate.get(ChronoField.MONTH_OF_YEAR);
        return monthNames[monthValue - 1];
    }
    
    // الحصول على رقم الشهر الهجري
    public static int getHijriMonthValue(HijrahDate hijriDate) {
        return hijriDate.get(ChronoField.MONTH_OF_YEAR);
    }
    
    // الحصول على اليوم من الشهر الهجري
    public static int getHijriDayOfMonth(HijrahDate hijriDate) {
        return hijriDate.get(ChronoField.DAY_OF_MONTH);
    }
    
    // الحصول على السنة الهجرية
    public static int getHijriYear(HijrahDate hijriDate) {
        return hijriDate.get(ChronoField.YEAR);
    }
    
    // تحويل التاريخ الهجري إلى نص عربي كامل
    public static String getHijriArabicText(HijrahDate hijriDate) {
        int day = getHijriDayOfMonth(hijriDate);
        String month = getArabicMonthName(hijriDate);
        int year = getHijriYear(hijriDate);
        
        return day + " " + month + " " + year + " هـ";
    }
    
    // إنشاء تاريخ هجري من القيم
    public static HijrahDate ofHijri(int year, int month, int day) {
        return HijrahDate.of(year, month, day);
    }
    
    // التحقق إذا كان التاريخ الهجري صحيحاً
    public static boolean isValidHijriDate(int year, int month, int day) {
        try {
            HijrahDate.of(year, month, day);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}