package com.example.myapplication.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationModel {
    @SerializedName("idReservation")
    private int idReservation;

    @SerializedName("idUser")
    private int idUser;

    @SerializedName("utlitisateurResever")
    private UtilisateurModel utilisateurReserver;

    @SerializedName("trajet")
    private TrajetModel trajet;

    @SerializedName("idTrajet")
    private int idTrajet;

    @SerializedName("siegeNumero")
    private List<Integer> siegeNumero;

    public ReservationModel(int idReservation, int idUser, UtilisateurModel utilisateurReserver, TrajetModel trajet, int idTrajet, List<Integer> siegeNumero) {
        this.idReservation = idReservation;
        this.idUser = idUser;
        this.utilisateurReserver = utilisateurReserver;
        this.trajet = trajet;
        this.idTrajet = idTrajet;
        this.siegeNumero = siegeNumero;
    }

    public ReservationModel(int idutilisateur, int idTrajet, List<Integer> placeReserver) {
        this.idUser = idutilisateur;
        this.idTrajet = idTrajet;
        this.siegeNumero = placeReserver;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public TrajetModel getTrajet() {
        return trajet;
    }

    public void setTrajet(TrajetModel trajet) {
        this.trajet = trajet;
    }

    public List<Integer> getSiegeNumero() {
        return siegeNumero;
    }

    public void setSiegeNumero(List<Integer> siegeNumero) {
        this.siegeNumero = siegeNumero;
    }

    public int getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public UtilisateurModel getUtilisateurReserver() {
        return utilisateurReserver;
    }

    public void setUtilisateurReserver(UtilisateurModel utilisateurReserver) {
        this.utilisateurReserver = utilisateurReserver;
    }
}