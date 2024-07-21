package com.example.myapplication.activity;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.StartForegroundCalledOnStoppedServiceException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
import com.example.myapplication.permissions.AppPermissions;
import com.example.myapplication.service.SmsService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import android.Manifest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RechercheFragment.OnCitySelectedListener {
    //private ActivityMainBinding binding;
    FloatingActionButton fab;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private AppPermissions appPermissions;
    //private LocationManager locationManager;
    private Button seconnecter;
    public Toolbar toolbar;
    private NavigationView navigationView;
    SharedPreferences sharedPreferences;
    private ApiService apiService;
    private UtilisateurModel userLogin;
    private UserManage userManage;
    Gson gson;

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private CarteFragment carteFragment;
    private TransportFragment transportFragment;
    private TrajetAdminFragment trajetAdminFragment;
    private RechercheFragment rechercheFragment;
    private ReservationFragment reservationFragment;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.nav_view);
        gson=new Gson();
        userManage=new UserManage(this);
        userLogin=userManage.getUser();

        lancerService();
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
        /*if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new TransportFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }*/


        /*replaceFragment(carteFragment);
        //action Item dans le navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.carte){
                replaceFragment(carteFragment);
            } else if(item.getItemId()==R.id.transport) {
                if(userLogin!=null && userLogin.isEst_conducteur()){
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
        });*/
        //
        gestionFenetre();
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
        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }*/
        demandePermition();
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

        if (userLogin!=null) {
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
                    userManage.deconnect();
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

    /*@Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, String.valueOf(location.getLatitude())+String.valueOf(location.getLatitude()) , Toast.LENGTH_SHORT).show();
        carteFragment.updateMapLocation(location.getLatitude(), location.getLongitude());
    }*/

    @Override
    public void onCitySelected(double latitude, double longitude) {
        if (carteFragment != null) {
            afficherFragment("CARTE_FRAGMENT");
            bottomNavigationView.setSelectedItemId(R.id.carte);
            carteFragment.updateMapLocation(latitude, longitude);
        }
    }
    private void gestionFenetre(){
        carteFragment = new CarteFragment();
        transportFragment = new TransportFragment();
        trajetAdminFragment = new TrajetAdminFragment();
        rechercheFragment = new RechercheFragment();
        reservationFragment = new ReservationFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, carteFragment, "CARTE_FRAGMENT");
        fragmentTransaction.add(R.id.frame_layout, transportFragment, "TRANSPORT_FRAGMENT");
        fragmentTransaction.add(R.id.frame_layout, trajetAdminFragment, "TRAJET_ADMIN_FRAGMENT");
        fragmentTransaction.add(R.id.frame_layout, rechercheFragment, "RECHERCHE_FRAGMENT");
        fragmentTransaction.add(R.id.frame_layout, reservationFragment, "RESERVATION_FRAGMENT");

        // Masquer tous les fragments sauf le premier
        fragmentTransaction.hide(transportFragment);
        fragmentTransaction.hide(trajetAdminFragment);
        fragmentTransaction.hide(rechercheFragment);
        fragmentTransaction.hide(reservationFragment);

        fragmentTransaction.commit();
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.carte) {
                afficherFragment("CARTE_FRAGMENT");
            }else if(item.getItemId()==R.id.transport) {
                if (userLogin != null && userLogin.isEst_conducteur()) {
                    afficherFragment("TRAJET_ADMIN_FRAGMENT");
                } else {
                    afficherFragment("TRANSPORT_FRAGMENT");
                }
            }else if(item.getItemId()==R.id.recherche) {
                afficherFragment("RECHERCHE_FRAGMENT");
            }else if(item.getItemId()==R.id.reservation) {
                afficherFragment("RESERVATION_FRAGMENT");
            }
            return true;
        });
    }
    private void afficherFragment(String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();

        Fragment carteFragment = fragmentManager.findFragmentByTag("CARTE_FRAGMENT");
        Fragment transportFragment = fragmentManager.findFragmentByTag("TRANSPORT_FRAGMENT");
        Fragment trajetAdminFragment = fragmentManager.findFragmentByTag("TRAJET_ADMIN_FRAGMENT");
        Fragment rechercheFragment = fragmentManager.findFragmentByTag("RECHERCHE_FRAGMENT");
        Fragment reservationFragment = fragmentManager.findFragmentByTag("RESERVATION_FRAGMENT");

        fragmentTransaction.hide(carteFragment);
        fragmentTransaction.hide(transportFragment);
        fragmentTransaction.hide(trajetAdminFragment);
        fragmentTransaction.hide(rechercheFragment);
        fragmentTransaction.hide(reservationFragment);
        //toolbarVisibilityListener= (TransportFragment.ToolbarVisibilityListener) context;
        //toolbarVisibilityListener.cacherToolbar();

        //toolbarVisibilityListener.afficherToolbar();
        //toolbarVisibilityListener=null;
        switch (tag) {
            case "CARTE_FRAGMENT":
                getSupportActionBar().show();
                fragmentTransaction.show(carteFragment);
                break;
            case "TRANSPORT_FRAGMENT":
                getSupportActionBar().hide();
                fragmentTransaction.show(transportFragment);
                break;
            case "TRAJET_ADMIN_FRAGMENT":
                getSupportActionBar().show();
                fragmentTransaction.show(trajetAdminFragment);
                break;
            case "RECHERCHE_FRAGMENT":
                getSupportActionBar().show();
                fragmentTransaction.show(rechercheFragment);
                break;
            case "RESERVATION_FRAGMENT":
                getSupportActionBar().show();
                fragmentTransaction.show(reservationFragment);
                break;
        }

        fragmentTransaction.commit();
    }
    private void demandePermition(){
        appPermissions = new AppPermissions();
        appPermissions = new AppPermissions();

        // Vérifiez et demandez les permissions de stockage
        if (!appPermissions.isStorageOk(this)) {
            appPermissions.requestStoragePermission(this);
        }

        // Vérifiez et demandez les permissions de SMS
        if (!appPermissions.isSmsSendPermissionGranted(this)) {
            appPermissions.requestSmsSendPermission(this);
        }
        if (!appPermissions.isSmsReceivePermissionGranted(this)) {
            appPermissions.requestSmsReceivePermission(this);
        }
        if (!appPermissions.isSmsReadPermissionGranted(this)) {
            appPermissions.requestSmsReadPermission(this);
        }

        // Vérifiez et demandez de désactiver les optimisations de batterie
        if (!appPermissions.isIgnoringBatteryOptimizations(this)) {
            appPermissions.requestIgnoreBatteryOptimizations(this);
        }

        // Vérifiez et demandez les permissions de localisation
        if (!appPermissions.isLocationPermissionGranted(this)) {
            appPermissions.requestLocationPermission(this);
        }

        // Vérifiez et demandez d'activer la localisation si désactivée
        if (!appPermissions.isLocationEnabled(this)) {
            appPermissions.requestEnableLocation(this);
        }
    }
    private void lancerService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent serviceIntent=new Intent(this, SmsService.class);
            startForegroundService(serviceIntent);
            forgeBackgroundServiceRuning();
        }
    }
    public boolean forgeBackgroundServiceRuning(){
        ActivityManager activityManager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo:activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(SmsService.class.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getLastLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}