package com.example.myapplication.apiClass;

import com.google.gson.annotations.SerializedName;

public class NominatimResponse {
    @SerializedName("display_name")
    private String displayName;


    public String getDisplayName() {
        return displayName;
    }
}
