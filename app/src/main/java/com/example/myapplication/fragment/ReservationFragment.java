package com.example.myapplication.fragment;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.adapteur.TrajetAdapter;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.databinding.FragmentReservationBinding;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.outile.UserManage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentReservationBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserManage user;
    private TrajetAdapter adapter;
    private ApiService apiService;
    private List<TrajetModel> listtrajet;
    private RecyclerView recyclerView;

    public ReservationFragment() {
        // Required empty public constructor
    }
    public static ReservationFragment newInstance(String param1, String param2) {
        ReservationFragment fragment = new ReservationFragment();
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
        binding=FragmentReservationBinding.inflate(inflater,container,false);
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        recyclerView=binding.resultat;
        user=new UserManage(getContext());
       // binding.resultat.setVisibility(View.GONE);
        binding.aucunReservation.setVisibility(View.GONE);
        binding.seConnect.setVisibility(View.GONE);
        listtrajet=new ArrayList<>();
        gestionAuth();
        return  binding.getRoot();
    }
    private void gestionAuth(){
        if(user.getUser()==null){
            binding.seConnect.setVisibility(View.VISIBLE);
            //binding.resultat.setVisibility(View.GONE);
            binding.aucunReservation.setVisibility(View.GONE);
        }else{
            binding.seConnect.setVisibility(View.GONE);
            Call<List<ReservationModel>> getPost=apiService.getReservation(user.getUser().getId());
            getPost.enqueue(new Callback<List<ReservationModel>>() {
                @Override
                public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                    if(response.isSuccessful()){
                        binding.seConnect.setVisibility(View.GONE);
                        if(response.body().size()==0){
                            binding.resultat.setVisibility(View.GONE);
                            binding.aucunReservation.setVisibility(View.VISIBLE);
                        }else {
                            binding.resultat.setVisibility(View.VISIBLE);
                            binding.aucunReservation.setVisibility(View.GONE);
                            obtientData(response.body());
                        }
                    }else {
                        Toast.makeText(getContext(), "une erreur se produit", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                    Toast.makeText(getContext(), "echec de connexion", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void obtientData(List<ReservationModel> reservationModelList) {
        adapter=new TrajetAdapter(false,reservationModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

}