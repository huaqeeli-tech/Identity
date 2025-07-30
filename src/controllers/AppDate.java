package controllers;

import Validation.FormValidation;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class AppDate {

    public static void setDateValue(ComboBox day, ComboBox month, ComboBox year) {
        for (int i = 1; i <= 30; i++) {
            if (i < 10) {
                day.getItems().addAll("0" + Integer.toString(i));
            } else {
                day.getItems().addAll(Integer.toString(i));
            }
        }
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                month.getItems().addAll("0" + Integer.toString(i));
            } else {
                month.getItems().addAll(Integer.toString(i));
            }
        }
        int currentYear = HijriCalendar.getSimpleYear();
        for (int i = 0; i <= 70; i++) {
            year.getItems().addAll(Integer.toString(currentYear));
            currentYear--;
        }
    }

    public static void setCurrentDate(ComboBox day, ComboBox month, ComboBox year) {
        if (HijriCalendar.getSimpleDay() < 10) {
            day.setValue("0" + HijriCalendar.getSimpleDay());
        } else {
            day.setValue(HijriCalendar.getSimpleDay());
        }
        if (HijriCalendar.getSimpleMonth() < 10) {
            month.setValue("0" + HijriCalendar.getSimpleMonth());
        } else {
            month.setValue(HijriCalendar.getSimpleMonth());
        }
        year.setValue(HijriCalendar.getSimpleYear());
    }

    public static String getDate(ComboBox day, ComboBox month, ComboBox year) {
            return year.getValue().toString() + "/" + month.getValue().toString() + "/" + day.getValue().toString();
    }

    public static String getDay(String date) {
        String day = null;
        CharSequence seq1 = "/";
        if (date != null && date.contains(seq1)) {
            String[] parts = date.split("/");
            day = parts[2];
        }
        return day;
    }

    public static String getMonth(String date) {
        String month = null;
        CharSequence seq1 = "/";
        if (date != null && date.contains(seq1)) {
            String[] parts = date.split("/");
            month = parts[1];
        }
        return month;
    }

    public static String getYear(String date) {
        String year = null;
        CharSequence seq1 = "/";
        if (date != null && date.contains(seq1)) {
            String[] parts = date.split("/");
            year = parts[0];
        }
        return year;
    }

    public static String getAge(String date) {
        String age = null;
        if (date != null) {
            int intage = 0;
            int birthYear = Integer.parseInt(getYear(date));
            int currentYear = HijriCalendar.getSimpleYear();
            intage = currentYear - birthYear;
            age = Integer.toString(intage);
        }
        return age;
    }

    public static void setSeparateDate(ComboBox day, ComboBox month, ComboBox year, String date) {
        day.setValue(getDay(date));
        month.setValue(getMonth(date));
        year.setValue(getYear(date));
    }

    public static String getDifferenceDate(int day1, int month1, int year1, int day2, int month2, int year2) {
        String value = null;
        if (day2 < day1) {
            day2 = day2 + 30;
            month2 = month2 - 1;
        }
        if (month2 < month1) {
            month2 = month2 + 12;
            year2 = year2 - 1;
        }
        if (year2 < year1) {
            FormValidation.showAlert(null, "ادخل تاريخ صحيح", Alert.AlertType.ERROR);
        } else {
            int deffday = day2 - day1+1;
            int deffmonth = month2 - month1;
            int deffyear = year2 - year1;
            value = printedText(setDayText(deffday), setMonthText(deffmonth), setYearText(deffyear));
        }
        return value;
    }

    public static String setDayText(int dayes) {
        String value = null;
        if (dayes == 1) {
            value = "يوما واحدا ";
        } else if (dayes == 2) {
            value = "يومان ";
        } else if (dayes <= 10 && dayes > 2) {
            value = dayes + " " + "أيام";
        } else if (dayes > 10) {
            value = dayes + " " + "يوما";
        } else {
            value = null;
        }
        return value;
    }

    public static String setMonthText(int monthes) {
        String value = null;
        if (monthes == 1) {
            value = "شهرا ";
        } else if (monthes == 2) {
            value = "شهران ";
        } else if (monthes <= 10 && monthes > 2) {
            value = monthes + " " + "أشهر";
        } else if (monthes > 10) {
            value = monthes + " " + "شهرا";
        } else {
            value = null;
        }
        return value;
    }

    public static String setYearText(int years) {
        String value = null;
        if (years == 1) {
            value = "سنة ";
        } else if (years == 2) {
            value = "سنتان ";
        } else if (years <= 10 && years > 2) {
            value = years + " " + "سنوات";
        } else if (years > 10) {
            value = years + " " + "سنة";
        } else {
            value = null;
        }
        return value;
    }

    public static String printedText(String day, String month, String year) {
        String value = null;
        if (year == null && month == null) {
            value = day;
        } else if (year == null && day == null) {
            value = month;
        } else if (month == null && day == null) {
            value = year;
        } else if (year == null) {
            value = month + "و" + day;
        } else if (month == null) {
            value = year + "و" + day;
        } else if (day == null) {
            value = year + "و" + month;
        } else {
            value = year + "و" + month + "و" + day;
        }
        return value;
    }
}
