package com.meinnotizen.notizenapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NotizenFormularController {
    //die folgenden Annotationen dienen der Verknuepfung der Attribute mit den Controls in der fxml-Datei
    @FXML
    private TextField idField;
    @FXML
    private TextField descriptionFild;
    @FXML
    private TextField textField;
    @FXML
    private TextField datumField;

    private Stage popupStage;
    private Notizen notizen;
    private boolean bestaetigt = false;

    /**
     * Initialisiert das Controller-Objekt. Wird automatisch aufgerufen nach dem Laden der fxml-Datei.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Setzt die (secondary) Stage fuer dieses Popup.
     *
     * @param popupStage
     */
    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }
    /**
     * Setzt das Webinar-Objekt, das in diesem Formular bearbeitet werden soll.
     *
     * @param notizen
     */
    public void setNotizen(Notizen notizen) {
        this.notizen = notizen;

        //aktuelle Werte in Formularfelder uebernehmen (leere Felder bei neuem Webinar)
        idField.setText(notizen.getId() > 0 ? Integer.toString(notizen.getId()) : "");
        descriptionFild.setText(notizen.getDescription());
        textField.setText(notizen.getText());
        datumField.setText(notizen.getDatum());
    }
    /**
     * Liefert zurueck, ob das Formular mit "Bestaetigen" oder "Abbrechen" geschlossen wurde.
     *
     * @return true, falls bestaetigt, false sonst.
     */
    public boolean istBestaetigt() {
        return bestaetigt;
    }

    /**
     * Handler fuer Clicks auf "Bestaetigen". Die Eingaben werden geprueft und - falls gueltig - in das Webinar-Objekt eingetragen.
     * Anschliessend wird das Popup geschlossen.
     */
    @FXML
    private void handleBestaetigen() {
        if (eingabenGueltig()) {
            notizen.setId(Integer.parseInt(idField.getText()));						//String -> int
            notizen.setDescription(descriptionFild.getText());
            notizen.setText(textField.getText());
            notizen.setDatum(datumField.getText());
            bestaetigt = true;
            popupStage.close();		//Fenster schliessen
        }
    }
    /**
     * Prueft die Eintraege in den Formularfeldern auf Gueltigkeit.
     *
     * @return true falls gueltig, false sonst.
     */
    private boolean eingabenGueltig() {
        String str = "";
        return true; //Dummy
    }

    /**
     * Handler fuer Clicks auf "Abbrechen". Popup wird ohne Datenuebernahme geschlossen.
     */
    @FXML
    private void handleAbbrechen() {
        popupStage.close();
    }

}
