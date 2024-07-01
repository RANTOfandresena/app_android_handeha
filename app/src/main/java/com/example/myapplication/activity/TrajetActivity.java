package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityTrajetBinding;
import com.example.myapplication.model.TrajetModel;

public class TrajetActivity extends AppCompatActivity {
    private ActivityTrajetBinding binding;
    TrajetModel trajetModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTrajetBinding.inflate(getLayoutInflater());
        binding.btnMoins.setOnClickListener(view->{
            decremente(-1);
        });
        binding.btnPlus.setOnClickListener(View->{
            decremente(1);
        });
        Intent intent=getIntent();
        if(intent!=null){
            trajetModel =(TrajetModel) intent.getSerializableExtra("data");
            if(trajetModel!=null){
                binding.depart.setText(trajetModel.getLieuDepart());
                binding.arrive.setText(trajetModel.getLieuArrive());
                binding.date.setText("Depart : "+trajetModel.getHoraire());
                binding.prix.setText("Prix : "+trajetModel.getPrix());
            }else {
                finish();
            }
        }else{
            finish();
        }
        int[][] places = {
                {0, 0, 2, 2},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {2, 2, 2, 1}
        };
        afficherPlace(places,binding.placee);
        binding.valider.setOnClickListener(view->{
            afficherReservationPlace();
        });
        setContentView(binding.getRoot());
    }

    private void afficherPlace(int[][] places, GridLayout gridLayout) {
        int rowCount = places.length;
        int colCount = 0;
        for (int[] row : places) {
            if (row.length > colCount) {
                colCount = row.length;
            }
        }
        gridLayout.setColumnCount(colCount);
        gridLayout.setRowCount(rowCount);

        // Générer les boutons en fonction du tableau de données
        int placeNumber = 1;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < places[i].length; j++) {
                Button button = new Button(TrajetActivity.this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 150;
                params.height = 150;
                params.setMargins(8, 8, 8, 8);
                params.columnSpec = GridLayout.spec(j);
                params.rowSpec = GridLayout.spec(i);
                button.setLayoutParams(params);
                button.setText(String.valueOf(placeNumber));
                placeNumber++;

                button.setBackgroundResource(R.drawable.button_stylee);

                // Définir la couleur de fond en fonction de la valeur du tableau
                if (places[i][j] == 0) {
                    button.setBackgroundColor(Color.YELLOW);
                } else if (places[i][j] == 1) {
                    button.setBackgroundColor(Color.GRAY);
                } else if (places[i][j] == 2) {
                    button.setBackgroundColor(Color.BLUE);
                }

                // Ajouter le bouton au GridLayout
                gridLayout.addView(button);
            }
        }
    }
    public void decremente(int nombre ){
        String nb=binding.voyageur.getText().toString();
        if(!TextUtils.isEmpty(nb)){
            int resultat=Integer.parseInt(nb)+nombre;
            if(resultat>=1 && resultat<40)
                binding.voyageur.setText(String.valueOf(resultat));
        }
    }
    private void afficherReservationPlace(){
        ConstraintLayout trajet_dialog=findViewById(R.id.trajet_dialog);
        View view= LayoutInflater.from(TrajetActivity.this).inflate(R.layout.trajet_dialog,trajet_dialog);
        Button okvalider=view.findViewById(R.id.valider);

        AlertDialog.Builder builder=new AlertDialog.Builder(TrajetActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        okvalider.findViewById(R.id.valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
                Toast.makeText(TrajetActivity.this, "reservation effectuer", Toast.LENGTH_SHORT).show();
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}