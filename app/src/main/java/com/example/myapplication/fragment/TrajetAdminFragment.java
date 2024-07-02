package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.TrajetActivity;
import com.example.myapplication.adapteur.TrajetAdapter;
import com.example.myapplication.databinding.FragmentTrajetAdminBinding;
import com.example.myapplication.model.TrajetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrajetAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrajetAdminFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentTrajetAdminBinding binding;
    private RecyclerView recyclerView;
    private TrajetAdapter adapter;
    private List<TrajetModel> trajetList=new ArrayList<>();
    private String mParam1;
    private String mParam2;

    public TrajetAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrajetAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        getData();
        binding.ajoutTrajet.setOnClickListener(view->{

        });
        return binding.getRoot();
    }
    public void getData(){
        recyclerView=binding.resultat;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        trajetList.add(new TrajetModel(1, "Antananarivo", "Antsirabe", "2024-07-01T12:02:00Z", "1000.00", "10", 1, 1));
        trajetList.add(new TrajetModel(2, "antanifotsy", "Antsirabe", "2024-08-01T12:02:00Z", "100.00", "17", 1, 1));
        trajetList.add(new TrajetModel(3, "Antanifotsy", "Antananarivo", "2024-07-03T11:01:00Z", "6000.00", "9", 1, 1));
        trajetList.add(new TrajetModel(4, "antanifotsy", "Antsirabe", "2024-08-01T12:02:00Z", "100.00", "17", 1, 1));
        trajetList.add(new TrajetModel(5, "antanifotsy", "Antsirabe", "2024-08-01T12:02:00Z", "100.00", "17", 1, 1));

        adapter=new TrajetAdapter(trajetList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrajetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "ok ok", Toast.LENGTH_SHORT).show();
            }
        });
    }
}