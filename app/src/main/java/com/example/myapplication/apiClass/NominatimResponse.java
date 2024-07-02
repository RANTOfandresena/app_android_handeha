package com.example.myapplication.apiClass;

import com.google.gson.annotations.SerializedName;

public class NominatimResponse {
    @SerializedName("display_name")
    private String displayName;

    // Ajoute d'autres champs si nécessaire

    public String getDisplayName() {
        return displayName;
    }
}
