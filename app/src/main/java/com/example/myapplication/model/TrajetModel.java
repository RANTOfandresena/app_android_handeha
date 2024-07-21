package com.example.myapplication.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.outile.DateChange;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "trajet")
public class TrajetModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
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
    //@SerializedName("attribute")
    //private String attribute;
    @SerializedName("idVehicule")
    private int idVehicule;
    @Ignore
    @Embedded
    private VehiculeModel vehicule;
    @SerializedName("idUser")
    private int idUser;
    @Ignore
    @SerializedName("chauffeur")
    private UtilisateurModel user;
    @SerializedName("siegeReserver")
    private List<Integer> siegeReserver;
    @Ignore
    public TrajetModel(int idTrajet, String lieuDepart, String lieuArrive, String horaire, String prix,  int idVehicule, VehiculeModel vehicule, int idUser, UtilisateurModel user,List<Integer> siegeReserver) {
        this.idTrajet = idTrajet;
        this.lieuDepart = lieuDepart;
        this.lieuArrive = lieuArrive;
        this.horaire = horaire;
        this.prix = prix;
        this.idVehicule = idVehicule;
        this.vehicule = vehicule;
        this.idUser = idUser;
        this.user = user;
        this.siegeReserver=siegeReserver;
    }

    public TrajetModel(int idTrajet, String lieuDepart, String lieuArrive, String horaire, String prix, int idVehicule, int idUser,List<Integer> siegeReserver) {
        this.idTrajet = idTrajet;
        this.lieuDepart = lieuDepart;
        this.lieuArrive = lieuArrive;
        this.horaire = horaire;
        this.prix = prix;
        this.idVehicule = idVehicule;
        this.idUser = idUser;
        this.siegeReserver=siegeReserver;
    }
    @Ignore
    public TrajetModel(String lieuDepart, String lieuArrive, String horaire, String prix, int idVehicule, int idUser) {
        this.lieuDepart = lieuDepart;
        this.lieuArrive = lieuArrive;
        this.horaire = horaire;
        this.prix = prix;
        this.idVehicule = idVehicule;
        this.idUser = idUser;
    }

    public List<Integer> getSiegeReserver() {
        return siegeReserver;
    }

    public void setSiegeReserver(List<Integer> siegeReserver) {
        this.siegeReserver = siegeReserver;
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
