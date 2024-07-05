package com.example.myapplication.adapteur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.PositionLieuModel;

import java.util.List;

public class PositionLieuAdapter extends RecyclerView.Adapter<PositionLieuAdapter.PositionLieuViewHolder>{
    private List<PositionLieuModel> listPositionLieuModel;
    public PositionLieuAdapter(List<PositionLieuModel> listPositionLieuModel) {
        this.listPositionLieuModel = listPositionLieuModel;
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
        PositionLieuModel positionLieuModel=listPositionLieuModel.get(position);
        holder.nom_lieu.setText(positionLieuModel.getName());

    }

    @Override
    public int getItemCount() {
        return listPositionLieuModel.size();
    }

    public static class PositionLieuViewHolder extends RecyclerView.ViewHolder {
        // Elements de votre vue
        public TextView nom_lieu;

        public PositionLieuViewHolder(View itemView) {
            super(itemView);
            nom_lieu = itemView.findViewById(R.id.lieu_recherche);
        }
    }
    public void filterList(List<PositionLieuModel> filteredList) {
        listPositionLieuModel = filteredList;
        notifyDataSetChanged();
    }
}
