package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.model.UtilisateurModel;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EditText num, mdp;
    SharedPreferences sharedPreferences;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        num = binding.num;
        mdp = binding.mdp;
        binding.inscrire.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, InscriptionActivity.class));
        });
        binding.btn.setOnClickListener(view -> {
            validationUtilisateur();
        });
    }

    public Boolean validationNum(){
        String numStr=num.getText().toString();
        if (numStr.isEmpty()){
            num.setError("Le numero ne doit pas etre vide");
            return false;
        }
        num.setError(null);
        return true;
    }

    public Boolean validationMdp(){
        String mdpStr=mdp.getText().toString();
        if (mdpStr.isEmpty()){
            mdp.setError("Le mot de passe ne doit pas etre vide");
            return false;
        }
        mdp.setError(null);
        return true;
    }

    public void validationUtilisateur(){
        if(validationNum() && validationMdp()){
            String mdpStr=mdp.getText().toString().trim();
            String numStr=num.getText().toString().trim();
            if(authentification(mdpStr,numStr)){
                SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private boolean authentification(String mdpStr, String numStr) {
        return true;
    }
    private void sauveAPIkey(String apiKey){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("apikey",apiKey);
        editor.apply();
    }
    private void sauveUtilisateur(UtilisateurModel user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(user);
        editor.putString("UtilisateurModel", json);
        editor.apply();
    }
}