package com.example.myapplication.apiService;

import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/trajet/")
    Call<List<TrajetModel>> getTrajet(@Query("ordering") String ordering);
    @POST("api/trajet/")
    Call<TrajetModel> insertionTrajetApi(@Body TrajetModel trajetModel);
    @POST("api/user/")
    Call<UtilisateurModel> setUtilisateur(@Body UtilisateurModel utilisateurModel);

    @POST("dj-rest-auth/logout/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
