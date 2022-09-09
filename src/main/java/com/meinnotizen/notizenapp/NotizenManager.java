package com.meinnotizen.notizenapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class NotizenManager {

    //ArrayList zum Verwalten der Notizen, zur Verwendung mit JavaFX als ObservableList
    private ObservableList<Notizen> notizenData = FXCollections.observableArrayList();

    public ObservableList<Notizen> getNotizenData() {
        return notizenData;
    }
    /** Nimmt ein Webinar-Objekt entgegen und traegt es in die interne Liste ein.
     * @param webinar Das einzutragende Webinar.
     */
    public void webinarEintragen(Notizen webinar) {
        notizenData.add(webinar);
    }
     boolean notizenLaden(String dateiname){
        Notizen notizen ;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(dateiname));
            while (br.ready()){
                notizen = new Notizen();
                notizen.setId(Integer.parseInt(br.readLine()));
                notizen.setDescription(br.readLine());
                notizen.setText(br.readLine());
                notizen.setDatum(br.readLine());
                notizenData.add(notizen);
            }
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
            }catch (IOException e){
                    System.out.println(e.getMessage());
                }
        }
        return false;
    }
    boolean notizenSpeichern(String dateiname){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(dateiname));
            for (Notizen notizen : notizenData){
                if (notizen != null){
                    bw.write(notizen.getId());
                    bw.write(notizen.getDescription());
                    bw.write(notizen.getText());
                    bw.write(notizen.getDatum());
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
}
