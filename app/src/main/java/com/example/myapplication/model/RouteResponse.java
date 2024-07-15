package com.example.myapplication.model;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;
import java.util.List;

public class RouteResponse {
    private List<List<Double>> route_coords;

    public RouteResponse(List<List<Double>> route_coords) {
        this.route_coords = route_coords;
    }

    public void setRoute_coords(List<List<Double>> route_coords) {
        this.route_coords = route_coords;
    }

    public List<List<Double>> getRouteCoords() {
        return route_coords;
    }
    public List<LatLong> conversionLatLong(){
        List<LatLong> latLongs = new ArrayList<>();
        for (List<Double> coordonnee : route_coords) {
            latLongs.add(new LatLong(coordonnee.get(0), coordonnee.get(1)));
        }
        return latLongs;
    }
}
