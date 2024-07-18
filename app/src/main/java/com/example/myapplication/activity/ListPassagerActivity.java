package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapteur.ReservationUtilisateurAdapter;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.bddsqlite.ConnectBddSqlite;
import com.example.myapplication.bddsqlite.database.AppDatabase;
import com.example.myapplication.databinding.ActivityListPassagerBinding;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.UtilisateurModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPassagerActivity extends AppCompatActivity {
    private ActivityListPassagerBinding binding;
    private List<ReservationModel> reservationModelList;
    private RecyclerView recyclerView;
    private ReservationUtilisateurAdapter adapter;
    private ApiService apiService;
    private AppDatabase databaseSql;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListPassagerBinding.inflate(getLayoutInflater());
        databaseSql= ConnectBddSqlite.connectBdd(this);
        setContentView(binding.getRoot());
        binding.aucunPassager.setVisibility(View.INVISIBLE);
        actionToolbar();
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.resultat);
        reservationModelList=new ArrayList<>();
        getlistReservation();

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
                onSupportNavigateUp();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getlistReservation(){
        String idTrajet = getIntent().getStringExtra("idTrajet");
        Toast.makeText(this, idTrajet, Toast.LENGTH_LONG).show();
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<List<ReservationModel>> getcall=apiService.getReservationidTrajet(idTrajet);
        getcall.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                reservationModelList=response.body();
                if(reservationModelList.isEmpty())
                    binding.aucunPassager.setVisibility(View.VISIBLE);
                adapter = new ReservationUtilisateurAdapter(ListPassagerActivity.this, reservationModelList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListPassagerActivity.this));
                databaseSql.reservationDao().viderTable(idTrajet);
                databaseSql.reservationDao().insertReservations(reservationModelList);
            }

            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                Toast.makeText(ListPassagerActivity.this, "achec de connexion", Toast.LENGTH_SHORT).show();
                reservationModelList=databaseSql.reservationDao().getAllReservations(idTrajet);
                if(reservationModelList.isEmpty())
                    binding.aucunPassager.setVisibility(View.VISIBLE);
                adapter = new ReservationUtilisateurAdapter(ListPassagerActivity.this, reservationModelList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListPassagerActivity.this));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}