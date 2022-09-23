package com.meinnotizen.notizenapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

public class HelloApplication extends Application {


    private Stage primaryStage;
   // private ObservableList<Notizen> notizenData = FXCollections.observableArrayList();
    private NotizenManager notizenManager = new NotizenManager();


    public HelloApplication(){
        try {
            Connection con = SqlHandling.getInstance().connect("jdbc:mysql://localhost:3306/Notizen", "root", "Taha@1234");
            ResultSet ergebnis = SqlHandling.getInstance().query("SELECT idNotices, dataandtime, description, text FROM Notices");

            while (ergebnis.next()){
                int id = ergebnis.getInt("idNotices");
                String descr = ergebnis.getString("description");
                String text = ergebnis.getString("text");
                String datum = ergebnis.getString("dataandtime");
                notizenManager.getNotizenData().add(new Notizen(id, descr, text, datum));
            }

            ergebnis.close();
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
            loader.setLocation(HelloApplication.class.getResource("NotizenWindow.fxml"));
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
    /** Oeffnet das Formular zum Editieren eines (ggf. neuen, d. h. leeren) Webinar-Objekts.
	 * Gibt zurueck, ob die Bearbeitung bestaetigt oder abgebrochen wurde.
     *
     * @param notizen Das zu editierende Webinar-Objekt
	 * @return true falls "Bestaetigen" geklickt wurde, sonst false.
     */
    public boolean notizenFormularZeigen(Notizen notizen) {
        //View laden
        // ### Beginn VARIANTE 1: View aus fxml ###
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("NotizenFormular.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();

            //Controller (steht in fxml) holen und Webinar-Objekt uebergeben
            NotizenFormularController controller = loader.getController();
            // ### Ende VARIANTE 1: View aus fxml ###

            // ### Beginn VARIANTE 2: View aus Java ###
	        /*
			WebinarFormularController controller = new WebinarFormularController();
			AnchorPane pane = (AnchorPane) controller.getPane();
			controller.initialize();
			*/
            // ### Ende VARIANTE 2: View aus Java ###

            //(Secondary) Stage fuer das Popup-Formular erzeugen und mit der Primary Stage verknuepfen
            Stage popupStage = new Stage();
            popupStage.setTitle("Bearbeite Webinardaten");
            popupStage.initModality(Modality.WINDOW_MODAL);	//Popup muss geschlossen werden, bevor es im Hauptfenster weitergehen kann
            popupStage.initOwner(primaryStage);
            popupStage.setResizable(false);					//fixe Fenstergroesse
            Scene scene = new Scene(pane);
            popupStage.setScene(scene);

            controller.setNotizen(notizen);
            //ausserdem Popup Stage uebergeben, damit Controller diese z. B. schliessen oder weitere Fenster oeffnen kann
            controller.setPopupStage(popupStage);

            //Popup anzeigen und warten, bis der User es schliesst
            popupStage.showAndWait();

            //zurueckgeben, ob bestaetigt oder abgebrochen wurde
            return controller.istBestaetigt();
            // ### Beginn catch-Block VARIANTE 1: View aus fxml ###
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // ### Ende catch-Block VARIANTE 1: View aus fxml ###

    }

    public static void main(String[] args) {
        launch();
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public NotizenManager getNotizenManager(){
        return notizenManager;
    }

}