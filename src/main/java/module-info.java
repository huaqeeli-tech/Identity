module com.huaqeeli.training {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.huaqeeli.training to javafx.fxml;
    exports com.huaqeeli.training;
    requires hibernate.jpa;
}

