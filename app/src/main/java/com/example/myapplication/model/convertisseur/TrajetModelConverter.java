package com.example.myapplication.model.convertisseur;

import androidx.room.TypeConverter;

import com.example.myapplication.model.TrajetModel;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class TrajetModelConverter {
    @TypeConverter
    public static String fromTrajetModel(TrajetModel trajetModel) {
        if (trajetModel == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(trajetModel);
    }

    @TypeConverter
    public static TrajetModel toTrajetModel(String trajetModelString) {
        if (trajetModelString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<TrajetModel>() {}.getType();
        return gson.fromJson(trajetModelString, type);
    }
}
