package com.example.myapplication.outile;

import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.Join;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.Point;

public class PaintOutile {
    public static Paint paint = new Paint() {
        @Override
        public int getColor() {
            return 0;
        }

        @Override
        public float getStrokeWidth() {
            return 0;
        }

        @Override
        public int getTextHeight(String text) {
            return 0;
        }

        @Override
        public int getTextWidth(String text) {
            return 0;
        }

        @Override
        public boolean isTransparent() {
            return false;
        }

        @Override
        public void setBitmapShader(Bitmap bitmap) {

        }

        @Override
        public void setBitmapShaderShift(Point origin) {

        }

        @Override
        public void setColor(Color color) {

        }

        @Override
        public void setColor(int color) {

        }

        @Override
        public void setDashPathEffect(float[] strokeDasharray) {

        }

        @Override
        public void setStrokeCap(Cap cap) {

        }

        @Override
        public void setStrokeJoin(Join join) {

        }

        @Override
        public void setStrokeWidth(float strokeWidth) {

        }

        @Override
        public void setStyle(Style style) {

        }

        @Override
        public void setTextAlign(Align align) {

        }

        @Override
        public void setTextSize(float textSize) {

        }

        @Override
        public void setTypeface(FontFamily fontFamily, FontStyle fontStyle) {

        }
    };
}
