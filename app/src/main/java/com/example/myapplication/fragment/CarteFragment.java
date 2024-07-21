package com.example.myapplication.fragment;


import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.RouteResponse;
import com.example.myapplication.outile.AngleRotation;
import com.example.myapplication.outile.PaintOutile;
import com.example.myapplication.outile.TraceCarte;
import com.example.myapplication.outile.Utils;
import com.google.android.gms.location.LocationListener;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rotation.RotateView;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.core.graphics.Color ;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarteFragment extends Fragment implements LocationListener, SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private LocationManager locationManager;
    private RotateView rotateMarker ;
    private MainActivity activity;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public MapView mapView;
    private TileCache tileCache;
    private LayerManager layerManager;
    private ApiService apiService;
    private Marker marker;
    private MainActivity mainActivity;
    private float[] gravity;
    private float[] geomagnetic;
    private float azimuth;
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor, accelerometer, magnetometer;
    private float max=0;
    private AngleRotation angleRotation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Marker localisationMarker;

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
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        mapView = rootView.findViewById(R.id.cartee);
        mapView.setClickable(true);
        angleRotation=new AngleRotation();
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mainActivity= (MainActivity) getActivity();

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
            //activity.getLastLocation();
        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        localisation();
    }
    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        //mapView.onStartTemporaryDetach();
    }
    @Override
    public void onPause(){
        //mapView.onSt
        super.onPause();
        sensorManager.unregisterListener(this);
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
            //marker = createMarker(cityPosition);
            mapView.getLayerManager().getLayers().add(marker);
        }
    }
    private void localisationMarker(LatLong position) {
        Bitmap mfBitmap = AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.d4));
        localisationMarker = new Marker(position, mfBitmap, 0, 0);
        mapView.getLayerManager().getLayers().add(localisationMarker);
    }
    private void addMarker(LatLong position) {
        // Charger l'icône depuis les ressources drawable
        // Créer le marqueur
        Bitmap mfBitmap = AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.d4));
        mfBitmap.scaleTo(60, 60);
        localisationMarker = new Marker(position, mfBitmap, 0, 0);
        mapView.getLayerManager().getLayers().add(localisationMarker);
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
        layerManager = mapView.getLayerManager();
        if (marker != null) {
            layerManager.getLayers().remove(marker);
        }
        //createMarker(point);
        layerManager.getLayers().add(marker);
    }
    private void get_route(){
        Call<RouteResponse> call = apiService.getRoute(-19.833333, 47.016667, -18.916667, 47.533333);
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if(response.isSuccessful()){
                    ajoutMarker(new LatLong(-18.916667, 47.533333));
                    List<LatLong> latLongs=response.body().conversionLatLong();
                    TraceCarte traceCarte=new TraceCarte(mapView.getLayerManager().getLayers());
                    //traceCarte.tracerUnPolyLine(new LatLong(-18.916667, 47.533333),new LatLong(-18.216667, 47.733333));
                    //traceCarte.tracerUnPolyLine(new LatLong(-18.216667, 47.733333),new LatLong(-18.816667, 48.733333));
                    for (int i = 0; i < 10; i+=1) {
                        //traceCarte.tracerUnPolyLine(latLongs.get(i),latLongs.get(i+10));
                        //ajoutMarker(latLongs.get(i));
                    }
                    //traceCarte.addLatLongs(latLongs);
                    //traceCarte.tracerPolylines();
                    Toast.makeText(getContext(), "gg", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        } else {
            throw new IllegalStateException("MyFragment must be attached to MyActivity");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    /*private void addPolyline(List<LatLong> latLongs) {
        aint paint = Utils.createPaint(AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE), 80, Style.STROKE);
       Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
      polyline.getLatLongs().addAll(latLongs);
       mapView.getLayerManager().getLayers().add(polyline);
        addOverlayLayers(mapView.getLayerManager().getLayers(),latLongs);
    }
    protected void addOverlayLayers(Layers layers,List<LatLong> latL) {

        Polyline polyline = new Polyline(Utils.createPaint(
                AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE), 8,
                Style.STROKE), AndroidGraphicFactory.INSTANCE);
        List<LatLong> latLongs = polyline.getLatLongs();
        latLongs.addAll(latL);
        layers.add(polyline);
    }
    private void drawLine(LatLong pointA, LatLong pointB) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Style.STROKE);
        paint.setStrokeCap(Cap.ROUND);

        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        polyline.getLatLongs().addAll(Arrays.asList(pointA, pointB));
        mapView.getLayerManager().getLayers().add(polyline);
    }
    private void drawlie(List<LatLong> latLongs){
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Style.STROKE);
        paint.setStrokeCap(Cap.ROUND);
        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        for(int i=0 ;i!=latLongs.size()-1;i++){
            polyline.getLatLongs().addAll(Arrays.asList(latLongs.get(i), latLongs.get(i+1)));
            //polyline.setPoints(latLongs);
        }
        mapView.getLayerManager().getLayers().add(polyline);
    }*/
    private void localisation() {
        locationManager= (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            onGps();
        }else{
            getLocalisation();
        }

    }

    private void getLocalisation() {
        boolean a=true;
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },1);

        }else {
            Location locationGps=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetWorks=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            double lat = 0,lon = 0;
            if(locationGps!=null){
                lat=locationGps.getLatitude();
                lon=locationGps.getLongitude();
                
                Toast.makeText(getContext(), String.valueOf(lat)+"|gps|"+String.valueOf(lon), Toast.LENGTH_LONG).show();
            } else if (locationNetWorks!=null) {
                lat=locationNetWorks.getLatitude();
                lon=locationNetWorks.getLongitude();

                Toast.makeText(getContext(), String.valueOf(lat)+"|donne|"+String.valueOf(lon), Toast.LENGTH_LONG).show();
            } else if (LocationPassive!=null) {
                lat=LocationPassive.getLatitude();
                lon=LocationPassive.getLongitude();

                Toast.makeText(getContext(), String.valueOf(lat)+"|autre|"+String.valueOf(lon), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "impossible de localiser", Toast.LENGTH_LONG).show();
                addMarker(new LatLong(-18.766947,48.869107));
                a=false;
            }
            if(a)
                addMarker(new LatLong(-19.85587166666,47.02742499999));
        }
    }

    private void onGps() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("Active le GPS").setCancelable(false).setPositiveButton("oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values;
        if (gravity != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation contains: azimut, pitch and roll
                if (azimuth < 0) {
                    azimuth += 360;
                }
                if (localisationMarker != null) {
                    int index= (int) (azimuth/22.6);
                    if(max<azimuth)
                        max=azimuth;
                    String resourceName = "d" + index;

                    localisationMarker.setBitmap(AngleRotation.getImageDirection(azimuth,getContext()));
                    mainActivity.toolbar.setTitle(String.valueOf(max)+"||"+resourceName);
                    mapView.repaint();
                }
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}