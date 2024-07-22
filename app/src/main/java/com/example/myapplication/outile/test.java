package com.example.myapplication.outile;

import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

public class test extends Marker {
    private float rotation;

    public test(LatLong latLong, Drawable bitmap, int horizontalOffset, int verticalOffset, float rotation) {
        super(latLong, AndroidGraphicFactory.convertToBitmap(bitmap), horizontalOffset, verticalOffset);
        this.rotation = rotation;
    }

    /**
     * @param latLong          the initial geographical coordinates of this marker (may be null).
     * @param bitmap           the initial {@code Bitmap} of this marker (may be null).
     * @param horizontalOffset the horizontal marker offset.
     * @param verticalOffset   the vertical marker offset.
     */
    public test(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
    }


    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }
}
