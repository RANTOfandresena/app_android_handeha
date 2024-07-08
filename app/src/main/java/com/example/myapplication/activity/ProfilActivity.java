package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapteur.VehiculeAdapter;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivityProfilBinding;
import com.example.myapplication.model.VehiculeModel;
import com.example.myapplication.outile.PlaceVoiture;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {
    private ActivityProfilBinding binding;
    private View dialogView;
    private Button btn_enregistrer,btn_retour;
    private TextInputEditText place_large,place_long,num_voiture;
    private ImageButton edit_place;
    private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<VehiculeModel> vehiculeList = new ArrayList<>();
        RecyclerView recyclerView=binding.vehiculeliste;


        vehiculeList.add(new VehiculeModel(1,10, "11", "65756757HJ",R.drawable.a3));
        vehiculeList.add(new VehiculeModel(1,40, "11", "6575kjlk6757HJ",R.drawable.a3));
        vehiculeList.add(new VehiculeModel(1,20, "11", "65756jhk757HJ",R.drawable.a3));

        VehiculeAdapter adapter = new VehiculeAdapter( vehiculeList);
        adapter.setOnItemClickListener(new VehiculeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                modifierVehicule(vehiculeList.get(position));
                Toast.makeText(ProfilActivity.this, "Modifier", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemRetourClickListener(new VehiculeAdapter.OnItemRetourClickListener() {
            @Override
            public void onItemRetourClick(int position) {
                Toast.makeText(ProfilActivity.this, "suppr", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(this, String.valueOf(vehiculeList.size()), Toast.LENGTH_SHORT).show();
    }
    private void modifierVehicule(VehiculeModel vehiculeModel) {
        int donneLarge=4;
        int donneLong=7;
        ConstraintLayout trajet_dialog=binding.getRoot().findViewById(R.id.dialog_voiture_place);
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_voiture_place,trajet_dialog);
        btn_enregistrer=dialogView.findViewById(R.id.btn_enregistrer);
        btn_retour=dialogView.findViewById(R.id.btn_retour);
        place_large=dialogView.findViewById(R.id.place_large);
        place_long=dialogView.findViewById(R.id.place_long);
        num_voiture=dialogView.findViewById(R.id.num_voiture);
        edit_place=dialogView.findViewById(R.id.edit_place);
        gridLayout=dialogView.findViewById(R.id.placee);
        place_large.setText(String.valueOf(donneLarge));
        place_long.setText(String.valueOf(donneLong));
        int[][] place= PlaceVoiture.generatePlace(donneLarge,donneLong);
        gridLayout.removeAllViews();
        afficherPlace(place,gridLayout);
        num_voiture.setText(vehiculeModel.getNumeroVehicule());
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog=builder.create();
        edit_place.setOnClickListener(v->{
            changerStructurePlace(place_large,place_long,gridLayout);
        });
        btn_retour.findViewById(R.id.btn_retour).setOnClickListener(v-> {
            Toast.makeText(ProfilActivity.this, "retour", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        btn_enregistrer.findViewById(R.id.btn_enregistrer).setOnClickListener(view-> {
            Toast.makeText(this, "enregistrer", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void changerStructurePlace(TextInputEditText placeLarge, TextInputEditText placeLong, GridLayout gridLayout) {
        int large=Integer.valueOf(placeLarge.getText().toString());
        int longe=Integer.valueOf(placeLong.getText().toString());
        if(nompbrePlaceValide(placeLarge,placeLong,large,longe)){
            int[][] place=PlaceVoiture.generatePlace(large,longe);
            gridLayout.removeAllViews();
            afficherPlace(place,gridLayout);
        }
    }

    private boolean nompbrePlaceValide(TextInputEditText placeLarge, TextInputEditText placeLong,int large,int longe) {
        boolean resultat=true;
        if(large<=2 || large>=6) {
            resultat=false;
            placeLarge.setError("La valeur doit être comprise entre 2 et 6");
        }else{
            placeLarge.setError(null);
        }
        if(longe<=2 || longe>=12) {
            resultat=false;
            placeLong.setError("La valeur doit être comprise entre 2 et 12");
        }else{
            placeLong.setError(null);
        }
        return resultat;
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
        boolean firstButtonMerged = false;  // Ajout d'un indicateur pour suivre si le premier bouton a été fusionné

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < places[i].length; j++) {
                // Saute la création du deuxième bouton s'il a été fusionné avec le premier
                if (firstButtonMerged && i == 0 && j == 1) {
                    continue;
                }

                Button button = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 150;
                params.height = 150;
                params.setMargins(8, 8, 8, 8);

                // Si c'est le premier bouton, fusionnez avec le deuxième
                if (i == 0 && j == 0) {
                    params.columnSpec = GridLayout.spec(j, 2); // Occupe deux colonnes
                    button.setText("chauffeur");
                    firstButtonMerged = true;  // Indique que le premier bouton a été fusionné
                } else {
                    // Ajustez les indices de colonne pour les boutons suivants
                    params.columnSpec = GridLayout.spec(j);
                    button.setText(String.valueOf(placeNumber));
                    placeNumber++;
                }

                params.rowSpec = GridLayout.spec(i);
                button.setLayoutParams(params);
                button.setBackgroundResource(R.drawable.button_stylee);
                gridLayout.addView(button);
            }
        }
    }
}