module com.perpus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 

    opens com.perpus to javafx.fxml;
    opens com.perpus.app.controllers to javafx.fxml; 
    opens com.perpus.app.models to javafx.base;    
    
    exports com.perpus;
}