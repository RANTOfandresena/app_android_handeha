package com.example.myapplication.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.allConstant.Allconstant;


public class AppPermissions {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private boolean isStorageOk(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }
    private void requestStoragePermission(Activity activity){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{   Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                Allconstant.STOREGE_REQUEST_CODE);
    }
}
