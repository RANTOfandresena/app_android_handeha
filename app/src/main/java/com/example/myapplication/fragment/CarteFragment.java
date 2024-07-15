package com.example.myapplication.fragment;


import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.RouteResponse;
import com.example.myapplication.outile.PaintOutile;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.core.graphics.Color;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MapView mapView;
    private TileCache tileCache;
    private LayerManager layerManager;
    private ApiService apiService;
    private Marker marker;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarteFragment newInstance(String param1, String param2) {
        CarteFragment fragment = new CarteFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_carte, container, false);
        apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);

        AndroidGraphicFactory.createInstance(getActivity().getApplication());

        mapView = rootView.findViewById(R.id.cartee);
        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);

        tileCache = AndroidUtil.createTileCache(
                getActivity(),
                "mapcache",
                mapView.getModel().displayModel.getTileSize(),
                1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor()
        );
        MapDataStore mapDataStore = new MapFile(
                new File(getActivity().getExternalFilesDir(null),
                        "madagascar.map")
        );
        TileRendererLayer tileRendererLayer = new TileRendererLayer(
                tileCache,
                mapDataStore,
                mapView.getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE
        ) {
            @Override
            public boolean onLongPress(LatLong tapLatLong, Point thisXY, Point tapXY) {
                // Appel de la méthode onLongPress avec la position de la pression longue
                CarteFragment.this.onLongPress(tapLatLong);
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        mapView.setCenter(new LatLong(-18.766947,48.869107));
        mapView.setZoomLevel((byte) 9);
        rootView.findViewById(R.id.position).setOnClickListener(v -> {
            get_route();
        });
        return rootView;
    }
    protected void onLongPress(final LatLong position) {
        //
    }
    @Override
    public void onResume(){
        super.onResume();
        //mapView.onStartTemporaryDetach();
    }

    @Override
    public void onPause(){
        //mapView.onSt
        super.onPause();
    }
    @Override
    public void onDestroy(){
        mapView.destroyAll();
        //AndroidGraphicFactory.clearResourceFileCache();
        super.onDestroy();
    }
    public void parametreArgument(){
        if(getArguments() != null){
            double latitude = getArguments().getDouble("lat");
            double longitude = getArguments().getDouble("lon");

            // Mettre à jour la carte avec la position de la ville sélectionnée
            LatLong cityPosition = new LatLong(latitude, longitude);
            mapView.setCenter(cityPosition);
            mapView.setZoomLevel((byte) 15); // Ajuster le niveau de zoom si nécessaire

            // Ajouter un marqueur pour la ville
            Marker marker = createMarker(cityPosition);
            mapView.getLayerManager().getLayers().add(marker);
        }
    }
    private Marker createMarker(LatLong position) {
        // Créer et configurer le marqueur ici
        Drawable drawable = getResources().getDrawable(R.drawable.baseline_add_location_24);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.scaleTo(50, 50); // Adapter la taille de l'icône si nécessaire

        Marker marker = new Marker(position, bitmap, 0, 0);
        return marker;
    }
    public void updateMapLocation(double latitude, double longitude) {
        LatLong point = new LatLong(latitude, longitude);
        layerManager = mapView.getLayerManager();
        if (marker != null) {
            layerManager.getLayers().remove(marker);
        }
        ajoutMarker(point);
        mapView.setCenter(point);
    }
    public void ajoutMarker(LatLong point) {
        if (layerManager != null) {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_add_location_24);
            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
            Marker marker = new Marker(point, bitmap, 0, -bitmap.getHeight() / 2);
            layerManager.getLayers().add(marker);
        } else {
            throw new IllegalStateException("LayerManager n'est pas initialisé.");
        }
    }
    private void get_route(){
        Call<RouteResponse> call = apiService.getRoute(-19.833333, 47.016667, -18.916667, 47.533333);
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                List<LatLong> latLongs=response.body().conversionLatLong();
                Paint paint = PaintOutile.paint;
                paint.setColor(AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE));
                paint.setStrokeWidth(5);
                Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
                polyline.getLatLongs().addAll(latLongs);
                mapView.getLayerManager().getLayers().add(polyline);
                Toast.makeText(getContext(), "gg", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "echec", Toast.LENGTH_SHORT).show();
            }
        });
    }

}