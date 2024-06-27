package com.example.myapplication.outile;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.view.LayoutInflater;

import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogLayoutBinding;

public class ChargementDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public ChargementDialog(Activity activity){
        this.activity=activity;
    }

    private void startChargement(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity, R.style.chargementDialogStyle);
        DialogLayoutBinding binding=DialogLayoutBinding.inflate(LayoutInflater.from(activity),null,false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }
    private void stopChargement(){
        alertDialog.dismiss();
    }
}
