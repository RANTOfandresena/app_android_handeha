package com.example.myapplication.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.allConstant.Allconstant;


public class AppPermissions {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 3;
    private static final int MY_PERMISSIONS_REQUEST_BATTERY_OPTIMIZATIONS = 4;
    private static final int STORAGE_REQUEST_CODE = 5;
    private static final int LOCATION_REQUEST_CODE = 6;
    private static final int REQUEST_ENABLE_LOCATION = 7;

    public  boolean isStorageOk(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }
    public void requestStoragePermission(Activity activity){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{   Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                Allconstant.STOREGE_REQUEST_CODE);
    }
    // Vérification des permissions d'envoi de SMS
    public boolean isSmsSendPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }
    public void requestSmsSendPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS
        );
    }
    // Vérification des permissions de réception de SMS
    public boolean isSmsReceivePermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }
    public void requestSmsReceivePermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_RECEIVE_SMS
        );
    }
    // Vérification des permissions de lecture de SMS
    public boolean isSmsReadPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }
    // Demande des permissions de lecture de SMS
    public void requestSmsReadPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.READ_SMS},
                MY_PERMISSIONS_REQUEST_READ_SMS
        );
    }
    // Vérification des optimisations de batterie
    public boolean isIgnoringBatteryOptimizations(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        String packageName = context.getPackageName();
        return pm.isIgnoringBatteryOptimizations(packageName);
    }
    // Demande de désactivation des optimisations de batterie
    public void requestIgnoreBatteryOptimizations(Activity activity) {
        Intent intent = new Intent();
        String packageName = activity.getPackageName();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + packageName));
        activity.startActivityForResult(intent, MY_PERMISSIONS_REQUEST_BATTERY_OPTIMIZATIONS);
    }
    // Vérification des permissions de localisation
    public boolean isLocationPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Demande des permissions de localisation
    public void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_REQUEST_CODE
        );
    }

    // Vérification si la localisation est activée
    public boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // Demande d'activation des services de localisation
    public void requestEnableLocation(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, REQUEST_ENABLE_LOCATION);
    }
}
