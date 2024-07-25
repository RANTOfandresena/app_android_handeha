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

/**
 * Classe TraceCarte pour tracer des polylines sur une carte avec Mapsforge.
 */
public class TraceCarte {
    private Paint paint;
    private Layers layers;
    private List<LatLong> latLongs = new ArrayList<>();

    /**
     * Constructeur pour initialiser les paramètres de traçage.
     * @param layers Les couches de la carte sur lesquelles tracer les polylines.
     */
    public TraceCarte(Layers layers) {
        this.layers = layers;
        this.paint = AndroidGraphicFactory.INSTANCE.createPaint();
        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(5); // Ajustez cette valeur selon vos besoins.
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeCap(Cap.ROUND);
    }

    /**
     * Méthode privée pour tracer une polyline entre deux points.
     * @param depart Le point de départ de la polyline.
     * @param arriver Le point d'arrivée de la polyline.
     */
    public void tracerUnPolyLine(LatLong depart, LatLong arriver) {
        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        polyline.getLatLongs().addAll(Arrays.asList(depart, arriver));
        layers.add(polyline);
    }

    /**
     * Ajoute une liste de coordonnées à tracer.
     * @param latLongs La liste des coordonnées (LatLong).
     */
    public void addLatLongs(List<LatLong> latLongs) {
        this.latLongs = latLongs;
    }

    /**
     * Trace des polylines entre chaque point de la liste de coordonnées.
     */
    public void tracerPolylines() {
        for (int i = 0; i < latLongs.size() - 20; i=i+10) {
            tracerUnPolyLine(latLongs.get(i), latLongs.get(i + 10));
        }
    }

    /**
     * Change la couleur de la polyline.
     * @param color La nouvelle couleur de la polyline.
     */
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    /**
     * Change la largeur de la polyline.
     * @param width La nouvelle largeur de la polyline.
     */
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
