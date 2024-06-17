package com.example.myapplication.model;

public class UtilisateurModel {
    private String nom,prenom,numero,cin,pseudo,mdp;

    public UtilisateurModel(String nom, String prenom, String numero, String cin, String pseudo, String mdp) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.cin = cin;
        this.pseudo = pseudo;
        this.mdp = mdp;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }


}
