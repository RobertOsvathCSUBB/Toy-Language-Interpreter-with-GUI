module com.robi {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.robi to javafx.fxml;
    exports com.robi;
}
