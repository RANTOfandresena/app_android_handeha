package com.example.myapplication.model;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.model.convertisseur.IntegerListConverter;
import com.example.myapplication.model.convertisseur.PaiementModelConverter;
import com.example.myapplication.model.convertisseur.TrajetModelConverter;
import com.example.myapplication.model.convertisseur.UtilisateurModelConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@Entity(tableName = "reservation")
@TypeConverters({UtilisateurModelConverter.class, IntegerListConverter.class, TrajetModelConverter.class, PaiementModelConverter.class})
public class ReservationModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
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

    @SerializedName("paiement")
    private PaiementModel paiement;
    public ReservationModel(int idReservation, int idUser, UtilisateurModel utilisateurReserver, TrajetModel trajet, int idTrajet, List<Integer> siegeNumero, PaiementModel paiement) {
        this.idReservation = idReservation;
        this.idUser = idUser;
        this.utilisateurReserver = utilisateurReserver;
        this.trajet = trajet;
        this.idTrajet = idTrajet;
        this.siegeNumero = siegeNumero;
        this.paiement = paiement;
    }
    @Ignore
    public ReservationModel(
            int idUser,
            UtilisateurModel utilisateurReserver,
            TrajetModel trajet,
            int idTrajet,
            List<Integer> siegeNumero,
            PaiementModel paiement
    ) {
        this.idUser = idUser;
        this.utilisateurReserver = utilisateurReserver;
        this.trajet = trajet;
        this.idTrajet = idTrajet;
        this.siegeNumero = siegeNumero;
        this.paiement = paiement;
    }

    @Ignore
    public ReservationModel(int idutilisateur, int idTrajet, List<Integer> placeReserver) {
        this.idUser = idutilisateur;
        this.idTrajet = idTrajet;
        this.siegeNumero = placeReserver;
    }
    @Ignore
    public ReservationModel(int idReservation,int idutilisateur, int idTrajet, List<Integer> placeReserver) {
        this.idReservation=idReservation;
        this.idUser = idutilisateur;
        this.idTrajet = idTrajet;
        this.siegeNumero = placeReserver;
    }

    public PaiementModel getPaiement() {
        return paiement;
    }

    public void setPaiement(PaiementModel paiement) {
        this.paiement = paiement;
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