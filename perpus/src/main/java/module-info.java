module com.perpus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // WAJIB ADA agar JDBC MySQL jalan

    opens com.perpus to javafx.fxml;
    opens com.perpus.app.controllers to javafx.fxml; // Izinkan JavaFX baca controller
    opens com.perpus.app.models to javafx.base;      // Izinkan TableView baca model buku/anggota
    
    exports com.perpus;
}