package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.apiService.NominatimApi;
import com.example.myapplication.apiClass.NominatimResponse;
import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.databinding.FragmentRechercheBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RechercheFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RechercheFragment extends Fragment {
    private FragmentRechercheBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    public RechercheFragment() {
        // Required empty public constructor
    }

    public static RechercheFragment newInstance(String param1, String param2) {
        RechercheFragment fragment = new RechercheFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_recherche, container, false);
        binding=FragmentRechercheBinding.inflate(inflater,container,false);
        recherche("gg");
        return binding.getRoot();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bar_recherche, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_action=menu.findItem(R.id.action_action);
        SearchView searchView= (SearchView) action_action.getActionView();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                resultat(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private void resultat(String a){
        Toast.makeText(requireContext(), a, Toast.LENGTH_SHORT).show();
    }
    private void recherche(String text){
        NominatimApi api = RetrofitClient.getClient("https://nominatim.openstreetmap.org/",null).create(NominatimApi.class);
        double latitude = 48.8566;
        double longitude = 2.3522;
        Call<NominatimResponse> call = api.getLocationName("json", latitude, longitude, 18, 1);
        call.enqueue(new Callback<NominatimResponse>() {
            @Override
            public void onResponse(Call<NominatimResponse> call, Response<NominatimResponse> response) {
                if (response.isSuccessful()) {
                    NominatimResponse nominatimResponse = response.body();
                    if (nominatimResponse != null) {
                        Log.d("MainActivity", "Lieu: " + nominatimResponse.getDisplayName());
                        Toast.makeText(getContext(), nominatimResponse.getDisplayName() , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NominatimResponse> call, Throwable t) {
                Log.e("MainActivity", "Erreur: " + t.getMessage());
            }
        });
    }
}