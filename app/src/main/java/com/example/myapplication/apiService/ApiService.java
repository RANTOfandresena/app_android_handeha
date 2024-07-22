package com.example.myapplication.apiService;

import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.RouteResponse;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.UtilisateurModel;
import com.example.myapplication.model.VehiculeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //trajet
    @GET("api/trajet/")
    Call<List<TrajetModel>> getTrajet(@Query("ordering") String ordering);
    @GET("api/trajet")
    Call<List<TrajetModel>> getTrajetRecherche(@Query("lieuArrive") String lieuArrive,
                                       @Query("lieuDepart") String lieuDepart,
                                       @Query("horaire") String horaire);

    @GET("api/trajet/")
    Call<List<TrajetModel>> getTrajetUser(@Query("ordering") String ordering, @Query("idUser") int idUser);
    @POST("api/trajet/")
    Call<TrajetModel> insertionTrajetApi(@Body TrajetModel trajetModel);

    //Utilisateur
    @POST("api/user/")
    Call<UtilisateurModel> setUtilisateur(@Body UtilisateurModel utilisateurModel);
    @GET("api/user/")
    Call<List<UtilisateurModel>> getUtilisateur(@Query("numero") String numero);
    @GET("api/user/{id}/")
    Call<UtilisateurModel> getUtilisateurId(@Path("id") int id);
    @POST("dj-rest-auth/login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("dj-rest-auth/logout/")
    Call<Void> logout();

    //crud vehicule
    @GET("api/vehicule/")
    Call<List<VehiculeModel>> getVehiculeId(@Query("idVehicule") String id);
    @GET("api/vehicule/")
    Call<List<VehiculeModel>> getVehiculeParUser(@Query("idUser") String id);
    @POST("api/vehicule/")
    Call<VehiculeModel> postVehicule(@Body VehiculeModel vehiculeModel);
    @PUT("api/vehicule/{idVehicule}/")
    Call<VehiculeModel> majVehicule(@Path("idVehicule") int vehiculeId, @Body VehiculeModel vehiculeModel);
    @DELETE("api/vehicule/{idVehicule}/")
    Call<Void> suppreVehicule(@Path("idVehicule") int vehiculeId);

    //reservation
    @GET("api/reservation/")
    Call<List<ReservationModel>> getReservation(@Query("idUser") String idUser);
    @GET("api/reservation/")
    Call<List<ReservationModel>> getReservationidTrajet(@Query("idTrajet") String idTrajet);
    @POST("api/reservation/")
    Call<ReservationModel> postReservation(@Body ReservationModel reservationModel);

    @GET("get_route/")
    Call<RouteResponse> getRoute(
            @Query("lat1") double lat1,
            @Query("lon1") double lon1,
            @Query("lat2") double lat2,
            @Query("lon2") double lon2
    );
    @POST("api/paiement/")
    Call<PaiementModel> postPaiement(@Body PaiementModel paiement);


}