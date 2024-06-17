package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView voyageur;
    private View rootView;
    private TextView daty;
    private Calendar calendrier;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        daty=rootView.findViewById(R.id.date);
        calendrier = Calendar.getInstance();
        rootView.findViewById(R.id.btnMoins).setOnClickListener(view->{
            decremente(-1);
        });
        rootView.findViewById(R.id.btnPlus).setOnClickListener(View->{
            decremente(1);
        });

        daty.setOnClickListener(view->{
            afficheCalendrier();
        });


        return rootView;
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
    private void afficheCalendrier(){
        final Calendar currentDate=Calendar.getInstance();
        calendrier= Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendrier.set(year,month,dayOfMonth);
                daty.setText(android.text.format.DateFormat.format("yyyy-MM-dd",calendrier));
            }
        },currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }
}