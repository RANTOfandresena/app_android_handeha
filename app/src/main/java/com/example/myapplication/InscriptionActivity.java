package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityInscriptionBinding;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRatour.setOnClickListener(view->{
            onBackPressed();
        });
    }
}