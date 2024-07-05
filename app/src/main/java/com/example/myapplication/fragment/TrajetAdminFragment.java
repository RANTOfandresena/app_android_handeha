package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.adapteur.TrajetAdapter;
import com.example.myapplication.allConstant.Calendrier;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.databinding.FragmentTrajetAdminBinding;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrajetAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrajetAdminFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentTrajetAdminBinding binding;
    private ApiService apiService;
    private View dialogView;
    private RecyclerView recyclerView;
    private TrajetAdapter adapter;
    private List<TrajetModel> trajetList=new ArrayList<>();
    private String mParam1;
    private String mParam2;
    TextInputEditText textInputEditText_horaire;
    Calendar calendar;
    private TrajetModel trajetModel;
    SharedPreferences sharedPreferences;

    public static TrajetAdminFragment newInstance(String param1, String param2) {
        TrajetAdminFragment fragment = new TrajetAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTrajetAdminBinding.inflate(inflater,container,false);
        getTrajetListApi();
        sharedPreferences = getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE);
        binding.ajoutTrajet.setOnClickListener(view->{
            insertionTrajetDialog();
        });
        return binding.getRoot();
    }
    public void insertDataAdapter(){
        recyclerView=binding.resultat;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new TrajetAdapter(trajetList,sharedPreferences.getBoolean("isLoggedIn", false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrajetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "ok ok", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getTrajetListApi(){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<List<TrajetModel>> getCall = apiService.getTrajet("horaire");
        getCall.enqueue(new Callback<List<TrajetModel>>() {
            @Override
            public void onResponse(Call<List<TrajetModel>> call, Response<List<TrajetModel>> response) {
                trajetList.clear();
                trajetList=response.body();
                insertDataAdapter();
            }

            @Override
            public void onFailure(Call<List<TrajetModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void insertionTrajetDialog(){
        ConstraintLayout trajet_dialog=binding.getRoot().findViewById(R.id.formulaire_trajet);
        dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.formulaire_trajet,trajet_dialog);
        Button okvalider=dialogView.findViewById(R.id.formulaire_trajet_valider);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        final AlertDialog alertDialog=builder.create();
        textInputEditText_horaire = dialogView.findViewById(R.id.horaire);
        textInputEditText_horaire.setOnClickListener(view->{
            calendar=Calendrier.afficheCalendrier(requireActivity(),textInputEditText_horaire,true);
        });
        okvalider.findViewById(R.id.formulaire_trajet_valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationFormAjoutTrajet()){
                    insertionTrajetApi(alertDialog);
                    //Toast.makeText(getActivity(), "Trajet cree avec succes", Toast.LENGTH_LONG).show();

                }
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private boolean validationFormAjoutTrajet(){
        TextInputEditText textInputEditText_lieuDepart = dialogView.findViewById(R.id.lieuDepart);
        String lieuDepart= textInputEditText_lieuDepart.getText().toString();

        TextInputEditText textInputEditText_lieuArrive = dialogView.findViewById(R.id.lieuArrive);
        String lieuArrive= textInputEditText_lieuArrive.getText().toString();


        textInputEditText_horaire = dialogView.findViewById(R.id.horaire);
        String horaire= textInputEditText_horaire.getText().toString();


        /*TextInputEditText textInputEditText_attribute = dialogView.findViewById(R.id.attribute);
        String attribute= textInputEditText_attribute.getText().toString();*/

        TextInputEditText textInputEditText_prix = dialogView.findViewById(R.id.prix);
        String prix= textInputEditText_prix.getText().toString();

        AppCompatSpinner appCompatSpinner_idvoiture = dialogView.findViewById(R.id.idVehicle);
        String selectedVehicle = (String) appCompatSpinner_idvoiture.getSelectedItem();
        if (lieuDepart.isEmpty()){
            textInputEditText_lieuDepart.setError("ce ci ne doit pas etre vide");
            return false;
        } else if (lieuArrive.isEmpty()) {
            textInputEditText_lieuDepart.setError(null);
            textInputEditText_lieuArrive.setError("ce ci ne doit pas etre vide");
            return false;
        } else if (horaire.isEmpty()) {
            textInputEditText_lieuArrive.setError(null);
            textInputEditText_horaire.setError("ce ci ne doit pas etre vide");
            return false;
        } else if (prix.isEmpty()) {
            textInputEditText_horaire.setError(null);
            textInputEditText_prix.setError("ce ci ne doit pas etre vide");
            return false;
        } else if (appCompatSpinner_idvoiture.getSelectedItemPosition() == 0) {
            //textInputEditText_attribute.setError(null);
            TextView errorText = (TextView)appCompatSpinner_idvoiture.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("ce ci ne doit pas etre vide");
            return false;
        }
        trajetModel=new TrajetModel(
                lieuDepart,
                lieuArrive,
                horaire,
                prix,
                1,
                sharedPreferences.getInt("idUser",0)
        );
        return true;
    }
    private void insertionTrajetApi(AlertDialog alertDialog){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<TrajetModel> postCall = apiService.insertionTrajetApi(trajetModel);
        postCall.enqueue(new Callback<TrajetModel>() {
            @Override
            public void onResponse(Call<TrajetModel> call, Response<TrajetModel> response) {
                trajetList.add(response.body());
                Toast.makeText(getActivity(), "Trajet cree avec succes", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TrajetModel> call, Throwable t) {
                Toast.makeText(getActivity(), "echec de connecxion", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }
}