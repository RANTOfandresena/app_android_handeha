package com.example.myapplication.model;

public class VehiculeModel {
    private int id,capacite,image;

    private String numeroVehicule,position;


    public VehiculeModel(int id, int capacite, String position, String numeroVehicule, int image) {
        this.id = id;
        this.capacite = capacite;
        this.position = position;
        this.numeroVehicule = numeroVehicule;
        this.image=image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
