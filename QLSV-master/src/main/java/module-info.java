module main.slinkv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;

    opens controller to javafx.fxml;
    opens model to javafx.base, javafx.fxml;
    exports main.slinkv2;
}