package com.example.myapplication.outile;

import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.overlay.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TraceCarte {
    private Paint paint;
    private Layers layers;
    private List<LatLong> latLongs = new ArrayList<>();


    public TraceCarte(Layers layers) {
        this.layers = layers;
        this.paint = AndroidGraphicFactory.INSTANCE.createPaint();
        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(5); // Ajustez cette valeur selon vos besoins.
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeCap(Cap.ROUND);
    }


    public void tracerUnPolyLine(LatLong depart, LatLong arriver) {
        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        polyline.getLatLongs().addAll(Arrays.asList(depart, arriver));
        layers.add(polyline);
    }


    public void addLatLongs(List<LatLong> latLongs) {
        this.latLongs = latLongs;
    }


    public void tracerPolylines() {
        for (int i = 0; i < latLongs.size() - 20; i=i+10) {
            tracerUnPolyLine(latLongs.get(i), latLongs.get(i + 10));
        }
    }


    public void setColor(int color) {
        this.paint.setColor(color);
    }


    public void setStrokeWidth(float width) {
        this.paint.setStrokeWidth(width);
    }
    public void supprimerToutesLesPolylines() {
        List<Layer> layersToRemove = new ArrayList<>();
        for (Layer layer : layers) {
            if (layer instanceof Polyline) {
                layersToRemove.add(layer);
            }
        }
        layers.removeAll(layersToRemove);
    }
}
