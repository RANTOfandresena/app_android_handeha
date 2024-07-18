package com.example.myapplication.model.convertisseur;

import androidx.room.TypeConverter;

import com.example.myapplication.model.UtilisateurModel;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class UtilisateurModelConverter {
    @TypeConverter
    public static String fromUtilisateurModel(UtilisateurModel utilisateurModel) {
        if (utilisateurModel == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(utilisateurModel);
    }

    @TypeConverter
    public static UtilisateurModel toUtilisateurModel(String utilisateurModelString) {
        if (utilisateurModelString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<UtilisateurModel>() {}.getType();
        return gson.fromJson(utilisateurModelString, type);
    }
}
