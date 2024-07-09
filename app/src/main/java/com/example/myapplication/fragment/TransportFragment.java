package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.adapteur.TrajetAdapter;
import com.example.myapplication.allConstant.Calendrier;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.outile.UserManage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransportFragment extends Fragment {
    OkHttpClient client;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ToolbarVisibilityListener toolbarVisibilityListener;

    private TextView voyageur,json;
    private View rootView;
    private TextInputEditText daty;
    private Calendar calendrier;
    private RecyclerView recyclerView;
    private TrajetAdapter adapter;
    private List<TrajetModel> trajetList;
    private UserManage userManage;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;
    private ApiService apiService;

    public TransportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransportFragment newInstance(String param1, String param2) {
        TransportFragment fragment = new TransportFragment();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_transport, container, false);
        sharedPreferences = getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE);
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        client=new OkHttpClient();
        daty=rootView.findViewById(R.id.date);
        calendrier = Calendar.getInstance();
        recyclerView=rootView.findViewById(R.id.resultat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trajetList=new ArrayList<>();
        userManage=new UserManage(getContext());

        rootView.findViewById(R.id.btnMoins).setOnClickListener(view->{
            decremente(-1);
        });
        rootView.findViewById(R.id.btnPlus).setOnClickListener(View->{
            decremente(1);
        });

        daty.setOnClickListener(view->{
            calendrier=Calendrier.afficheCalendrier(requireActivity(),daty,false);
        });

        rootView.findViewById(R.id.go).setOnClickListener(view->{
            getTrajetApi();
        });


        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ToolbarVisibilityListener){
            toolbarVisibilityListener= (ToolbarVisibilityListener) context;
            toolbarVisibilityListener.cacherToolbar();
        }else {
            throw new RuntimeException(context.toString()+"must implement ToolbarVisibilityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbarVisibilityListener.afficherToolbar();
        toolbarVisibilityListener=null;
    }

    private void getTrajet() {
        UtilisateurModel user=userManage.getUser();
        boolean accessAdmin=user!=null && user.isEst_conducteur();
        adapter=new TrajetAdapter(trajetList,accessAdmin);
        adapter.setOnItemClickListener(new TrajetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(user!=null){
                    Intent intent=new Intent(getContext(), TrajetActivity.class);
                    intent.putExtra("data",trajetList.get(position));
                    voyageur=rootView.findViewById(R.id.voyageur);
                    intent.putExtra("nbplace",voyageur.getText().toString());
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void getTrajetApi(){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        retrofit2.Call<List<TrajetModel>> getCall = apiService.getTrajet("horaire");
        getCall.enqueue(new Callback<List<TrajetModel>>() {
            @Override
            public void onResponse(retrofit2.Call<List<TrajetModel>> call, Response<List<TrajetModel>> response) {
                trajetList=response.body();
                getTrajet();
            }

            @Override
            public void onFailure(Call<List<TrajetModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void decremente(int nombre ){
        voyageur=rootView.findViewById(R.id.voyageur);
        String nb=voyageur.getText().toString();

        if(!TextUtils.isEmpty(nb)){
            int resultat=Integer.parseInt(nb)+nombre;
            if(resultat>=1 && resultat<40)
                voyageur.setText(String.valueOf(resultat));
        }
    }
    private void afficherPlace(int[][] places, GridLayout gridLayout) {
        /*int rowCount = places.length;
        int colCount = 0;
        for (int[] row : places) {
            if (row.length > colCount) {
                colCount = row.length;
            }
        }
        gridLayout.setColumnCount(colCount);
        gridLayout.setRowCount(rowCount);

        // Générer les boutons en fonction du tableau de données
        int placeNumber = 1;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < places[i].length; j++) {
                Button button = new Button(getActivity());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 150;
                params.height = 150;
                params.setMargins(8, 8, 8, 8);
                params.columnSpec = GridLayout.spec(j);
                params.rowSpec = GridLayout.spec(i);
                button.setLayoutParams(params);
                button.setText(String.valueOf(placeNumber));
                placeNumber++;

                button.setBackgroundResource(R.drawable.button_stylee);

                // Définir la couleur de fond en fonction de la valeur du tableau
                if (places[i][j] == 0) {
                    button.setBackgroundColor(Color.YELLOW);
                } else if (places[i][j] == 1) {
                    button.setBackgroundColor(Color.GRAY);
                } else if (places[i][j] == 2) {
                    button.setBackgroundColor(Color.BLUE);
                }

                // Ajouter le bouton au GridLayout
                gridLayout.addView(button);
            }
        }*/
    }

    public interface ToolbarVisibilityListener{
        void cacherToolbar();
        void afficherToolbar();
    }
}