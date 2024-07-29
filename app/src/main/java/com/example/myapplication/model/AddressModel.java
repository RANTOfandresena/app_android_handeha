package com.example.myapplication.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("city")
    private String city;

    @SerializedName("town")
    private String town;

    @SerializedName("village")
    private String village;

    @SerializedName("hamlet")
    private String hamlet;

    @SerializedName("county")
    private String county;

    @SerializedName("state")
    private String state;

    @SerializedName("province")
    private String province;

    @SerializedName("region")
    private String region;

    @SerializedName("country")
    private String country;

    @SerializedName("suburb")
    private String suburb;

    @SerializedName("locality")
    private String locality;

    @SerializedName("postcode")
    private String postcode;

    @SerializedName("district")
    private String district;

    @SerializedName("neighborhood")
    private String neighborhood;
    public String getFirstNonNullAttribute() {
        if (city != null) return city;       // Priorité : Ville
        if (town != null) return town;       // Petite ville ou bourg
        if (village != null) return village; // Village
        if (hamlet != null) return hamlet;   // Hameau
        if (suburb != null) return suburb;   // Quartier ou banlieue
        if (district != null) return district; // District
        if (county != null) return county;   // Comté
        if (region != null) return region;   // Région
        if (state != null) return state;     // État
        if (province != null) return province; // Province
        if (country != null) return country; // Pays
        if (postcode != null) return postcode; // Code postal
        return null; // Si tous les attributs sont null
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHamlet() {
        return hamlet;
    }

    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", hamlet='" + hamlet + '\'' +
                ", county='" + county + '\'' +
                ", state='" + state + '\'' +
                ", province='" + province + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", suburb='" + suburb + '\'' +
                ", locality='" + locality + '\'' +
                ", postcode='" + postcode + '\'' +
                ", district='" + district + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                '}';
    }
}
