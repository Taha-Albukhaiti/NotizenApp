package com.meinnotizen.notizenapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.*;
import java.util.List;


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

    /**
     * Initialisiert das Controller-Objekt. Wird automatisch aufgerufen nach dem Laden der fxml-Datei.
     */
    @FXML
    public void initialize() {
        descriptionColumn.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().descriptionProperty());
        showText(null);
        notizenTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showText(newValue));
    }

    /**
     * Wird vom Hauptprogramm aufgerufen, um eine Referenz an sich selbst an den Controller zu uebergeben.
     * Setzt ausserdem die im Backend hinterlegten Webinare in die Tabelle.
     *
     * @param helloApplication
     */
    public void setMeinApp(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
        notizenTableView.setItems(helloApplication.getNotizenManager().getNotizenData());
    }

    private void showText(Notizen notizen) {
        if (helloApplication != null) {
            descriptionColumn.setText(notizen.getDescription());
            textArea.setText(notizen.getText());
            datum.setText(notizen.getDatum());
        } else {
            descriptionColumn.setText(" ");
            datum.setText(" ");
            textArea.setText(" ");
        }

    }

    /**
     * Handler fuer Clicks auf "Selektiertes Notiz loeschen..." bzw. Strg-D. Loescht die selektierte Notiz.
     * Falls keine Notiz selektiert war, wird eine Warnung in einem Popup ausgegeben.
     */
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
            alert.setHeaderText("Keine Notiz selektiert");
            alert.setContentText("Bitte waehlen Sie eine Notiz in der Tabelle aus.");
            alert.showAndWait();
        }
    }


    /**
     * Hilfsmethode, die einen FileChooser für Textdateien bereitstellt. Initial ist das Arbeitsverzeichnis ausgewaehlt.
     *
     * @return FileChooser
     */
    private FileChooser getTextFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //Arbeitsverzeichnis als initiales Verzeichnis setzen
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(workingDirectory);
        //Dateiextensionen auf .txt beschraenken
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        return fileChooser;
    }

    /**
     * Handler fuer Clicks auf Laden bzw. Strg-L.
     * Oeffnet einen FileChooser für Textdateien, laedt (falls Datei ausgewaehlt)
     * die Objekte und setzt sie in die Tabelle.
     */
    @FXML
    private void handleLaden() {
        FileChooser fc = getTextFileChooser();
        File file = fc.showOpenDialog(helloApplication.getPrimaryStage());
        if (file != null) {
            //helloApplication.getNotizenManager().getNotizenData().clear();
            helloApplication.getNotizenManager().notizenLaden(file.getAbsolutePath());
            notizenTableView.setItems(helloApplication.getNotizenManager().getNotizenData());

          /*  // Noch zu bearbeiten
            List<Notizen> n = helloApplication.
                    getNotizenManager().
                    getNotizenData().
                    stream().toList();
            insertDataInTheDatabase((Notizen) helloApplication.
                    getNotizenManager().
                    getNotizenData().
                    stream().toList());

           */
        }
    }

    /**
     * Handler fuer Clicks auf Speichern bzw. Strg-S. Oeffnet einen FileChooser für Textdateien und speichert (falls Datei ausgewaehlt)
     * die Objekte in die Datei.
     */
    @FXML
    private void handleSpeichern() {
        FileChooser fc = getTextFileChooser();
        File file = fc.showOpenDialog(helloApplication.getPrimaryStage());
        if (file != null) {
            helloApplication.getNotizenManager().notizenSpeichern(file.getAbsolutePath());
        }
    }

    /**
     * Handler fuer Clicks auf "Selektiertes Notizen editieren..." bzw. Strg-E. Zeigt das selektierte Webinar im Formular an.
     * Falls kein Webinar selektiert war, wird eine Warnung in einem Popup ausgegeben.
     */
    @FXML
    private void handleEditieren() {
        Notizen selektiertesWebinar = notizenTableView.getSelectionModel().getSelectedItem();
        if (selektiertesWebinar != null) {
            helloApplication.notizenFormularZeigen(selektiertesWebinar);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Sie haben nichts selektiert");
            alert.setTitle("Warning");
            alert.show();
        }
    }

    /**
     * Handler fuer Clicks auf "Neue Notiz..." bzw. Strg-N. Erzeugt ein neues Notiz-Objekt und zeigt es im Formular an.
     * Falls dieses mit "Bestaetigen" verlassen wird, wird das Webinar der Liste hinzugefuegt.
     */
    @FXML
    private void handleNeuesNotizen() {
        Notizen notizen = new Notizen();
        boolean bestaetigt = helloApplication.notizenFormularZeigen(notizen);
        System.out.println(bestaetigt);
        if (bestaetigt) {
            helloApplication.getNotizenManager().webinarEintragen(notizen);
            insertDataInTheDatabase(notizen);
        }

    }

    /**
     * Handler fuer Clicks auf Beenden bzw. Strg-Q. Beendet das Programm.
     */
    @FXML
    private void handleBeenden() {
        System.exit(0);
    }

    /**
     * Loesche die Daten a
     *
     * @param notizen
     */
    public static void deletDataInTheFromTheDatabase(Notizen notizen) {

        try {
            Connection con = SqlHandling.getInstance().connect("root", "Taha@1234", "jdbc:mysql://localhost:3306/Notizen");
            String loeschen = "DELETE FROM Notices WHERE idNotices = " + notizen.getId() + ";";
            Statement statement = con.createStatement();
            statement.execute(loeschen);

            statement.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     *
     * @param notizen
     */
    public static void insertDataInTheDatabase(@NotNull Notizen notizen) {

        try {
            Connection con = SqlHandling.getInstance().connect("jdbc:mysql://localhost:3306/Notizen", "root", "Taha@1234");
            String einfuegen = "INSERT into Notices(idNotices, dataandtime, description, text) " +
                    " values (?, ?, ?, ?)";
            PreparedStatement preparedStmt = con.prepareStatement(einfuegen);
            preparedStmt.setInt(1, notizen.getId());
            preparedStmt.setString (2, notizen.getDatum());
            preparedStmt.setString(3, notizen.getDescription());
            preparedStmt.setString(4, notizen.getText());

            preparedStmt.execute();
           // preparedStmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
