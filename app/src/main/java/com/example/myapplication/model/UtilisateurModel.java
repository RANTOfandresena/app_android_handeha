package com.example.myapplication.model;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

public class UtilisateurModel implements Serializable {


    private String id;
    private String first_name;
    private String last_name;
    private String numero;
    private String cin;
    private String username;
    private String password;

    private boolean est_conducteur;

    public UtilisateurModel(String first_name, String last_name, String numero, String cin, String username, String password,boolean est_conducteur) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.numero = numero;
        this.cin = cin;
        this.username = username;
        this.password=password;
        this.est_conducteur=est_conducteur;
    }

    public UtilisateurModel(String first_name, String last_name, String numero) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.numero = numero;
    }

    public UtilisateurModel(String id, String first_name, String last_name, String numero, String cin, String username, String mdp1, boolean est_conducteur) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.numero = numero;
        this.cin = cin;
        this.username = username;
        this.password  = mdp1 ;
        this.id=id;
        this.est_conducteur=est_conducteur;
    }

    protected UtilisateurModel(Parcel in) {
        id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        numero = in.readString();
        cin = in.readString();
        username = in.readString();
        password = in.readString();
        est_conducteur = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEst_conducteur() {
        return est_conducteur;
    }

    public void setEst_conducteur(boolean est_conducteur) {
        this.est_conducteur = est_conducteur;
    }

}
