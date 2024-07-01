package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EditText num, mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        num = binding.num;
        mdp = binding.mdp;
        binding.inscrire.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, InscriptionActivity.class));
        });
        binding.btn.setOnClickListener(view -> {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            //Location position = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0, (LocationListener) this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L,0, (LocationListener) this);
            LocationListener locationListener=new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Toast.makeText(LoginActivity.this, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                }
            };
            /*if(validationMdp() && validationNum()){
                validationUtilisateur();
            }*/
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

    }
}