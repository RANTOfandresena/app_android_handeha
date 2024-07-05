package com.example.myapplication.model;

public class PositionLieuModel {
    private String id;
    private double lat;
    private double lon;
    private String name;

    public PositionLieuModel(String id, double lat, double lon, String name) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    // Getters
    public String getId() { return id; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getName() { return name; }
}
