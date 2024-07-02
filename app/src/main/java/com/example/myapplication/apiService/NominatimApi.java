package com.example.myapplication.apiService;

import com.example.myapplication.apiClass.NominatimResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimApi {
    @GET("reverse")
    Call<NominatimResponse> getLocationName(
            @Query("format") String format,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("zoom") int zoom,
            @Query("addressdetails") int addressDetails
    );
}
