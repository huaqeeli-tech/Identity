
package controllers;

import java.time.LocalDate;
import java.time.Period;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Notification {
    private final StringProperty documentName;
    private final StringProperty personName;
    private final StringProperty expirationDateStr;
    private final IntegerProperty daysRemaining;

    public Notification(String documentName, String personName, LocalDate expirationDate) {
        this.documentName = new SimpleStringProperty(documentName);
        this.personName = new SimpleStringProperty(personName);
        
        // تحويل التاريخ إلى String للعرض
        if (expirationDate != null) {
            this.expirationDateStr = new SimpleStringProperty(expirationDate.toString());
            
            // حساب الأيام المتبقية بطريقة أبسط
            Period period = Period.between(LocalDate.now(), expirationDate);
            int totalDays = period.getYears() * 365 + period.getMonths() * 30 + period.getDays();
            this.daysRemaining = new SimpleIntegerProperty(totalDays);
        } else {
            this.expirationDateStr = new SimpleStringProperty("غير محدد");
            this.daysRemaining = new SimpleIntegerProperty(0);
        }
    }

    // Getters
    public String getDocumentName() { return documentName.get(); }
    public String getPersonName() { return personName.get(); }
    public String getExpirationDateStr() { return expirationDateStr.get(); }
    public int getDaysRemaining() { return daysRemaining.get(); }
    
    // Properties
    public StringProperty documentNameProperty() { return documentName; }
    public StringProperty personNameProperty() { return personName; }
    public StringProperty expirationDateStrProperty() { return expirationDateStr; }
    public IntegerProperty daysRemainingProperty() { return daysRemaining; }
}
