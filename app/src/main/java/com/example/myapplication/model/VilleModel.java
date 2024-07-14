package com.example.myapplication.model;

public class VilleModel {
    private String nomVille,lat,lon,id;

    public VilleModel(String id, String lat, String lon,String nomVille) {
        this.nomVille = nomVille;
        this.lat = lat;
        this.lon = lon;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
