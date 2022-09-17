module com.example.images {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;


    requires org.junit.jupiter.api;


    opens com.example.images to javafx.fxml;
    exports com.example.images;
}