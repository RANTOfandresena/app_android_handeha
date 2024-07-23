package com.example.myapplication.outile;


import com.example.myapplication.R;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class ChemainTrace {
    private Layers layers;
    private Bitmap bitmap;
    private List<Marker> markers;
    public ChemainTrace(Layers layers,Bitmap bitmap){
        this.bitmap=bitmap;
        this.markers=new ArrayList<>();
        this.layers=layers;
    }
    private void ajouterPoint(LatLong latLong){
        Marker marker = new Marker(latLong, bitmap, 0, 0);
        markers.add(marker);
        layers.add(marker);
    }
    public void supprPoint(){
        for(int i=0;i!=markers.size();i++){
            layers.remove(markers.get(i));
        }
        markers.clear();
    }
    public void tracerPoint(List<Marker> markerss){
        for(int i=0;i!=markerss.size();i++){
            layers.add(markerss.get(i));
        }
        this.markers=markerss;
    }
}
