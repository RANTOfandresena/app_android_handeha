package com.example.myapplication.apiService;
import com.example.myapplication.model.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Openrouteservice {
    @GET("v2/directions/driving-car")
    Call<RouteResponse> getRoute(
            @Query("start") String start,
            @Query("end") String end,
            @Query("api_key") String apiKey
    );
}
