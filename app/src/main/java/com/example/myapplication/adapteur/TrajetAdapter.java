package com.example.myapplication.adapteur;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.outile.DateChange;

import java.util.List;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.TrajetViewHolder> {
    private List<TrajetModel> trajetList;
    private OnItemClickListener listener;
    private boolean isChauffer;
    public TrajetAdapter(List<TrajetModel> trajetList,boolean isChauffer) {
        this.trajetList = trajetList;
        this.isChauffer=isChauffer;
    }

    @NonNull
    @Override
    public TrajetAdapter.TrajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reservation, parent, false);
        return new TrajetViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrajetAdapter.TrajetViewHolder holder, int position) {
        TrajetModel trajet=trajetList.get(position);
        holder.lieuDepart.setText(trajet.getLieuDepart());
        holder.lieuArrive.setText(trajet.getLieuArrive());
        holder.horaire.setText("Depart : "+ DateChange.changerLaDate(trajet.getHoraire()));
        holder.placeLibre.setText("Place libre :"+trajet.getAttribute());
        holder.prix.setText(trajet.getPrix()+"/personne");
    }

    @Override
    public int getItemCount() {
        return trajetList.size();
    }

    public class TrajetViewHolder extends RecyclerView.ViewHolder{
        public TextView lieuDepart, lieuArrive, horaire, prix,placeLibre;
        public Button btn_reserver;
        public TrajetViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            lieuDepart = itemView.findViewById(R.id.depart);
            lieuArrive = itemView.findViewById(R.id.arrive);
            horaire = itemView.findViewById(R.id.date);
            prix = itemView.findViewById(R.id.prix);
            placeLibre = itemView.findViewById(R.id.place_libre);
            btn_reserver=itemView.findViewById(R.id.reserver);
            if(isChauffer){
                btn_reserver.setText("Voir la list des passagers");
            }

            btn_reserver.setOnClickListener(view->{
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
