package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.bddsqlite.ConnectBddSqlite;
import com.example.myapplication.bddsqlite.database.AppDatabase;
import com.example.myapplication.databinding.ActivityMonReservationBinding;
import com.example.myapplication.databinding.ActivityTrajetBinding;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.model.VehiculeModel;
import com.example.myapplication.outile.Algo;
import com.example.myapplication.outile.DateChange;
import com.example.myapplication.outile.PlaceVoiture;
import com.example.myapplication.outile.UserManage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonReservationActivity extends AppCompatActivity {
    private ActivityMonReservationBinding binding;
    private AlertDialog alertDialog;
    private BroadcastReceiver smsVerification;
    private String montantTotal;
    private UserManage userManage;
    private TrajetModel trajetModel;
    private ApiService apiService;
    private List<Button> bouttonplace;
    private List<Integer> placeReserver;
    private UtilisateurModel chauffeur;
    private Toolbar toolbar;
    private PaiementModel paiementModel;
    private AppDatabase bddSqlite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMonReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiService = RetrofitClient.getClient(URL_SERVER, null).create(ApiService.class);
        paiementModel=new PaiementModel();
        userManage=new UserManage(this);
        bouttonplace=new ArrayList<>();
        placeReserver=new ArrayList<>();
        bddSqlite= ConnectBddSqlite.connectBdd(this);
        Intent intent=getIntent();
        if(intent!=null){
            trajetModel =(TrajetModel) intent.getSerializableExtra("data");
            chauffeur=(UtilisateurModel) intent.getSerializableExtra("chaufeurModel");
            if(trajetModel!=null && chauffeur!=null){
                binding.depart.setText(trajetModel.getLieuDepart());
                binding.arrive.setText(trajetModel.getLieuArrive());
                binding.date.setText("Depart : "+ DateChange.changerLaDate(trajetModel.getHoraire()));
                binding.prix.setText("Prix : "+trajetModel.getPrix());
                binding.placelibre.setText("Places Libres : "+ Algo.compterNumbre(trajetModel.getSiegeReserver(),0));
                binding.nomc.setText("Nom : "+chauffeur.getFirst_name());
                binding.prenomc.setText("Prenom : "+chauffeur.getLast_name());
                binding.numeroc.setText("Numero :"+chauffeur.getNumero());
                getVoiture(trajetModel.getIdVehicule());
                paiementModel.setNumero(chauffeur.getNumero());
            }else {
                Toast.makeText(this, "trajetModel: "+trajetModel+" chauffeur"+chauffeur, Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            finish();
            Toast.makeText(this, "intent null", Toast.LENGTH_SHORT).show();
        }
        actionToolbar();
    }
    private void getVoiture(int idVoiture){
        String idVoitureString =String.valueOf(idVoiture);
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<List<VehiculeModel>> getCall = apiService.getVehiculeId(idVoitureString);
        getCall.enqueue(new Callback<List<VehiculeModel>>() {
            @Override
            public void onResponse(Call<List<VehiculeModel>> call, Response<List<VehiculeModel>> response) {
                VehiculeModel voiture=response.body().get(0);
                int longe=Integer.parseInt(voiture.getNb_colonne());
                int large=Integer.parseInt(voiture.getNb_rangee());
                int[][] places= PlaceVoiture.generatePlace(longe,large);
                afficherPlace(places,binding.placee);
                binding.numerov.setText("Numero du voiture : "+voiture.getNumeroVehicule());
                if(voiture.getPosition()!=null)
                    binding.marquev.setText("Marque du voiture : "+voiture.getPosition());
            }
            @Override
            public void onFailure(Call<List<VehiculeModel>> call, Throwable t) {
                Toast.makeText(MonReservationActivity.this, "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });
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
                //Button button = (Button) LayoutInflater.from(this).inflate(R.layout.btn_place, null);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 150;
                params.height = 150;
                params.setMargins(8, 8, 8, 8);
                //button.setBackgroundColor(getResources().getColor(R.color.trasparent));

                // Si c'est le premier bouton, fusionnez avec le deuxième
                if (i == 0 && j == 0) {
                    params.columnSpec = GridLayout.spec(j, 2); // Occupe deux colonnes
                    button.setText("chauffeur");
                    button.setBackgroundResource(R.drawable.button_stylee);
                    firstButtonMerged = true;  // Indique que le premier bouton a été fusionné
                } else {
                    // Ajustez les indices de colonne pour les boutons suivants
                    params.columnSpec = GridLayout.spec(j);
                    button.setText(String.valueOf(placeNumber));

                    /*if(trajetModel.getSiegeReserver().get(placeNumber-1)!=0){
                        //button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_place));
                        button.setTextColor(getResources().getColor(R.color.white));
                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else if (trajetModel.getSiegeReserver().get(placeNumber-1)==Integer.parseInt(userManage.getUser().getId())) {
                        button.setBackgroundColor(getResources().getColor(R.color.lavender));
                        button.setTextColor(getResources().getColor(R.color.white));
                    } else{
                        button.setBackgroundColor(getResources().getColor(R.color.white));
                        button.setTextColor(getResources().getColor(R.color.lavender));
                        button.setBackgroundResource(R.drawable.button_stylee);
                    }*/

                    if(trajetModel.getSiegeReserver().get(placeNumber-1)==0){
                        button.setBackgroundColor(getResources().getColor(R.color.white));
                        button.setTextColor(getResources().getColor(R.color.lavender));
                        button.setBackgroundResource(R.drawable.button_stylee);
                    } else if (trajetModel.getSiegeReserver().get(placeNumber-1)==Integer.parseInt(userManage.getUser().getId())) {
                        button.setBackgroundColor(getResources().getColor(R.color.lavender));
                        button.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        button.setTextColor(getResources().getColor(R.color.white));
                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    bouttonplace.add(button);
                    placeNumber++;
                }

                params.rowSpec = GridLayout.spec(i);
                button.setLayoutParams(params);
                gridLayout.addView(button);
            }
        }
    }
    private void actionToolbar() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable btnretour = getResources().getDrawable(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationIcon(btnretour);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer le clic sur l'icône "retour"
                onSupportNavigateUp();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}