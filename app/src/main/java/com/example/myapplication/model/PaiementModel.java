package com.example.myapplication.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "paiementModel")
public class PaiementModel {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idPaiement")
    private int idPaiement;
    @SerializedName("idReservation")
    private int idReservation;
    @SerializedName("montant")
    private long montant;
    @SerializedName("numero")
    private String numero;
    @SerializedName("ref")
    private String ref;
    @SerializedName("refapp")
    private String refapp;
    @SerializedName("nomRemetant")
    private String nomRemetant;
    @SerializedName("datePaiement")
    private String datePaiement;

    public PaiementModel(int idPaiement, int idReservation, long montant, String numero, String ref, String refapp, String nomRemetant,String datePaiement) {
        this.idPaiement = idPaiement;
        this.idReservation = idReservation;
        this.montant = montant;
        this.numero = numero;
        this.ref = ref;
        this.refapp = refapp;
        this.nomRemetant = nomRemetant;
        this.datePaiement=datePaiement;
    }
    @Ignore
    public PaiementModel() {
        this.idPaiement = 0;
        this.idReservation = 0;
        this.montant = 0;
        this.numero = "";
        this.ref = "";
        this.refapp = "";
        this.nomRemetant = "";
        this.datePaiement="";
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public long getMontant() {
        return montant;
    }

    public void setMontant(long montant) {
        this.montant = montant;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRefapp() {
        return refapp;
    }

    public void setRefapp(String refapp) {
        this.refapp = refapp;
    }

    public String getNomRemetant() {
        return nomRemetant;
    }

    public void setNomRemetant(String nomRemetant) {
        this.nomRemetant = nomRemetant;
    }
    @Ignore
    public static PaiementModel parseFromString(String input) {
        PaiementModel paiement = new PaiementModel();

        Pattern pattern = Pattern.compile(
                "^(\\d+)/(\\d+) ((\\d{1,3}(?: \\d{3})*)) Ar recu de ([\\w\\s]+) \\((\\d+)\\) le (\\d{2}/\\d{2}/\\d{2}) a (\\d{2}:\\d{2}).* Raison: ([\\w\\s]+).* Ref: (\\d+)$"
        );
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            //paiement.setIdReservation(Integer.parseInt(matcher.group(1))); // idReservation
            String montantStr = matcher.group(3).replace(" ", ""); // Remove spaces from montant
            paiement.setMontant(Integer.parseInt(montantStr)); // montant
            paiement.setNomRemetant(matcher.group(5)); // nomRemetant
            paiement.setNumero(matcher.group(6)); // numero
            paiement.setDatePaiement(matcher.group(7) + " " + matcher.group(8)); // datePaiement
            paiement.setRef(matcher.group(10)); // ref
            paiement.setRefapp(matcher.group(9)); // raison
            return paiement;
        }else{
            return null;
        }


    }
}
