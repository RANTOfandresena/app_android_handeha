package com.example.myapplication.adapteur;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.VehiculeModel;

import java.util.List;

public class VehiculeAdapter extends RecyclerView.Adapter<VehiculeAdapter.VehiculeViewHolder> {
    private List<VehiculeModel> mList;
    private Context mContext;

    public VehiculeAdapter(Context context, List<VehiculeModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public VehiculeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vehicule, parent, false);
        return new VehiculeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculeAdapter.VehiculeViewHolder holder, int position) {
        VehiculeModel item = mList.get(position);
        try {
            holder.imageView.setImageResource(item.getImage());
        } catch (Resources.NotFoundException e) {}
        holder.numeroVehiculeTextView.setText(item.getNumeroVehicule());
        holder.capaciteTextView.setText(String.valueOf(item.getCapacite()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to handle edit button click
                Toast.makeText(mContext, "Edit button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to handle delete button click
                Toast.makeText(mContext, "Delete button clicked", Toast.LENGTH_SHORT).show();
            }
        });
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

        public VehiculeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            numeroVehiculeTextView = itemView.findViewById(R.id.numero_vehicule);
            capaciteTextView = itemView.findViewById(R.id.email);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_suppr);
        }
    }
}