package com.example.myapplication.adapteur;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.outile.DateChange;

import java.util.List;

public class ReservationUtilisateurAdapter extends RecyclerView.Adapter<ReservationUtilisateurAdapter.ReservationUtilisateurViewHolder> {

    private Context context;
    private List<ReservationModel> reservationModel;

    public ReservationUtilisateurAdapter(Context context, List<ReservationModel> r) {
        this.context = context;
        this.reservationModel = r;
    }

    @NonNull
    @Override
    public ReservationUtilisateurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_reservation_utilisateur, parent, false);
        return new ReservationUtilisateurViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationUtilisateurViewHolder holder, int position) {
        ReservationModel reservationModel1 = reservationModel.get(position);
        String strprix=reservationModel1.getTrajet().getPrix();
        strprix = strprix.replace(".00", "");
        String prix_total= String.valueOf(Integer.parseInt(strprix)*reservationModel1.getSiegeNumero().size());
        holder.prix.setText("prix total :"+prix_total+" Ar");
        holder.nom.setText("Nom :"+reservationModel1.getUtilisateurReserver().getFirst_name());
        holder.prenom.setText("Prenom :"+ reservationModel1.getUtilisateurReserver().getLast_name());
        holder.numero.setText("Numero :"+reservationModel1.getUtilisateurReserver().getNumero());
        holder.nb_place.setText("nombre de place reserver :"+String.valueOf(reservationModel1.getSiegeNumero().size()));
        holder.reserver.setOnClickListener(v->{
            afficheDialog(reservationModel1);
        });
    }
    @Override
    public int getItemCount() {
        return reservationModel.size();
    }
    public static class ReservationUtilisateurViewHolder extends RecyclerView.ViewHolder {

        TextView nom,prenom,numero,prix,nb_place;
        Button reserver;

        public ReservationUtilisateurViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.prenom);
            numero = itemView.findViewById(R.id.numero);
            prix = itemView.findViewById(R.id.prix);
            nb_place = itemView.findViewById(R.id.nb_place);
            reserver=itemView.findViewById(R.id.reserver);
        }
    }
    private void afficheDialog(ReservationModel reservation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_paiement, null);
        builder.setView(dialogView);
        afficheDonne(dialogView,reservation);
        AlertDialog dialog = builder.create();
        Button dialogButton = dialogView.findViewById(R.id.btn_back);
        Button btn_payment_sms=dialogView.findViewById(R.id.btn_payment_sms);
        btn_payment_sms.setOnClickListener(v->{
            readSms(reservation.getPaiement().getRef());
        });
        dialogButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void afficheDonne(View dialogView,ReservationModel reservation){
        TextView info=dialogView.findViewById(R.id.info_message);
        TextView nom=dialogView.findViewById(R.id.nom);
        TextView num_phone=dialogView.findViewById(R.id.num_phone);
        TextView ref=dialogView.findViewById(R.id.ref);
        TextView refapp=dialogView.findViewById(R.id.refapp);
        TextView total_prix=dialogView.findViewById(R.id.total_prix);
        TextView daty=dialogView.findViewById(R.id.daty);
        info.setText("Vous avez reçu de l'argent de cette personne:");
        nom.setText("Nom et prénom: "+ reservation.getUtilisateurReserver().getFirst_name()+" "+reservation.getUtilisateurReserver().getLast_name());
        num_phone.setText("Numéro : "+reservation.getUtilisateurReserver().getNumero());
        ref.setText("Référence mobile monnaie:"+reservation.getPaiement().getRef());
        refapp.setText("Référence de l'application: "+reservation.getPaiement().getRefapp());
        total_prix.setText("Prix total : "+reservation.getPaiement().getMontant()+"Ar");
        daty.setText("Date de paiement :"+ DateChange.changerLaDate(reservation.getPaiement().getDatePaiement()));
    }
    public void readSms(String ref) {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        boolean verification=true;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                if (body.contains(ref)) {
                    Toast.makeText(context, "le code de reference exist dans votre message", Toast.LENGTH_SHORT).show();
                    ouvrirSmsAppavecMessage(ref);
                    verification=false;
                    break;
                }
            }
            if(verification)
                Toast.makeText(context, "le code de reference n'exist pas dans votre message", Toast.LENGTH_SHORT).show();
            cursor.close();
        }
    }

    private void ouvrirSmsAppavecMessage(String ref) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label", ref);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(context, "Le code de reference est copié dans votre presse-papiers", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }
    /*private void notifyUserAboutMessage(String messageBody) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "sms_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "SMS Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_bookmark_remove_24)
                .setContentTitle("Preuve sur le message")
                .setContentText("ouvrir le message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }*/
}
