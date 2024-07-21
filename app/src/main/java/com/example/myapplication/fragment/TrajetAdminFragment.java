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
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.ListPassagerActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.adapteur.TrajetAdapter;
import com.example.myapplication.adapteur.VoiturePetitListAdapter;
import com.example.myapplication.allConstant.Calendrier;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.bddsqlite.ConnectBddSqlite;
import com.example.myapplication.bddsqlite.database.AppDatabase;
import com.example.myapplication.databinding.FragmentTrajetAdminBinding;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.model.VehiculeModel;
import com.example.myapplication.outile.UserManage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private AppDatabase bddsqlite;
    private ApiService apiService;
    private AppDatabase bdsqlite;
    private View dialogView;
    private RecyclerView recyclerView;
    private TrajetAdapter adapter;
    private List<TrajetModel> trajetList=new ArrayList<>();
    private VehiculeModel selectionnerVehicule;
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView autoCompleteTextView;
    TextInputEditText textInputEditText_horaire;
    Calendar calendar;
    private TrajetModel trajetModel;
    private UserManage userManage;
    SharedPreferences sharedPreferences;
    private UtilisateurModel user;

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
        bddsqlite=ConnectBddSqlite.connectBdd(getContext());
        userManage=new UserManage(getContext());
        user=userManage.getUser();
        getTrajetListApi();
        binding.ajoutTrajet.setOnClickListener(view->{
            insertionTrajetDialog();
        });
        bdsqlite= ConnectBddSqlite.connectBdd(getContext());
        return binding.getRoot();
    }
    public void insertDataAdapter(){
        recyclerView=binding.resultat;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        boolean accessAdmin=user!=null && user.isEst_conducteur();
        adapter=new TrajetAdapter(trajetList,accessAdmin);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrajetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getActivity(), ListPassagerActivity.class);
                intent.putExtra("idTrajet",String.valueOf(trajetList.get(position).getIdTrajet()));
                startActivity(intent);
            }
        });
    }
    public void getTrajetListApi(){
        UtilisateurModel userr= new UserManage(getContext()).getUser();
        if(userr!=null && user.isEst_conducteur()){
            apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
            Call<List<TrajetModel>> getCall = apiService.getTrajetUser("horaire",Integer.parseInt(user.getId()));
            getCall.enqueue(new Callback<List<TrajetModel>>() {
                @Override
                public void onResponse(Call<List<TrajetModel>> call, Response<List<TrajetModel>> response) {
                    trajetList.clear();
                    trajetList=response.body();
                    insertDataAdapter();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            bddsqlite.trajetDao().viderTable();
                            bddsqlite.trajetDao().insertTrajets(response.body());
                        }
                    });
                }
                @Override
                public void onFailure(Call<List<TrajetModel>> call, Throwable t) {
                    Toast.makeText(getActivity(), "echec de connexion", Toast.LENGTH_SHORT).show();

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            trajetList.clear();
                            trajetList=bddsqlite.trajetDao().getAllTrajets();
                            if(!trajetList.isEmpty()){
                                insertDataAdapter();
                                Toast.makeText(getActivity(), "ce sont des donne hors ligne ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        }
    }
    private void insertionTrajetDialog(){
        ConstraintLayout trajet_dialog=binding.getRoot().findViewById(R.id.formulaire_trajet);
        dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.formulaire_trajet,trajet_dialog);
        Button okvalider=dialogView.findViewById(R.id.formulaire_trajet_valider);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        autoCompleteTextView=dialogView.findViewById(R.id.idVehicle);
        final AlertDialog alertDialog=builder.create();
        textInputEditText_horaire = dialogView.findViewById(R.id.horaire);
        listVoiture();
        textInputEditText_horaire.setOnClickListener(view->{
            calendar=Calendrier.afficheCalendrier(requireActivity(),textInputEditText_horaire,true);
        });
        okvalider.findViewById(R.id.formulaire_trajet_valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationFormAjoutTrajet()){
                    insertionTrajetApi(alertDialog);
                    //Toast.makeText(getActivity(), String.valueOf(selectionnerVehicule.getIdVehicule()), Toast.LENGTH_SHORT).show();

                }
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCanceledOnTouchOutside(false);
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
        } else if (selectionnerVehicule==null) {
            autoCompleteTextView.setError("choisir une voiture");
            return false;
        }
        autoCompleteTextView.setError(null);
        trajetModel=new TrajetModel(
                lieuDepart,
                lieuArrive,
                horaire,
                prix,
                selectionnerVehicule.getIdVehicule(),
                Integer.parseInt(userManage.getUser().getId())
        );
        return true;
    }
    private void insertionTrajetApi(AlertDialog alertDialog){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Toast.makeText(getActivity(), String.valueOf(trajetModel.getIdUser()), Toast.LENGTH_SHORT).show();
        Call<TrajetModel> postCall = apiService.insertionTrajetApi(trajetModel);
        postCall.enqueue(new Callback<TrajetModel>() {
            @Override
            public void onResponse(Call<TrajetModel> call, Response<TrajetModel> response) {
                if(response.isSuccessful()){
                    //trajetList.add(response.body());
                    adapter.ajoutTrajet(response.body());
                    Toast.makeText(getActivity(), "Trajet cree avec succes", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        Toast.makeText(getContext(), "Erreur: " + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<TrajetModel> call, Throwable t) {
                Toast.makeText(getActivity(), "echec de connecxion", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }
    private void listVoiture(){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<List<VehiculeModel>> getCall = apiService.getVehiculeParUser(user.getId());
        getCall.enqueue(new Callback<List<VehiculeModel>>() {
            @Override
            public void onResponse(Call<List<VehiculeModel>> call, Response<List<VehiculeModel>> response) {
                VoiturePetitListAdapter adapterType=new VoiturePetitListAdapter(getContext(),response.body());
                autoCompleteTextView=dialogView.findViewById(R.id.idVehicle);
                autoCompleteTextView.setAdapter(adapterType);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectionnerVehicule = (VehiculeModel) parent.getItemAtPosition(position);

                        // Afficher l'ID du véhicule dans un Toast
                        //Toast.makeText(getActivity(), "ID du véhicule: " + selectionnerVehicule.getIdVehicule(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(Call<List<VehiculeModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });


    }
}