package com.example.myapplication.outile;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

public class RotatedMarker extends Marker {

    private float rotation;

    public RotatedMarker(LatLong latLong, Drawable bitmap, int horizontalOffset, int verticalOffset, float rotation) {
        super(latLong, AndroidGraphicFactory.convertToBitmap(bitmap), horizontalOffset, verticalOffset);
        this.rotation = rotation;
    }



    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }
}
