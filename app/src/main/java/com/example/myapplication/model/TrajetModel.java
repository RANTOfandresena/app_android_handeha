package com.example.myapplication.model;

import java.io.Serializable;

public class TrajetModel implements Serializable {
    private int idTrajet;
    private String lieuDepart;
    private String lieuArrive;
    private String horaire;
    private String prix;
    private String attribute;
    private int idVehicule;
    private int idUser;

    public TrajetModel(int idTrajet, String lieuDepart, String lieuArrive, String horaire, String prix, String attribute, int idVehicule, int idUser) {
        this.idTrajet = idTrajet;
        this.lieuDepart = lieuDepart;
        this.lieuArrive = lieuArrive;
        this.horaire = horaire;
        this.prix = prix;
        this.attribute = attribute;
        this.idVehicule = idVehicule;
        this.idUser = idUser;
    }



    public int getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuArrive() {
        return lieuArrive;
    }

    public void setLieuArrive(String lieuArrive) {
        this.lieuArrive = lieuArrive;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
