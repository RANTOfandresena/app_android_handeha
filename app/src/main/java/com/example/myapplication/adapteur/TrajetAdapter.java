package com.example.myapplication.adapteur;

import static android.content.Context.MODE_PRIVATE;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.fragment.CarteDialogFragment;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.RouteResponse;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.VehiculeModel;
import com.example.myapplication.outile.Algo;
import com.example.myapplication.outile.DateChange;
import com.example.myapplication.outile.ParcelableLatLong;

import org.mapsforge.core.model.LatLong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.TrajetViewHolder> {
    private List<TrajetModel> trajetList;
    private ApiService apiService;
    private List<ReservationModel> reservationList;
    private OnItemClickListener listener;
    private boolean isChauffer;
    private FragmentManager fragmentManager;
    public TrajetAdapter(List<TrajetModel> trajetList,boolean isChauffer,FragmentManager fragmentManager) {
        this.trajetList = trajetList;
        this.isChauffer=isChauffer;
        this.fragmentManager=fragmentManager;
    }
    public TrajetAdapter( boolean isChauffer,List<ReservationModel> reservationList) {
        this.reservationList = reservationList;
        this.isChauffer = isChauffer;
    }
    @NonNull
    @Override
    public TrajetAdapter.TrajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reservation, parent, false);
        return new TrajetViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrajetAdapter.TrajetViewHolder holder, int position) {
        if(trajetList!=null){
            TrajetModel trajet=trajetList.get(position);
            String lieuDeparts=trajet.getLieuDepart();
            String[] lieuDepart = lieuDeparts.split("\\|");
            holder.lieuDepart.setText(lieuDepart[0]);
            String lieuArrives=trajet.getLieuArrive();
            String[] lieuArrive = lieuArrives.split("\\|");
            holder.lieuArrive.setText(lieuArrive[0]);
            holder.horaire.setText("Depart : "+ DateChange.changerLaDate(trajet.getHoraire()));
            holder.placeLibre.setText("Place libre :"+ Algo.compterNumbre(trajet.getSiegeReserver(),0));
            holder.prix.setText(trajet.getPrix().replace(".00", "")+"Ar/personne");
        }else if(reservationList!=null){
            ReservationModel reservation = reservationList.get(position);
            String lieuDeparts=reservation.getTrajet().getLieuDepart();
            String[] lieuDepart = lieuDeparts.split("\\|");
            holder.lieuDepart.setText(lieuDepart[0]);
            String lieuArrives=reservation.getTrajet().getLieuArrive();
            String[] lieuArrive = lieuArrives.split("\\|");
            holder.lieuArrive.setText(lieuArrive[0]);
            holder.horaire.setText("Depart : " + DateChange.changerLaDate(reservation.getTrajet().getHoraire()));
            holder.placeLibre.setText("nombre de place reserver :" + String.valueOf(reservation.getSiegeNumero().size()));
            String strprix=reservation.getTrajet().getPrix();
            strprix = strprix.replace(".00", "");
            int montant=Integer.parseInt(strprix)*reservation.getSiegeNumero().size();
            holder.prix.setText("prix:"+String.valueOf(montant)+"ar");
        }

    }

    @Override
    public int getItemCount() {
        if(trajetList!=null){
            return trajetList.size();
        }else if(reservationList!=null){
            return reservationList.size();
        }else {
            return 0;
        }

    }

    public class TrajetViewHolder extends RecyclerView.ViewHolder{
        public TextView lieuDepart, lieuArrive, horaire, prix,placeLibre;
        public Button btn_reserver;
        public ImageButton voirCart;
        public TrajetViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            lieuDepart = itemView.findViewById(R.id.depart);
            lieuArrive = itemView.findViewById(R.id.arrive);
            horaire = itemView.findViewById(R.id.date);
            prix = itemView.findViewById(R.id.prix);
            placeLibre = itemView.findViewById(R.id.place_libre);
            btn_reserver=itemView.findViewById(R.id.reserver);
            voirCart=itemView.findViewById(R.id.carteVoir);
            if(isChauffer){
                btn_reserver.setText("Voir la list des passagers");
            }else if(trajetList==null){
                btn_reserver.setText("Voir le detail du reservation");
            }
            btn_reserver.setOnClickListener(view->{
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
            voirCart.setOnClickListener(v->{
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String lieuDeparts=trajetList.get(position).getLieuDepart();
                        String[] lieuDepart = lieuDeparts.split("\\|");

                        String lieuArrives=trajetList.get(position).getLieuArrive();
                        String[] lieuArrive = lieuArrives.split("\\|");

                        LatLong depart = ParcelableLatLong.StringToLatLong(lieuDepart[1],lieuDepart[2]);
                        LatLong arrive = ParcelableLatLong.StringToLatLong(lieuArrive[1], lieuArrive[2]);
                        boolean disableLongPress = true;
                        Toast.makeText(itemView.getContext(), "chargement...", Toast.LENGTH_SHORT).show();
                        get_route(position,depart,arrive,disableLongPress,itemView.getContext());
                    }
                }
            });
        }
    }
    public void ajoutTrajet(TrajetModel item) {
        trajetList.add(item);
        notifyItemInserted(trajetList.size() - 1);
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCartClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    private void get_route(int position,LatLong a, LatLong b,boolean disableLongPress, Context c){
        CarteDialogFragment dialogFragment;
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<RouteResponse> call = apiService.getRoute(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if(response.isSuccessful()){
                    List<LatLong> latLongs=response.body().conversionLatLong();
                    CarteDialogFragment dialogFragment = CarteDialogFragment.newInstance(latLongs, disableLongPress, new CarteDialogFragment.DialogListener() {
                        @Override public void retourLatLong(LatLong latLong, String nom, boolean depart) {
                            // Handle the returned data here
                        }});

                    dialogFragment.show(fragmentManager, "CarteDialogFragment");
                    listener.onCartClick(position);
                }else{
                    CarteDialogFragment dialogFragment = CarteDialogFragment.newInstance(a,b, disableLongPress, new CarteDialogFragment.DialogListener() {
                        @Override public void retourLatLong(LatLong latLong, String nom, boolean depart) {
                            // Handle the returned data here
                        }});
                    Toast.makeText(c, "Echec de connexion", Toast.LENGTH_SHORT).show();
                    dialogFragment.show(fragmentManager, "CarteDialogFragment");
                    listener.onCartClick(position);
                }
            }
            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                CarteDialogFragment dialogFragment = CarteDialogFragment.newInstance(a,b, disableLongPress, new CarteDialogFragment.DialogListener() {
                    @Override public void retourLatLong(LatLong latLong, String nom, boolean depart) {
                        // Handle the returned data here
                    }});
                Toast.makeText(c, "Echec de connexion", Toast.LENGTH_SHORT).show();
                dialogFragment.show(fragmentManager, "CarteDialogFragment");
                listener.onCartClick(position);
            }
        });
    }
}
