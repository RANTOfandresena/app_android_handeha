package com.example.myapplication.outile;
import android.os.Parcel;
import android.os.Parcelable;

import org.mapsforge.core.model.LatLong;

public class ParcelableLatLong implements Parcelable {
    private LatLong latLong;

    public ParcelableLatLong(LatLong latLong) {
        this.latLong = latLong;
    }
    public static LatLong StringToLatLong(String s,String ss){
        return new LatLong(Double.parseDouble(s),Double.parseDouble(ss));
    }
    protected ParcelableLatLong(Parcel in) {
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        latLong = new LatLong(latitude, longitude);
    }

    public static final Creator<ParcelableLatLong> CREATOR = new Creator<ParcelableLatLong>() {
        @Override
        public ParcelableLatLong createFromParcel(Parcel in) {
            return new ParcelableLatLong(in);
        }

        @Override
        public ParcelableLatLong[] newArray(int size) {
            return new ParcelableLatLong[size];
        }
    };

    public LatLong getLatLong() {
        return latLong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latLong.latitude);
        dest.writeDouble(latLong.longitude);
    }
}

