package com.meinnotizen.notizenapp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 */
public class Notizen {
    protected IntegerProperty id;

    protected StringProperty description;
    protected StringProperty text;
    protected StringProperty datum;

    public Notizen(){
        this(0,"","", "");
    }

    public Notizen(Integer id , String description, String text, String datum) {
        this.id = new SimpleIntegerProperty(id);
        this.text =  new SimpleStringProperty(text);
        this.description =  new SimpleStringProperty(description);
        this.datum =  new SimpleStringProperty(datum);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }



    public String getDatum() {
        return datum.get();
    }

    public void setDatum(String datum) {
        this.datum.set(datum);
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty datumProperty() {
        return datum;
    }

    public String getDescription() {
        return description.get();
    }

    @Override
    public String toString() {
        return "Notizen{" +
                "id=" + id +
                ", description=" + description +
                ", text=" + text +
                ", datum=" + datum +
                '}';
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty textProperty() {
        return text;
    }
}
