package com.example.myapplication.outile;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;
import com.example.myapplication.activity.ListPassagerActivity;
import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.bddsqlite.ConnectBddSqlite;
import com.example.myapplication.bddsqlite.database.AppDatabase;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.service.SmsService;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
        Intent serviceIntent = new Intent(context, SmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
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
                Toast.makeText(context, "msm:"+msgs[i].getOriginatingAddress(), Toast.LENGTH_LONG).show();
                if(userLogin!=null){
                    if (userLogin.isEst_conducteur()){
                        //Toast.makeText(context, "admin", Toast.LENGTH_SHORT).show();
                        PaiementModel paiement=PaiementModel.parseFromStringAdmin(msgs[i].getMessageBody());
                        if(paiement!=null && msgs[i].getOriginatingAddress().equals("+261346756924")){
                            Toast.makeText(context, paiement.getRefapp(), Toast.LENGTH_LONG).show();//gAE
                            if(paiement.getRefapp().contains(" 0h")){
                                String[] refEtId=paiement.getRefapp().split(" 0h");

                                //gAE
                                if(refEtId.length==3)
                                    verification(context,refEtId,paiement);
                            }
                        }
                    }else{
                        PaiementModel paiement=PaiementModel.parseFromStringClient(msgs[i].getMessageBody());
                        if(paiement!=null && msgs[i].getOriginatingAddress().equals("+261346756924")){
                            Intent intentt = new Intent("paiement_received_action");
                            intentt.putExtra("paiement", msgs[i].getMessageBody());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intentt);

                            Intent activityIntent = new Intent(context, TrajetActivity.class);
                            activityIntent.putExtra("paiement", msgs[i].getMessageBody());
                            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(activityIntent);
                        }
                    }

                }
            }
        }
    }

    private void notifyUserAboutMessage(Context context,PaiementModel paiement,int idTrajet) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "sms_channel";//alert.wav
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "SMS Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(soundUri, null);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.reserver)
                .setContentTitle("Nouveau reservation")
                .setContentText(paiement.getNomRemetant()+"a fait une reservation sur votre trajet")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)
                .setAutoCancel(true);

        Intent intent = new Intent(context, ListPassagerActivity.class);
        intent.putExtra("idTrajet", idTrajet);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }
    private void verification(Context context,String[] redId,PaiementModel paiement){
        AppDatabase bddSqlite=ConnectBddSqlite.connectBdd(context);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(context, "ok ok", Toast.LENGTH_LONG).show());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TrajetModel trajetModel=bddSqlite.trajetDao().getTrajetId(redId[1]);
                if(trajetModel!=null){
                    int[] listSiege=EncodeurTableauFixe.decodeString1(redId[0]);
                    if(trajetModel.getSiegeReserver().size()>listSiege.length){
                        for(int i=0;i!=listSiege.length;i++){
                            if(trajetModel.getSiegeReserver().get(listSiege[i])!=0){
                                handler.post(() -> Toast.makeText(context, "echec,place dejat pris", Toast.LENGTH_SHORT).show());
                                return;
                            }
                        }
                        String[] anarana=paiement.getNomRemetant().split(" ");

                        UtilisateurModel fakeUtilisateur=new UtilisateurModel(anarana[0],anarana[1],String.valueOf(paiement.getMontant()));
                        ReservationModel reservationModel=new ReservationModel(
                                Integer.parseInt(redId[2]),//ok ok
                                fakeUtilisateur,//ok ok
                                trajetModel,//ok ok
                                trajetModel.getIdTrajet(),//ok ok
                                EncodeurTableauFixe.convertieIntArToList(listSiege),//ok ok
                                paiement
                        );
                        long id=bddSqlite.reservationDao().insertReservation(reservationModel);
                        paiement.setIdReservation((int) id);
                        bddSqlite.paiementDao().insertPaiement(paiement);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Toast.makeText(context, "insertion avec succes", Toast.LENGTH_SHORT).show();
                            notifyUserAboutMessage(context, paiement,trajetModel.getIdTrajet());
                        }, 3000);
                    }
                }
            }
        });
    }
}
