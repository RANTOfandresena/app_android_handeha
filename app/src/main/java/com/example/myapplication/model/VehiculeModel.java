package com.example.myapplication.model;

import android.net.Uri;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.net.URI;

public class VehiculeModel {

    @SerializedName("idVehicule")
    private int idVehicule;
    @SerializedName("capacite")
    private int capacite;
    @SerializedName("photo")
    private String photo;

    @SerializedName("numeroVechicule")
    private String numeroVehicule;
    @SerializedName("positionActuelle")
    private String position;
    @SerializedName("nb_colonne")
    private String nb_colonne;//nombre de place
    @SerializedName("nb_rangee")
    private String nb_rangee;//banquette
//

    @SerializedName("idUser")
    private String idUser;



    @Embedded
    private UtilisateurModel user;
    
    public VehiculeModel(int id, String position, int capacite, String numeroVehicule, String photo, String nbColonne, String nbRangee,String idUser) {
        this.idVehicule = id;
        this.capacite = capacite;
        this.position = position;
        this.numeroVehicule = numeroVehicule;
        this.photo= photo;
        this.nb_colonne = nbColonne;
        this.nb_rangee = nbRangee;
        this.idUser=idUser;
    }
    public VehiculeModel(String numeroVehicule,String nbColonne, String nbRangee,String idUser ) {
        this.numeroVehicule = numeroVehicule;
        this.nb_colonne = nbColonne;
        this.nb_rangee = nbRangee;
        this.idUser=idUser;
    }
    @Override
    public String toString() {
        return "voiture numero :"+ numeroVehicule + "\nnombre de place " + capacite;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getNumeroVehicule() {
        return numeroVehicule;
    }

    public void setNumeroVehicule(String numeroVehicule) {
        this.numeroVehicule = numeroVehicule;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public String getNb_colonne() {
        return nb_colonne;
    }

    public void setNb_colonne(String nb_colonne) {
        this.nb_colonne = nb_colonne;
    }

    public String getNb_rangee() {
        return nb_rangee;
    }

    public void setNb_rangee(String nb_rangee) {
        this.nb_rangee = nb_rangee;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    public UtilisateurModel getUser() {
        return user;
    }

    public void setUser(UtilisateurModel user) {
        this.user = user;
    }

}
