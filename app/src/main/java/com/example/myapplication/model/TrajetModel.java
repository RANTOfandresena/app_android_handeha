package com.example.myapplication.model;

import androidx.room.Embedded;

import com.example.myapplication.outile.DateChange;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TrajetModel implements Serializable {
    @SerializedName("idTrajet")
    private int idTrajet;
    @SerializedName("lieuDepart")
    private String lieuDepart;
    @SerializedName("lieuArrive")
    private String lieuArrive;
    @SerializedName("horaire")
    private String horaire;
    @SerializedName("prix")
    private String prix;
    @SerializedName("attribute")
    private String attribute;
    @SerializedName("idVehicule")
    private int idVehicule;
    @Embedded
    private VehiculeModel vehicule;
    @SerializedName("idUser")
    private int idUser;
    @SerializedName("chauffeur")
    private UtilisateurModel user;

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
    public TrajetModel(String lieuDepart, String lieuArrive, String horaire, String prix, int idVehicule, int idUser) {
        this.lieuDepart = lieuDepart;
        this.lieuArrive = lieuArrive;
        this.horaire = horaire;
        this.prix = prix;
        this.attribute="";
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
    public VehiculeModel getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeModel vehicule) {
        this.vehicule = vehicule;
    }

    public UtilisateurModel getUser() {
        return user;
    }

    public void setUser(UtilisateurModel utilisateurModel) {
        this.user = utilisateurModel;
    }
}
