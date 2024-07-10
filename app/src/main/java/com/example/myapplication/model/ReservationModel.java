package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationModel {
    @SerializedName("idReservation")
    private int idReservation;
    @SerializedName("idUser")
    private int idUser;
    @SerializedName("idTrajet")
    private int idTrajet;
    @SerializedName("siegeNumero")
    private List<Integer> siegeNumero;
    public ReservationModel(int idUser, int idTrajet, List<Integer> siegeNumero) {
        this.idUser = idUser;
        this.idTrajet = idTrajet;
        this.siegeNumero = siegeNumero;
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

    public int getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public List<Integer> getSiegeNumero() {
        return siegeNumero;
    }

    public void setSiegeNumero(List<Integer> siegeNumero) {
        this.siegeNumero = siegeNumero;
    }
}
