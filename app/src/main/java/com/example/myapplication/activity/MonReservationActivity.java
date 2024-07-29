package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.bddsqlite.ConnectBddSqlite;
import com.example.myapplication.bddsqlite.database.AppDatabase;
import com.example.myapplication.databinding.ActivityMonReservationBinding;
import com.example.myapplication.databinding.ActivityTrajetBinding;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.ReservationModel;
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
    private ReservationModel reservationM;
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
            reservationM=(ReservationModel) intent.getSerializableExtra("reservation");
            if(trajetModel!=null && chauffeur!=null && reservationM!=null){
                String lieuDeparts=trajetModel.getLieuDepart();
                String[] lieuDepart = lieuDeparts.split("\\|");
                binding.depart.setText(lieuDepart[0]);
                String lieuArrives=trajetModel.getLieuDepart();
                String[] lieuArrive = lieuArrives.split("\\|");
                binding.arrive.setText(lieuArrive[0]);
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
        binding.btnrecu.setOnClickListener(v -> afficheDialog());
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
    //------------------------------------------------------------------ //------------------------------------------------------------------
    //------------------------------------------------------------------ //------------------------------------------------------------------
    private void afficheDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_paiement, null);
        builder.setView(dialogView);
        afficheDonne(dialogView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        Button dialogButton = dialogView.findViewById(R.id.btn_back);
        Button btn_payment_sms=dialogView.findViewById(R.id.btn_payment_sms);
        btn_payment_sms.setOnClickListener(v->{
            readSms(reservationM.getPaiement().getRef());
        });
        dialogButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void afficheDonne(View dialogView){
        TextView info=dialogView.findViewById(R.id.info_message);
        TextView nom=dialogView.findViewById(R.id.nom);
        TextView num_phone=dialogView.findViewById(R.id.num_phone);
        TextView ref=dialogView.findViewById(R.id.ref);
        TextView refapp=dialogView.findViewById(R.id.refapp);
        TextView total_prix=dialogView.findViewById(R.id.total_prix);
        TextView daty=dialogView.findViewById(R.id.daty);
        info.setText("Vous avez envoyé de l'argent de cette personne:");
        nom.setText("Nom et prénom: "+ chauffeur.getFirst_name()+" "+chauffeur.getLast_name());
        num_phone.setText("Numéro : "+chauffeur.getNumero());
        ref.setText("Référence mobile monnaie:"+reservationM.getPaiement().getRef());
        refapp.setText("Référence de l'application: "+reservationM.getPaiement().getRefapp());
        total_prix.setText("Prix total : "+reservationM.getPaiement().getMontant()+"Ar");
        daty.setText("Date de paiement :"+ DateChange.changerLaDate(reservationM.getPaiement().getDatePaiement()));
    }
    public void readSms(String ref) {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        boolean verification=true;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                if (body.contains(ref)) {
                    Toast.makeText(this, "le code de reference exist dans votre message", Toast.LENGTH_SHORT).show();
                    ouvrirSmsAppavecMessage(ref);
                    verification=false;
                    break;
                }
            }
            if(verification)
                Toast.makeText(this, "le code de reference n'exist pas dans votre message", Toast.LENGTH_SHORT).show();
            cursor.close();
        }
    }

    private void ouvrirSmsAppavecMessage(String ref) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label", ref);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(this, "Le code de reference est copié dans votre presse-papiers", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }
}