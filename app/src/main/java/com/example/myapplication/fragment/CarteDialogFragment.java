package com.example.myapplication.fragment;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClientOsm;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.AddressModel;
import com.example.myapplication.model.CoordonneApiNomVille;

import org.jetbrains.annotations.Nullable;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarteDialogFragment extends DialogFragment {
    public MapView mapViewDialog;
    private LayerManager layerManager;
    private TileCache tileCache;
    private LatLong depart;
    private Marker marker;
    private AddressModel nomDepart;
    private LatLong arrive;
    private AddressModel nomArriver;
    public boolean estDepart=true;
    private DialogListener listener;
    public static CarteDialogFragment newInstance(DialogListener listener) {
        CarteDialogFragment fragment = new CarteDialogFragment();
        fragment.listener = listener;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_carte_dialog, container, false);
        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        mapViewDialog = rootView.findViewById(R.id.mapView);
        mapViewDialog.setClickable(true);
        mapViewDialog.getMapScaleBar().setVisible(true);
        mapViewDialog.setBuiltInZoomControls(true);

        tileCache = AndroidUtil.createTileCache(
                getActivity(),
                "mapcache",
                mapViewDialog.getModel().displayModel.getTileSize(),
                1f,
                mapViewDialog.getModel().frameBufferModel.getOverdrawFactor()
        );
        MapDataStore mapDataStore = new MapFile(
                new File(getActivity().getExternalFilesDir(null),
                        "madagascar.map")
        );
        TileRendererLayer tileRendererLayer = new TileRendererLayer(
                tileCache,
                mapDataStore,
                mapViewDialog.getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE
        ) {
            @Override
            public boolean onLongPress(LatLong tapLatLong, Point thisXY, Point tapXY) {
                CarteDialogFragment.this.onLongPress(tapLatLong);
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        mapViewDialog.getLayerManager().getLayers().add(tileRendererLayer);

        mapViewDialog.setCenter(new LatLong(-18.766947,48.869107));
        mapViewDialog.setZoomLevel((byte) 9);
        return rootView;
    }

    private void onLongPress(LatLong tapLatLong) {
        depart=tapLatLong;
        getNomVille(tapLatLong);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(onCreateView(getLayoutInflater(), null, savedInstanceState))
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
        if (savedInstanceState == null) {
            Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CarteFragment())
                    .commit();
        }
    }*/
    private void getNomVille(LatLong latLong){
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        Retrofit retrofit=RetrofitClientOsm.getClient("https://nominatim.openstreetmap.org/");
        ApiService nominatimApi = retrofit.create(ApiService.class);
        Toast.makeText(getContext(), "chargement...", Toast.LENGTH_SHORT).show();

        Call<CoordonneApiNomVille> call = nominatimApi.getNomVille("json",latLong.getLatitude() , latLong.getLongitude());
        call.enqueue(new Callback<CoordonneApiNomVille>() {
            @Override
            public void onResponse(Call<CoordonneApiNomVille> call, Response<CoordonneApiNomVille> response) {
                if (response.isSuccessful()) {
                    CoordonneApiNomVille responseModel = response.body();
                    if (responseModel != null) {
                        AddressModel address = responseModel.getAddress();
                        if(estDepart){
                            depart=latLong;
                            setNomDepart(address);
                        }else{
                            arrive=latLong;
                            setNomArriver(address);
                        }
                        ajoutMarker(latLong);
                        retourResultat(latLong,address.getVillage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CoordonneApiNomVille> call, Throwable t) {
                Toast.makeText(getContext(), "echec de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LatLong getDepart() {
        return depart;
    }

    public void setDepart(LatLong depart) {
        this.depart = depart;
    }

    public AddressModel getNomDepart() {
        return nomDepart;
    }

    public void setNomDepart(AddressModel nomDepart) {
        this.nomDepart = nomDepart;
    }

    public LatLong getArrive() {
        return arrive;
    }

    public void setArrive(LatLong arrive) {
        this.arrive = arrive;
    }

    public AddressModel getNomArriver() {
        return nomArriver;
    }

    public void setNomArriver(AddressModel nomArriver) {
        this.nomArriver = nomArriver;
    }
    public interface DialogListener {
        void retourLatLong(LatLong latLong,String nom);
    }
    private void retourResultat(LatLong latLong,String nom) {
            listener.retourLatLong(latLong, nom);
    }
    public void ajoutMarker(LatLong point) {
        /*Bitmap mfBitmap = AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.d4));
        mfBitmap.scaleTo(60, 60);
        mapView.setZoomLevel((byte) 17);
        Marker localisationMarker = new Marker(point, mfBitmap, 0, 0);
        mapView.getLayerManager().getLayers().add(localisationMarker);*/
        Drawable drawable = getResources().getDrawable(R.drawable.d4);
        Bitmap mfBitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        mfBitmap.scaleTo(60, 60);

        Marker localisationMarker = new Marker(point, mfBitmap, 0, 0);
        Toast.makeText(getContext(), point.toString(), Toast.LENGTH_SHORT).show();

        mapViewDialog.getLayerManager().getLayers().add(localisationMarker);
        // Ensure the map view is updated
        mapViewDialog.getLayerManager().redrawLayers();
    }
}
