package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class AddressModel {
    private String village;
    private String county;
    private String state;
    private String ISO3166_2_lvl3;
    private String country;
    private String country_code;

    public AddressModel(String village, String county, String state, String ISO3166_2_lvl3, String country, String country_code) {
        this.village = village;
        this.county = county;
        this.state = state;
        this.ISO3166_2_lvl3 = ISO3166_2_lvl3;
        this.country = country;
        this.country_code = country_code;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getISO3166_2_lvl3() {
        return ISO3166_2_lvl3;
    }

    public void setISO3166_2_lvl3(String ISO3166_2_lvl3) {
        this.ISO3166_2_lvl3 = ISO3166_2_lvl3;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
