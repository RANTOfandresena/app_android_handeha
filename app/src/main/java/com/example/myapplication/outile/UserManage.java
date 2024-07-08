package com.example.myapplication.outile;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.model.UtilisateurModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserManage {
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public UserManage(Context context) {
        sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        gson = new GsonBuilder().create();
    }

    // Méthode pour enregistrer l'utilisateur dans SharedPreferences
    public void saveUser(UtilisateurModel utilisateur) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(utilisateur);
        editor.putString("utilisateur_data", json);
        editor.apply();
    }

    // Méthode pour récupérer l'utilisateur à partir de SharedPreferences
    public UtilisateurModel getUser() {
        String json = sharedPreferences.getString("utilisateur_data", null);
        if (json != null) {
            return gson.fromJson(json, UtilisateurModel.class);
        } else {
            return null;
        }
    }
    public void deconnect(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("utilisateur_data",null);
        editor.apply();
    }
}
