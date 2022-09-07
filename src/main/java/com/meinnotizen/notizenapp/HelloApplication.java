package com.meinnotizen.notizenapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;
import java.util.logging.Logger;

import static com.meinnotizen.notizenapp.SqlHandling.*;

public class HelloApplication extends Application {


    private Stage primaryStage;
    private ObservableList<Notizen> notizenData = FXCollections.observableArrayList();


    public HelloApplication(){
        try {
            String user = "root";
            String password = "Taha@1234";
            //String dBurl = "jdbc:m://slo.swe.fh-luebeck.de:3306/hochschule";
            String dBurl = "jdbc:mysql://localhost:3306/Notizen";
            Connection con = DriverManager.getConnection(dBurl, user, password);
            String anfrage = "SELECT idNotices, dataandtime, description, text FROM Notices";
            Statement prepStmt = con.createStatement();
            ResultSet ergebnis = prepStmt.executeQuery(anfrage);
            while (ergebnis.next()){
                int id = ergebnis.getInt("idNotices");
                String datum = ergebnis.getString("dataandtime");
                String descr = ergebnis.getString("description");
                String text = ergebnis.getString("text");
                notizenData.add(new Notizen(id, datum, descr, text));
            }
            ergebnis.close();
            prepStmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " \n" + e.getSQLState());
        }
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Notizen");
        initRootLayout();
        //showSeminarOverview();
    }
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("notizenWindow.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();

            NotizenWindowControl controller = loader.getController();
            controller.setMeinApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public ObservableList<Notizen> getNotizenData() {
        return notizenData;
    }

}