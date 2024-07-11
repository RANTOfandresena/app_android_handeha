package com.example.myapplication.adapteur;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.UtilisateurModel;

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
        holder.prix.setText("prix total :"+prix_total);
        holder.nom.setText("Nom :"+reservationModel1.getUtilisateurReserver().getFirst_name());
        holder.prenom.setText("Prenom :"+ reservationModel1.getUtilisateurReserver().getLast_name());
        holder.numero.setText("Numero :"+reservationModel1.getUtilisateurReserver().getNumero());
        holder.nb_place.setText("nombre de place reserver :"+String.valueOf(reservationModel1.getSiegeNumero().size()));
    }
    @Override
    public int getItemCount() {
        return reservationModel.size();
    }
    public static class ReservationUtilisateurViewHolder extends RecyclerView.ViewHolder {

        TextView nom,prenom,numero,prix,nb_place;

        public ReservationUtilisateurViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.prenom);
            numero = itemView.findViewById(R.id.numero);
            prix = itemView.findViewById(R.id.prix);
            nb_place = itemView.findViewById(R.id.nb_place);
        }
    }
}
