package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.adapteur.PositionLieuAdapter;
import com.example.myapplication.apiService.NominatimApi;
import com.example.myapplication.apiClass.NominatimResponse;
import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.databinding.FragmentRechercheBinding;
import com.example.myapplication.model.PositionLieuModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView recyclerView;
    private PositionLieuAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    ArrayList<PositionLieuModel> positionLieuList;

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
        recyclerView=binding.listRecherche;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        positionLieuList = new ArrayList<>();
        adapter = new PositionLieuAdapter(positionLieuList);
        lire_csv();
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
                recherche(query);
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
    private void lire_csv(){
        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput("madagascar.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    positionLieuList.add(new PositionLieuModel(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), parts[3]));
                }
            }
            reader.close();
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void recherche(String nomLieu){
        List<PositionLieuModel> pos_list = new ArrayList<>();
        for (PositionLieuModel item : positionLieuList) {
            if (item.getName().toLowerCase().contains(nomLieu.toLowerCase())) {
                pos_list.add(item);
            }
        }
        adapter.filterList(pos_list);
    }
}