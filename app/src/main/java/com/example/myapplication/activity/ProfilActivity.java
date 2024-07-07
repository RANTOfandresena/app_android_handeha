package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapteur.VehiculeAdapter;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivityProfilBinding;
import com.example.myapplication.model.VehiculeModel;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {
    private ActivityProfilBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<VehiculeModel> myList = new ArrayList<>();
        RecyclerView recyclerView=binding.vehiculeliste;

        myList.add(new VehiculeModel(1,10, "11", "65756757HJ",R.drawable.a3));
        myList.add(new VehiculeModel(1,40, "11", "6575kjlk6757HJ",R.drawable.a3));
        myList.add(new VehiculeModel(1,20, "11", "65756jhk757HJ",R.drawable.a3));

        VehiculeAdapter adapter = new VehiculeAdapter(this, myList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(this, String.valueOf(myList.size()), Toast.LENGTH_SHORT).show();
    }
}