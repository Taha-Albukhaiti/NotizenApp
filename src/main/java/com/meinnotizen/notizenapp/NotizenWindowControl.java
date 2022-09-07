package com.meinnotizen.notizenapp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class NotizenWindowControl {


    @FXML
    private TableView<Notizen> notizenTableView;
    @FXML
    private TableColumn<Notizen, String> descriptionColumn;
    @FXML
    private TextArea textArea;
    @FXML
    private Label datum;

    private HelloApplication helloApplication;


    public NotizenWindowControl() {
    }

    @FXML
    public void initialize() {
        descriptionColumn.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().descriptionProperty());
        showText(null);
        notizenTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showText(newValue));
    }

    public void setMeinApp(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
        notizenTableView.setItems(helloApplication.getNotizenData());
    }

    private void showText(Notizen notizen) {
        if (helloApplication != null) {
            textArea.setText(notizen.getText());
            datum.setText(notizen.getDatum());
        } else {
            datum.setText(" ");
            textArea.setText(" ");
        }

    }


    @FXML
    private void handleDeleteNotiz() {
        int selectedIndex = notizenTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Notizen seminarObject = notizenTableView.getItems().remove(selectedIndex);
            deletDataInTheFromTheDatabase(seminarObject);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(helloApplication.getPrimaryStage());
            alert.setTitle("Keine Auswahl");
            alert.setHeaderText("Kein Seminar selektiert");
            alert.setContentText("Bitte waehlen Sie ein Seminar in der Tabelle aus.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param notizen
     */
    public static void deletDataInTheFromTheDatabase(Notizen notizen) {

        try {
            String user = "root";
            String password = "Taha@1234";
            String dBurl = "jdbc:mysql://localhost:3306/Notizen";
            Connection con = DriverManager.getConnection(dBurl, user, password);

            String loeschen = "DELETE FROM Notices WHERE idNotices = " + notizen.getId() + ";";
            Statement statement = con.createStatement();
            statement.execute(loeschen);

            statement.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }





}
