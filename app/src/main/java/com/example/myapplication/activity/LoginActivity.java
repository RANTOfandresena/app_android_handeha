package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

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

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.outile.UserManage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    ApiService apiService;
    private EditText num, mdp;
    SharedPreferences sharedPreferences;
    private String authToken;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Créez une instance de Retrofit sans token d'authentification pour la connexion
        apiService = RetrofitClient.getClient(URL_SERVER, null).create(ApiService.class);
        gson = new GsonBuilder().create();
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
            apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
            Call<LoginResponse> postCall = apiService.login(new LoginRequest(numStr,mdpStr));
            Toast.makeText(this, "valide", Toast.LENGTH_SHORT).show();
            postCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Toast.makeText(LoginActivity.this, "ok ok", Toast.LENGTH_SHORT).show();
                    if(response.isSuccessful()){
                        LoginResponse loginResponse=response.body();
                        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        sauveAuthToken(loginResponse.getKey());
                        sauveUtilisateur(numStr,loginResponse.getKey());
                    }else
                        Toast.makeText(LoginActivity.this, "numero ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "echec de connexion", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
    private void sauveAuthToken(String apiKey){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("apikey",apiKey);
        editor.apply();
    }
    private void sauveUtilisateur(String numero,String key) {
        apiService = RetrofitClient.getClient(URL_SERVER, key).create(ApiService.class);
        Call<List<UtilisateurModel>> getCall = apiService.getUtilisateur(numero);
        getCall.enqueue(new Callback<List<UtilisateurModel>>() {
            @Override
            public void onResponse(Call<List<UtilisateurModel>> call, Response<List<UtilisateurModel>> response) {
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                UtilisateurModel user=response.body().get(0);
                UserManage userManage=new UserManage(LoginActivity.this);
                userManage.saveUser(user);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            @Override
            public void onFailure(Call<List<UtilisateurModel>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });

    }
}