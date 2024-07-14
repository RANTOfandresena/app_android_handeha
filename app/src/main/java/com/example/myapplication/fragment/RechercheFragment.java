package com.example.myapplication.fragment;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.myapplication.adapteur.ItemClickListener;
import com.example.myapplication.adapteur.PositionLieuAdapter;
import com.example.myapplication.R;
import com.example.myapplication.adapteur.TrajetAdapter;
import com.example.myapplication.bddsqlite.DatabaseHelper;
import com.example.myapplication.databinding.FragmentRechercheBinding;
import com.example.myapplication.model.PositionLieuModel;
import com.example.myapplication.model.VilleModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RechercheFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RechercheFragment extends Fragment  implements AdapterView.OnItemClickListener , ItemClickListener {
    private FragmentRechercheBinding binding;
    private DatabaseHelper dbHelper;

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
    ArrayList<VilleModel> positionLieuList;
    private OnCitySelectedListener mListener;

    @Override
    public void onButtonClick(VilleModel ville) {
        Toast.makeText(getContext(), ville.getNomVille(), Toast.LENGTH_SHORT).show();
        double latitude = Double.parseDouble(ville.getLat());
        double longitude = Double.parseDouble(ville.getLon());
        cityClicked(latitude, longitude);
    }

    public interface OnCitySelectedListener {
        void onCitySelected(double latitude, double longitude);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCitySelectedListener) {
            mListener = (OnCitySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCitySelectedListener");
        }
    }
    private void cityClicked(double latitude, double longitude) {
        if (mListener != null) {
            mListener.onCitySelected(latitude, longitude);
        }
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
        binding=FragmentRechercheBinding.inflate(inflater,container,false);
        dataBase();
        recyclerView=binding.listRecherche;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        positionLieuList = new ArrayList<>();
        adapter = new PositionLieuAdapter(positionLieuList,getContext(),this);
        recyclerView.setAdapter(adapter);
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
                //recherche(query);
                rechercheVille(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>2)
                    rechercheVille(newText);
                return false;
            }
        });
    }
    private void resultat(String a){
        Toast.makeText(requireContext(), a, Toast.LENGTH_SHORT).show();
    }

    private boolean initialisationBDD(Context context){
        try {
            InputStream inputStream=context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName=DatabaseHelper.DBLOCATION+DatabaseHelper.DBNAME;
            OutputStream outputStream=new FileOutputStream(outFileName);
            byte[]buff= new byte[1024];
            int length=0;
            while ((length=inputStream.read(buff))>0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "base de donne copir", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void rechercheVille(String nomVille) {
        List<VilleModel> villes = dbHelper.rechercheVille(nomVille);
        if (villes.isEmpty()) {
            Toast.makeText(getContext(), "Ville non trouvée", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(villes);
        }
    }
    private void dataBase(){
        dbHelper=new DatabaseHelper(getContext());
        File databse=getContext().getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false==databse.exists()){
            dbHelper.getReadableDatabase();
            if(initialisationBDD(getContext())){
                Toast.makeText(getContext(), "copie database", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "copie database erreur", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}