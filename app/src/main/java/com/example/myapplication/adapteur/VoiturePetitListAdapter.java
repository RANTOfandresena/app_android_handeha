package com.example.myapplication.adapteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.VehiculeModel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoiturePetitListAdapter extends ArrayAdapter<VehiculeModel> {

    private Context context;
    private List<VehiculeModel> vehiculeList;

    public VoiturePetitListAdapter(@NonNull Context context, @NonNull List<VehiculeModel> vehiculeList) {
        super(context, 0, vehiculeList);
        this.context = context;
        this.vehiculeList = vehiculeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drop_down_item, parent, false);
        }

        VehiculeModel vehicule = vehiculeList.get(position);
        TextView numero_vehicule = convertView.findViewById(R.id.numero_vehicule);
        TextView capacite = convertView.findViewById(R.id.capacite);

        numero_vehicule.setText(vehicule.getNumeroVehicule());
        capacite.setText(String.valueOf(vehicule.getCapacite()));

        return convertView;
    }
}

