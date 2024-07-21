package com.example.myapplication.outile;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.bddsqlite.ConnectBddSqlite;
import com.example.myapplication.bddsqlite.database.AppDatabase;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.service.SmsService;

import java.io.Serializable;
import java.util.List;

public class MySmsReceiver extends BroadcastReceiver {
    private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    private AppDatabase conn;
    private UtilisateurModel userLogin;
    private UserManage userManage;

    @Override
    public IBinder peekService(Context context, Intent service) {
        conn=ConnectBddSqlite.connectBdd(context);
        return super.peekService(context, service);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        userManage=new UserManage(context);
        userLogin=userManage.getUser();
        conn=ConnectBddSqlite.connectBdd(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent serviceIntent=new Intent(context, SmsService.class);
            context.startForegroundService(serviceIntent);
        }
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                //Toast.makeText(context, msgs[i].getOriginatingAddress(), Toast.LENGTH_LONG).show();
                if(userLogin!=null){
                    if (userLogin.isEst_conducteur()){
                        PaiementModel paiement=PaiementModel.parseFromStringAdmin(msgs[i].getMessageBody());
                        if(paiement!=null && msgs[i].getOriginatingAddress().equals("+261346756924")){

                        }
                    }else{
                        PaiementModel paiement=PaiementModel.parseFromStringClient(msgs[i].getMessageBody());
                        if(paiement!=null && msgs[i].getOriginatingAddress().equals("+261346756924")){
                            Toast.makeText(context, "gg", Toast.LENGTH_SHORT).show();
                            Intent intentt = new Intent("paiement_received_action");
                            intentt.putExtra("paiement", msgs[i].getMessageBody());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intentt);
                        }
                    }

                }

            }
        }
    }
}
