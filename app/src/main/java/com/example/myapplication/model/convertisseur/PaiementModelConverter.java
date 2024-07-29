package com.example.myapplication.model.convertisseur;

import androidx.room.TypeConverter;

import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.TrajetModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PaiementModelConverter {
    @TypeConverter
    public static String fromPaiementModel(PaiementModel paiement) {
        if (paiement == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(paiement);
    }

    @TypeConverter
    public static PaiementModel toPaiementModel(String paiementString) {
        if (paiementString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<PaiementModel>() {}.getType();
        return gson.fromJson(paiementString, type);
    }
}
