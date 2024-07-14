package com.example.myapplication.adapteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.PositionLieuModel;
import com.example.myapplication.model.VilleModel;

import java.util.List;

public class PositionLieuAdapter extends RecyclerView.Adapter<PositionLieuAdapter.PositionLieuViewHolder>{

    private List<VilleModel> listPositionLieuModel;
    private Context context;
    private static  ItemClickListener itemClickListener;
    public PositionLieuAdapter(List<VilleModel> ville, Context context,ItemClickListener itemClickListener) {
        this.listPositionLieuModel = ville;
        this.context=context;
        this.itemClickListener=itemClickListener;
    }
    @NonNull
    @Override
    public PositionLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_rech_lieu, parent, false);
        return new PositionLieuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionLieuViewHolder holder, int position) {
        VilleModel positionLieuModel=listPositionLieuModel.get(position);
        /*holder.nom_lieu.setText(positionLieuModel.getNomVille());
        holder.nom_lieu.setOnClickListener(v->{
            Toast.makeText(context, "long:"+String.valueOf(positionLieuModel.getLon())+"  Lat:"+String.valueOf(positionLieuModel.getLat()), Toast.LENGTH_SHORT).show();
        });*/
        holder.bind(positionLieuModel,position);

    }


    @Override
    public int getItemCount() {
        return listPositionLieuModel.size();
    }

    public static class PositionLieuViewHolder extends RecyclerView.ViewHolder {
        public TextView nom_lieu;

        public PositionLieuViewHolder(View itemView) {
            super(itemView);
            nom_lieu = itemView.findViewById(R.id.lieu_recherche);
        }
        public void bind(final VilleModel ville, final int position){
            nom_lieu.setText(ville.getNomVille());
            itemView.setOnClickListener(v->itemClickListener.onButtonClick(ville));
        }
    }
    public void filterList(List<VilleModel> filteredList) {
        listPositionLieuModel = filteredList;
        notifyDataSetChanged();
    }
}
