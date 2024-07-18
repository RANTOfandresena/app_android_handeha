package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.databinding.ActivityTrajetBinding;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.model.VehiculeModel;
import com.example.myapplication.outile.Algo;
import com.example.myapplication.outile.PlaceVoiture;
import com.example.myapplication.outile.UserManage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrajetActivity extends AppCompatActivity {
    private ActivityTrajetBinding binding;
    private UserManage userManage;
    private TrajetModel trajetModel;
    private ApiService apiService;
    private List<Button> bouttonplace;
    private List<Integer> placeReserver;
    private UtilisateurModel chauffeur;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTrajetBinding.inflate(getLayoutInflater());
        apiService = RetrofitClient.getClient(URL_SERVER, null).create(ApiService.class);

        userManage=new UserManage(this);
        bouttonplace=new ArrayList<>();
        placeReserver=new ArrayList<>();
        /*binding.btnMoins.setOnClickListener(view->{
            decremente(-1);
        });
        binding.btnPlus.setOnClickListener(View->{
            decremente(1);
        });*/
        Intent intent=getIntent();
        if(intent!=null){
            trajetModel =(TrajetModel) intent.getSerializableExtra("data");
            chauffeur=(UtilisateurModel) intent.getSerializableExtra("chaufeurModel");
            if(trajetModel!=null && chauffeur!=null){
                binding.depart.setText(trajetModel.getLieuDepart());
                binding.arrive.setText(trajetModel.getLieuArrive());
                binding.date.setText("Depart : "+trajetModel.getHoraire());
                binding.prix.setText("Prix : "+trajetModel.getPrix());
                binding.placelibre.setText("Places Libres : "+ Algo.compterNumbre(trajetModel.getSiegeReserver(),0));
                getVoiture(trajetModel.getIdVehicule());
            }else {
                Toast.makeText(this, "trajetModel: "+trajetModel+" chauffeur"+chauffeur, Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            finish();
            Toast.makeText(this, "intent null", Toast.LENGTH_SHORT).show();
        }
        binding.valider.setOnClickListener(view->{
            //afficherReservationPlace();
            if(binding.totalprix.getText().toString().equalsIgnoreCase("Montont Total : 0ar")){
                Toast.makeText(this, "Vous avez reservé aucune place", Toast.LENGTH_SHORT).show();
            }else {
                //postApireservation();
                confirmationPaimentAppel();
            }

        });
        setContentView(binding.getRoot());
        actionToolbar();
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
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    private void confirmationPaimentAppel(){
        ConstraintLayout trajet_dialog=findViewById(R.id.dialog_confirmation_payement);
        View view= LayoutInflater.from(TrajetActivity.this).inflate(R.layout.dialog_confirmation_payement,trajet_dialog);
        Button btn_effectuer=view.findViewById(R.id.btn_effectuer);
        Button btn_retour=view.findViewById(R.id.btn_retour);
        Button btn_copie=view.findViewById(R.id.btn_copie);

        AlertDialog.Builder builder=new AlertDialog.Builder(TrajetActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        btn_copie.findViewById(R.id.btn_copie).setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Label", "apprefOkOk");
            clipboard.setPrimaryClip(clipData);
            Toast.makeText(TrajetActivity.this, "Le 'raison' est copié dans le presse-papiers", Toast.LENGTH_SHORT).show();
            btn_effectuer.findViewById(R.id.btn_effectuer).setVisibility(View.VISIBLE);
        });
        btn_retour.findViewById(R.id.btn_retour).setOnClickListener(v->{
            alertDialog.dismiss();
        });
        btn_effectuer.findViewById(R.id.btn_effectuer ).setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Label", "apprefOkOk");
            clipboard.setPrimaryClip(clipData);
            //requestPermissionCall();
            //if(estAutoriserCall()){//-------------------------
            String codeUSSD = "#111*1*2*0340264169*1000*1#";

            // Check for permission before initiating USSD call
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }

            // Encode the "#" symbol for reliable USSD execution
            String encodedHash = Uri.encode("#");
            Uri uri = Uri.parse("tel:" + codeUSSD.replace("#", encodedHash));

            // Use ACTION_DIAL for better compatibility
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);

            // Optionally, display the USSD code for user reference
            Toast.makeText(this, codeUSSD, Toast.LENGTH_SHORT).show();
            //}else{
              //  requestPermissionCall();
            //}//--------------------------
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
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

                    if(trajetModel.getSiegeReserver().get(placeNumber-1)!=0){
                        //button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_place));
                        button.setTextColor(getResources().getColor(R.color.white));
                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        button.setBackgroundColor(getResources().getColor(R.color.white));
                        button.setTextColor(getResources().getColor(R.color.lavender));
                        button.setBackgroundResource(R.drawable.button_stylee);
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
                actionbtn();
            }
            @Override
            public void onFailure(Call<List<VehiculeModel>> call, Throwable t) {
                Toast.makeText(TrajetActivity.this, "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void actionbtn(){
        for (byte i=0;i!=bouttonplace.size();i++) {
            Button btn=bouttonplace.get(i);
            btn.setOnClickListener(v -> {
                int numplace=Integer.parseInt(btn.getText().toString());
                if(trajetModel.getSiegeReserver().get(numplace-1)==0){
                    btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_place));
                    if(!placeReserver.contains(numplace)){
                        placeReserver.add(numplace);
                        btn.setBackgroundColor(getResources().getColor(R.color.lavender));
                        btn.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        placeReserver.remove((Integer) numplace);
                        btn.setBackgroundColor(getResources().getColor(R.color.white));
                        btn.setTextColor(getResources().getColor(R.color.lavender));
                    }
                    binding.voyageur.setText(String.valueOf(placeReserver.size()));
                    String strprix = trajetModel.getPrix();
                    strprix = strprix.replace(".00", "");
                    binding.totalprix.setText("Montont Total : "+placeReserver.size()*Integer.parseInt(strprix)+"ar");
                    binding.placelibre.setText("Places Libres : "+ String.valueOf(Algo.compterNumbre(trajetModel.getSiegeReserver(),0)-placeReserver.size()));
                }
            });
        }
    }

    private void postApireservation(){
        int idutilisateur=Integer.parseInt(userManage.getUser().getId());
        ReservationModel reservationModel=new ReservationModel(idutilisateur,trajetModel.getIdTrajet(),placeReserver);
        Call<ReservationModel> postCall = apiService.postReservation(reservationModel);
        postCall.enqueue(new Callback<ReservationModel>() {
            @Override
            public void onResponse(Call<ReservationModel> call, Response<ReservationModel> response) {
                afficherReservationPlace();
            }

            @Override
            public void onFailure(Call<ReservationModel> call, Throwable t) {

            }
        });
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
    public void requestPermissionCall(){
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CALL_PHONE,
                },
                1);
    }
    public boolean estAutoriserCall(){
        return ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED;
    }
}