package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.fragment.CarteFragment;
import com.example.myapplication.R;
import com.example.myapplication.fragment.RechercheFragment;
import com.example.myapplication.fragment.ReservationFragment;
import com.example.myapplication.fragment.TrajetAdminFragment;
import com.example.myapplication.fragment.TransportFragment;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.outile.UserManage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import android.Manifest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TransportFragment.ToolbarVisibilityListener, LocationListener {
    //private ActivityMainBinding binding;
    FloatingActionButton fab;
    private LocationManager locationManager;
    private Button seconnecter;
    private Toolbar toolbar;
    private NavigationView navigationView;
    SharedPreferences sharedPreferences;
    private ApiService apiService;;
    Gson gson;

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    CarteFragment carteFragment=new CarteFragment();
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.nav_view);
        gson=new Gson();

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        updateMenuItems();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.clise_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new TransportFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        replaceFragment(carteFragment);

        bottomNavigationView.setBackground(null);
        //action Item dans le navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.carte){
                replaceFragment(carteFragment);
            } else if(item.getItemId()==R.id.transport) {
                if(sharedPreferences.getBoolean("isLoggedIn", false)){
                    replaceFragment(new TrajetAdminFragment());
                }else{
                    replaceFragment(new TransportFragment());
                }
            }else if (item.getItemId()==R.id.recherche) {
                replaceFragment(new RechercheFragment());
            } else if (item.getItemId()==R.id.reservation) {
                replaceFragment(new ReservationFragment());
            }
            return true;
        });
        //
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idItem=item.getItemId();
                if(idItem==R.id.nav_sedeconnecter){
                    afficherConfirmation();
                }else if(idItem==R.id.nav_home){
                    startActivity(new Intent(MainActivity.this, ProfilActivity.class));
                }
                return false;
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Passer en direct est cliqué",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    @Override
    public void cacherToolbar() {
        getSupportActionBar().hide();
    }

    @Override
    public void afficherToolbar() {
        getSupportActionBar().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMenuItems();
    }

    private void updateMenuItems() {
        Menu menu = navigationView.getMenu();
        menu.clear();

        View headerView = navigationView.getHeaderView(0);
        seconnecter = headerView.findViewById(R.id.seconncter);
        TextView numeroTextView=headerView.findViewById(R.id.numeroUser);
        TextView pseudoTextView=headerView.findViewById(R.id.pseudoUser);
        ImageView imageView =headerView.findViewById(R.id.imageUser);

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            navigationView.inflateMenu(R.menu.nav_menu_conneceter);
            seconnecter.setVisibility(View.GONE);
            UserManage userManage=new UserManage(this);
            if(userManage.getUser()!=null){
                numeroTextView.setText(userManage.getUser().getNumero());
                pseudoTextView.setText(userManage.getUser().getUsername());
            }
            imageView.setImageResource(R.drawable.baseline_person_24);
        } else {
            navigationView.inflateMenu(R.menu.nav_menu);
            seconnecter.setVisibility(View.VISIBLE);
            numeroTextView.setText("Aucun compte connecter");
            pseudoTextView.setText("");
            imageView.setImageResource(R.drawable.baseline_person_off_24);
        }
    }

    private UtilisateurModel getUser() {
        String json = sharedPreferences.getString("UtilisateurModel", null);
        return gson.fromJson(json, UtilisateurModel.class);
    }


    private String getAuthToken() {
        return sharedPreferences.getString("apikey", null);
    }

    public void seConnecter(View v){
        startActivity(new Intent(MainActivity.this,LoginActivity .class));
    }
    private void deConnecter(){
        apiService= RetrofitClient.getClient(URL_SERVER,getAuthToken()).create(ApiService.class);
        Call<Void> postCall = apiService.logout();
        postCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.putString("UtilisateurModel",null);
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }else Toast.makeText(MainActivity.this, "echec de deconnexion", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Echec de deconnexion", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void afficherConfirmation(){
        ConstraintLayout trajet_dialog=findViewById(R.id.dialog_confirmation);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_confirmation,trajet_dialog);
        Button okvalider=view.findViewById(R.id.confirmerBtn);
        Button annulerbtn=view.findViewById(R.id.annulerBtn);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        okvalider.findViewById(R.id.confirmerBtn).setOnClickListener(v-> {
                deConnecter();
                Toast.makeText(MainActivity.this, "Deconnexion", Toast.LENGTH_SHORT).show();
        });
        annulerbtn.findViewById(R.id.annulerBtn).setOnClickListener(v-> {
                alertDialog.dismiss();
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, String.valueOf(location.getLatitude())+String.valueOf(location.getLatitude()) , Toast.LENGTH_SHORT).show();
    }
}