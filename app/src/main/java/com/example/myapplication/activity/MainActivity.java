package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.fragment.CarteFragment;
import com.example.myapplication.R;
import com.example.myapplication.fragment.RechercheFragment;
import com.example.myapplication.fragment.ReservationFragment;
import com.example.myapplication.fragment.TrajetAdminFragment;
import com.example.myapplication.fragment.TransportFragment;
import com.example.myapplication.model.UtilisateurModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements TransportFragment.ToolbarVisibilityListener {
    //private ActivityMainBinding binding;
    FloatingActionButton fab;
    private Button seconnecter;
    private Toolbar toolbar;
    private NavigationView navigationView;
    SharedPreferences sharedPreferences;
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
                if(item.getItemId()==R.id.nav_sedeconnecter){
                    afficherConfirmation();
                }
                return false;
            }
        });

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
                Toast.makeText(MainActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();

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

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            navigationView.inflateMenu(R.menu.nav_menu_conneceter);
            seconnecter.setVisibility(View.GONE);
        } else {
            navigationView.inflateMenu(R.menu.nav_menu);
            seconnecter.setVisibility(View.VISIBLE);
        }
    }

    private UtilisateurModel getUser() {
        String json = sharedPreferences.getString("UtilisateurModel", null);
        return gson.fromJson(json, UtilisateurModel.class);
    }
    private void deConnecter(){
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }
    public void seConnecter(View v){
        startActivity(new Intent(MainActivity.this,LoginActivity .class));
    }
    private void afficherConfirmation(){
        ConstraintLayout trajet_dialog=findViewById(R.id.dialog_confirmation);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_confirmation,trajet_dialog);
        Button okvalider=view.findViewById(R.id.confirmerBtn);
        Button annulerbtn=view.findViewById(R.id.annulerBtn);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        okvalider.findViewById(R.id.confirmerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deConnecter();
                Toast.makeText(MainActivity.this, "Deconnexion", Toast.LENGTH_SHORT).show();
            }
        });
        annulerbtn.findViewById(R.id.annulerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

}