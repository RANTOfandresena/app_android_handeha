package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.databinding.ActivityInscriptionBinding;
import com.example.myapplication.model.UtilisateurModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;
    private EditText nom,prenom,num,cin,pseudo,mdp1,mdp2;
    private MaterialButton btnInscrire;
    private FirebaseDatabase database;
    private DatabaseReference reference;
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
            database=FirebaseDatabase.getInstance();
            reference=database.getReference("utilisateur");
            String nomStr=nom.getText().toString();
            String prenomStr=prenom.getText().toString();
            String numStr=num.getText().toString();
            String cinStr=cin.getText().toString();
            String pseudoStr=pseudo.getText().toString();
            String mdp1Str=mdp1.getText().toString();
            String mdp2Str=mdp2.getText().toString();

            UtilisateurModel utilisateurModel=new UtilisateurModel(nomStr,prenomStr,numStr,cinStr,pseudoStr,mdp1Str);
            reference.child(nomStr).setValue(utilisateurModel);

            Toast.makeText(this, "Inscription avec succes", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(InscriptionActivity.this,LoginActivity.class));
            finish();
        });



    }
}