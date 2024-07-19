package com.example.myapplication.outile;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class AngleRotation {
    public static Bitmap getImageDirection(double degrees, Context context) {
        int index= (int) (degrees/22.6);
        if (index == -1 || index>361) {
            return null;
        } else {
            String resourceName = "d" + index;
            int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
            if (resourceId != 0) {
                Drawable drawable = context.getResources().getDrawable(resourceId, null);
                Bitmap bitmap=AndroidGraphicFactory.convertToBitmap(drawable);
                bitmap.scaleTo(60, 60);
                return bitmap;
            } else
                return null;
        }
    }
}
