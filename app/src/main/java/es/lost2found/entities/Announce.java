package es.lost2found.entities;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.lost2found.R;

public class Announce implements Serializable {

    public String announceType;
    //public Date announceDateText;
    public String announceDateText;
    //public Date currentTime;
    public String currentTime;
    //public Date announceHourText;
    public String announceHourText;
    public String announceCategorie;
    public String brand;
    public String model;
    public String color;

    public Announce(String announceType, String currentTime, String announceDateText, String announceHourText, String model, String brand, String color, String announceCategorie) {
        this.announceType = announceType;
        this.announceDateText = announceDateText;
        this.currentTime = currentTime;
        this.announceHourText = announceHourText;
        this.announceCategorie = announceCategorie;
        this.brand = brand;
        this.model = model;
        this.color = color;
    }

    public String getAnnounceType() {
        return this.announceType;
    }

    public String getAnnounceDateText() {
        return this.announceDateText;
    }

    public String getAnnounceHourText() {
        return this.announceHourText;
    }

    public String getAnnounceCategorie() {
        return this.announceCategorie;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getColor() {
        return this.color;
    }

    // Faltan los getters y setters
    /*
    public List<Announce> fill_with_data(List<Announce> announce) {

        announce.add(new Announce("Anuncio1", "Descripcion1", R.drawable.ic_phone_android));
        announce.add(new Announce("Anuncio2", "Descripcion2", R.drawable.ic_phone_android));
        announce.add(new Announce("Anuncio3", "Descripcion3", R.drawable.ic_phone_android));
        announce.add(new Announce("Anuncio4", "Descripcion4", R.drawable.ic_phone_android));
        announce.add(new Announce("Anuncio5", "Descripcion5", R.drawable.ic_phone_android));
        announce.add(new Announce("Anuncio6", "Descripcion6", R.drawable.ic_phone_android));

        return announce;
    }*/
}
