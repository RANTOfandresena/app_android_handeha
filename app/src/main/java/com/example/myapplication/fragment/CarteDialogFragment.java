package com.example.myapplication.fragment;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiClass.RetrofitClientOsm;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.AddressModel;
import com.example.myapplication.model.CoordonneApiNomVille;
import com.example.myapplication.model.RouteResponse;
import com.example.myapplication.outile.AngleRotation;
import com.example.myapplication.outile.ParcelableLatLong;
import com.example.myapplication.outile.TraceCarte;

import org.jetbrains.annotations.Nullable;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarteDialogFragment extends DialogFragment {
    private static final String ARG_DEPART = "arg_depart";
    private static final String ARG_ARRIVE = "arg_arrive";
    private static final String ARG_DISABLE_LONG_PRESS = "arg_disable_long_press";
    private static final String ARG_CHEMIN = "arg_chemin";
    private MapView mapViewDialog;
    private ApiService apiService;
    private TileCache tileCache;
    private LatLong depart;
    private AddressModel nomDepart;
    private LatLong arrive;
    private boolean disableLongPress;
    private AddressModel nomArriver;
    public boolean estDepart = true;
    private DialogListener listener;
    private TraceCarte traceCarte;
    private List<LatLong> chemin;

    public static CarteDialogFragment newInstance(DialogListener listener) {
        CarteDialogFragment fragment = new CarteDialogFragment();
        fragment.listener = listener;
        return fragment;
    }
    public static CarteDialogFragment newInstance(List<LatLong> chemin, boolean disableLongPress, DialogListener listener) {
        CarteDialogFragment fragment = new CarteDialogFragment();
        fragment.listener = listener;
        Bundle args = new Bundle();

        ArrayList<ParcelableLatLong> parcelableChemin = new ArrayList<>();
        for (LatLong point : chemin) {
            parcelableChemin.add(new ParcelableLatLong(point));
        }
        args.putParcelableArrayList(ARG_CHEMIN, parcelableChemin);

        args.putBoolean(ARG_DISABLE_LONG_PRESS, disableLongPress);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ParcelableLatLong departParcel = getArguments().getParcelable(ARG_DEPART);
            ParcelableLatLong arriveParcel = getArguments().getParcelable(ARG_ARRIVE);
            depart = departParcel != null ? departParcel.getLatLong() : null;
            arrive = arriveParcel != null ? arriveParcel.getLatLong() : null;
            disableLongPress = getArguments().getBoolean(ARG_DISABLE_LONG_PRESS);

            ArrayList<ParcelableLatLong> parcelableChemin = getArguments().getParcelableArrayList(ARG_CHEMIN);
            if (parcelableChemin != null) {
                chemin = new ArrayList<>();
                for (ParcelableLatLong parcelablePoint : parcelableChemin) {
                    chemin.add(parcelablePoint.getLatLong());
                }
            }
        }
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
                Log.d("CarteDialogFragment", "Long press detected at: " + tapLatLong);
                CarteDialogFragment.this.onLongPress(tapLatLong);
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        mapViewDialog.getLayerManager().getLayers().add(tileRendererLayer);

        mapViewDialog.setCenter(new LatLong(-18.766947, 48.869107));
        mapViewDialog.setZoomLevel((byte) 9);
        if (chemin != null) {
            mapViewDialog.setCenter(chemin.get(0));
            tracerChemain(chemin);
        }
        return rootView;
    }

    private void onLongPress(LatLong tapLatLong) {
        depart = tapLatLong;
        getNomVille(tapLatLong);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(onCreateView(getLayoutInflater(), null, savedInstanceState))
                .setPositiveButton("annuler", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }

    private void getNomVille(LatLong latLong) {
        Toast.makeText(getContext(), latLong.toString(), Toast.LENGTH_SHORT).show();
        Retrofit retrofit = RetrofitClientOsm.getClient("https://nominatim.openstreetmap.org/");
        ApiService nominatimApi = retrofit.create(ApiService.class);
        Call<CoordonneApiNomVille> call = nominatimApi.getNomVille("json", latLong.getLatitude(), latLong.getLongitude());
        call.enqueue(new Callback<CoordonneApiNomVille>() {
            @Override
            public void onResponse(Call<CoordonneApiNomVille> call, Response<CoordonneApiNomVille> response) {
                if (response.isSuccessful()) {
                    CoordonneApiNomVille responseModel = response.body();
                    if (responseModel != null) {
                        AddressModel address = responseModel.getAddress();
                        if (estDepart) {
                            depart = latLong;
                            setNomDepart(address);
                        } else {
                            arrive = latLong;
                            setNomArriver(address);
                        }
                        retourResultat(latLong, address.getFirstNonNullAttribute());
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
        void retourLatLong(LatLong latLong, String nom ,boolean depart);
    }

    private void retourResultat(LatLong latLong, String nom) {
        listener.retourLatLong(latLong, nom ,estDepart);
        dismiss();
    }
    private void get_route(LatLong a,LatLong b){
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<RouteResponse> call = apiService.getRoute(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if(response.isSuccessful()){
                    List<LatLong> latLongs=response.body().conversionLatLong();
                    tracerChemain(latLongs);
                }else{
                    Toast.makeText(getContext(), "Echec ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "echec", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void tracerChemain(List<LatLong> points){
        traceCarte=new TraceCarte(mapViewDialog.getLayerManager().getLayers());
        traceCarte.addLatLongs(points);
        traceCarte.tracerPolylines();
    }
}

