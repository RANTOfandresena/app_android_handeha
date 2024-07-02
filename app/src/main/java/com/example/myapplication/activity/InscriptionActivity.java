package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.databinding.ActivityInscriptionBinding;
import com.example.myapplication.model.UtilisateurModel;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;
    ApiService apiService;
    private EditText nom,prenom,num,cin,pseudo,mdp1,mdp2;
    private MaterialButton btnInscrire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnRatour.setOnClickListener(view->{
            onBackPressed();
        });

        nom=binding.nom;
        prenom=binding.prenom;
        num=binding.num;
        cin=binding.cin;
        pseudo=binding.pseudo;
        mdp1=binding.mdp1;
        mdp2=binding.mdp2;

        binding.btnInscrire.setOnClickListener(view->{

            String nomStr=nom.getText().toString();
            String prenomStr=prenom.getText().toString();
            String numStr=num.getText().toString();
            String cinStr=cin.getText().toString();
            String pseudoStr=pseudo.getText().toString();
            String mdp1Str=mdp1.getText().toString();
            String mdp2Str=mdp2.getText().toString();

            UtilisateurModel utilisateurModel=new UtilisateurModel(nomStr,prenomStr,numStr,cinStr,pseudoStr,mdp1Str);

            Toast.makeText(this, utilisateurModel.getPassword(), Toast.LENGTH_SHORT).show();
            envoyeInscrit(utilisateurModel);
        });
    }
    private void envoyeInscrit(UtilisateurModel utilisateurModel){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<UtilisateurModel> postCall = apiService.setUtilisateur(utilisateurModel);
        postCall.enqueue(new Callback<UtilisateurModel>() {
            @Override
            public void onResponse(Call<UtilisateurModel> call, Response<UtilisateurModel> response) {
                Toast.makeText(InscriptionActivity.this, "Inscription fait avec success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InscriptionActivity.this,LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<UtilisateurModel> call, Throwable t) {
                Toast.makeText(InscriptionActivity.this, "Echec d'envoi ,verifier votre connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}