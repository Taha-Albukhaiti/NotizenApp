module com.meinnotizen.notizenapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.meinnotizen.notizenapp to javafx.fxml;
    exports com.meinnotizen.notizenapp;
}