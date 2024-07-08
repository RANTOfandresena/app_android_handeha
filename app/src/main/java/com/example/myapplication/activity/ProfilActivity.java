package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapteur.VehiculeAdapter;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.databinding.ActivityProfilBinding;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.VehiculeModel;
import com.example.myapplication.outile.PlaceVoiture;
import com.example.myapplication.outile.UserManage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {
    private ActivityProfilBinding binding;
    private VehiculeAdapter adapter;
    private View dialogView;
    private Button btn_enregistrer,btn_retour;
    private TextInputEditText place_large,place_long,num_voiture;
    private ImageButton edit_place;
    private GridLayout gridLayout;
    private ApiService apiService;
    private List<VehiculeModel> vehiculeList;
    private Button ajoutVoiture;
    private UserManage userManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManage=new UserManage(this);
        binding.ajoutVoiture.setOnClickListener(v->{
            VehiculeModel vehiculeModel=new VehiculeModel("","4","6",userManage.getUser().getId());
            modifierVehicule(vehiculeModel,true,-1);
        });
        if(userManage.getUser().isEst_conducteur()){
            getVoitureApi();
        }
    }
    private void afficherVoiture() {
        RecyclerView recyclerView=binding.vehiculeliste;
        adapter = new VehiculeAdapter( vehiculeList);
        adapter.setOnItemClickListener(new VehiculeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                modifierVehicule(vehiculeList.get(position),false,position);
                Toast.makeText(ProfilActivity.this, "Modifier", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemRetourClickListener(new VehiculeAdapter.OnItemRetourClickListener() {
            @Override
            public void onItemRetourClick(int position) {
                confirmationSuppr(position);
                Toast.makeText(ProfilActivity.this, "suppr", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(this, String.valueOf(vehiculeList.size()), Toast.LENGTH_SHORT).show();
    }

    private void modifierVehicule(VehiculeModel vehiculeModel, boolean isajout,int position) {
        ConstraintLayout trajet_dialog=binding.getRoot().findViewById(R.id.dialog_voiture_place);
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_voiture_place,trajet_dialog);
        btn_enregistrer=dialogView.findViewById(R.id.btn_enregistrer);
        btn_retour=dialogView.findViewById(R.id.btn_retour);
        place_large=dialogView.findViewById(R.id.place_large);
        place_long=dialogView.findViewById(R.id.place_long);
        num_voiture=dialogView.findViewById(R.id.num_voiture);
        edit_place=dialogView.findViewById(R.id.edit_place);
        gridLayout=dialogView.findViewById(R.id.placee);
        place_large.setText(vehiculeModel.getNb_colonne());
        place_long.setText(vehiculeModel.getNb_rangee());
        int[][] place= PlaceVoiture.generatePlace(
                Integer.parseInt(vehiculeModel.getNb_colonne()),
                Integer.parseInt(vehiculeModel.getNb_rangee())
        );
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
            vehiculeModel.setNumeroVehicule(num_voiture.getText().toString());
            vehiculeModel.setNb_colonne(place_large.getText().toString());
            vehiculeModel.setNb_rangee(place_long.getText().toString());
            int capa=Integer.parseInt(place_large.getText().toString())*Integer.parseInt(place_long.getText().toString())-2;
            vehiculeModel.setCapacite(capa);
            if(isajout){
                ajoutVoitureApi(vehiculeModel,alertDialog);
            }else{
                majVoitureApi(vehiculeModel,alertDialog,position);
            }

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
    private void getVoitureApi(){
        String idUser=userManage.getUser().getId();
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<List<VehiculeModel>> getCall = apiService.getVehiculeParUser(idUser);
        getCall.enqueue(new Callback<List<VehiculeModel>>() {
            @Override
            public void onResponse(Call<List<VehiculeModel>> call, Response<List<VehiculeModel>> response) {
                vehiculeList=response.body();
                afficherVoiture();
            }
            @Override
            public void onFailure(Call<List<VehiculeModel>> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ajoutVoitureApi(VehiculeModel vehicule,AlertDialog alertDialog){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<VehiculeModel> postCall = apiService.postVehicule(vehicule);
        postCall.enqueue(new Callback<VehiculeModel>() {
            @Override
            public void onResponse(Call<VehiculeModel> call, Response<VehiculeModel> response) {
                if(response.isSuccessful()){
                    //vehiculeList.add(response.body());
                    adapter.ajoutVehiculeAdapter(response.body());
                    Toast.makeText(ProfilActivity.this, "Voiture ajouter", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    // Afficher l'erreur dans un Toast
                    try {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(ProfilActivity.this, "Erreur: " + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ProfilActivity.this, "Erreur inconnue", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<VehiculeModel> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, "echec de cpnnexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void majVoitureApi(VehiculeModel vehicule,AlertDialog alertDialog,int position){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<VehiculeModel> postCall = apiService.majVehicule(vehicule.getIdVehicule(), vehicule);
        postCall.enqueue(new Callback<VehiculeModel>() {
            @Override
            public void onResponse(Call<VehiculeModel> call, Response<VehiculeModel> response) {
                // Mettre à jour la liste des véhicules dans l'Adapter
                vehiculeList.set(position, response.body()); // positionToUpdate est l'indice du véhicule modifié
                adapter.notifyDataSetChanged();
                Toast.makeText(ProfilActivity.this, "Modification enregistrer", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
            @Override
            public void onFailure(Call<VehiculeModel> call, Throwable t) {

            }
        });

    }
    private void confirmationSuppr(int position){
        ConstraintLayout trajet_dialog=findViewById(R.id.dialog_confirmation);
        View view= LayoutInflater.from(ProfilActivity.this).inflate(R.layout.dialog_confirmation,trajet_dialog);
        TextView textMsg=view.findViewById(R.id.date);
        textMsg.setText("Voulez-vous vraiment supprimer cette voiture ?");
        Button okvalider=view.findViewById(R.id.confirmerBtn);
        okvalider.setText("supprimer");
        Button annulerbtn=view.findViewById(R.id.annulerBtn);

        AlertDialog.Builder builder=new AlertDialog.Builder(ProfilActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        okvalider.findViewById(R.id.confirmerBtn).setOnClickListener(v-> {
            suppreVoitureApi(alertDialog,position);
        });
        annulerbtn.findViewById(R.id.annulerBtn).setOnClickListener(v-> {
            alertDialog.dismiss();
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void suppreVoitureApi(AlertDialog alertDialog,int position) {
        VehiculeModel vehiculeModel=vehiculeList.get(position);
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<Void> supprCall = apiService.suppreVehicule(vehiculeModel.getIdVehicule());
        supprCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //vehiculeList.remove(position);
                adapter.removeItem(position);
                Toast.makeText(ProfilActivity.this, "Voiture supprimer", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, "echec de connexion", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

}