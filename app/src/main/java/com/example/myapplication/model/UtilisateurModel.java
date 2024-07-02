package com.example.myapplication.model;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class UtilisateurModel {
    private String first_name,last_name,numero,cin,username,password;
    public UtilisateurModel(String first_name, String last_name, String numero, String cin, String username, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.numero = numero;
        this.cin = cin;
        this.username = username;
        this.password  = password ;
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
}
