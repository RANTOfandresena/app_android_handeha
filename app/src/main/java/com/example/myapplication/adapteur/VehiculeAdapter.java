package com.example.myapplication.adapteur;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.VehiculeModel;

import java.util.List;

public class VehiculeAdapter extends RecyclerView.Adapter<VehiculeAdapter.VehiculeViewHolder> {
    private List<VehiculeModel> mList;
    private OnItemClickListener listener;
    OnItemRetourClickListener listenerRetour;

    public VehiculeAdapter(List<VehiculeModel> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public VehiculeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vehicule, parent, false);
        return new VehiculeViewHolder(view,listener,listenerRetour);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculeAdapter.VehiculeViewHolder holder,int position) {
        VehiculeModel item = mList.get(position);
        try {
            //holder.imageView.setImageURI(item.getPhoto());
        } catch (Resources.NotFoundException e) {}
        holder.numeroVehiculeTextView.setText("Numero du voiture:"+item.getNumeroVehicule());
        holder.capaciteTextView.setText(String.valueOf("nombre de place :"+item.getCapacite()));
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class VehiculeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView numeroVehiculeTextView;
        public TextView capaciteTextView;
        public ImageButton editButton;
        public ImageButton deleteButton;

        public VehiculeViewHolder(View itemView,final OnItemClickListener listener,final OnItemRetourClickListener listenerRetour) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            numeroVehiculeTextView = itemView.findViewById(R.id.numero_vehicule);
            capaciteTextView = itemView.findViewById(R.id.capacite);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_suppr);
            editButton.setOnClickListener(v->{
                if(listener!=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
            deleteButton.setOnClickListener(v->{
                if(listenerRetour!=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listenerRetour.onItemRetourClick(position);
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
    public interface OnItemRetourClickListener {
        void onItemRetourClick(int position);
    }
    public void setOnItemRetourClickListener(OnItemRetourClickListener listenerRetour) {
        this.listenerRetour = listenerRetour;
    }
    public void ajoutVehiculeAdapter(VehiculeModel item) {
        mList.add(item);
        notifyItemInserted(mList.size() - 1);
    }
    public void updateVehiculeList(List<VehiculeModel> newVehiculeList) {
        mList.clear();
        mList.addAll(newVehiculeList);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
}