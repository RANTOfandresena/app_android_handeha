package com.example.myapplication.model;

import java.util.List;

public class RouteResponse {
    public List<Feature> features;

    public static class Feature {
        public Geometry geometry;

        public static class Geometry {
            public List<List<Double>> coordinates;
        }
    }
}
