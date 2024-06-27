package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EditText num,mdp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        num=binding.num;
        mdp=binding.mdp;
        binding.inscrire.setOnClickListener(view->{
            startActivity(new Intent(LoginActivity.this,InscriptionActivity.class));
        });
        binding.btn.setOnClickListener(view->{
            if(validationMdp() && validationNum()){
                validationUtilisateur();
            }
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
        String mdpStr=mdp.getText().toString().trim();
        String numStr=num.getText().toString().trim();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("utilisateur");
        Query validationUtilisateurBdd=reference.orderByChild("numero").equalTo(numStr);
        validationUtilisateurBdd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(LoginActivity.this, "gg3", Toast.LENGTH_SHORT).show();
                if(snapshot.exists()){
                    num.setError(null);
                    String mdpBdd=snapshot.child(numStr).child("mdp").getValue(String.class);

                    if(mdpBdd.equals(mdpStr)){
                        num.setError(null);
                        String numero=snapshot.child(numStr).child("numero").getValue(String.class);
                        String prenom=snapshot.child(numStr).child("prenom").getValue(String.class);
                        String nom=snapshot.child(numStr).child("nom").getValue(String.class);
                        String cin=snapshot.child(numStr).child("cin").getValue(String.class);
                        String pseudo=snapshot.child(numStr).child("pseudo").getValue(String.class);

                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("numero",numero);
                        intent.putExtra("nom",nom);
                        intent.putExtra("prenom",prenom);
                        intent.putExtra("cin",cin);
                        intent.putExtra("pseudo",pseudo);
                        startActivity(intent);
                        finish();
                    }else{
                        mdp.setError("Mot de pass incorect");
                        mdp.requestFocus();
                    }
                }else{
                    num.setError("ce numero n'est pas encore inscrit ou n'est pas approuvé par l'administrateur");
                    num.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "erreur de connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}