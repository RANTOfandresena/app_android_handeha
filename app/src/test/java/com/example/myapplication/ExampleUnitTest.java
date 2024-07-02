package com.example.myapplication;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.UtilisateurModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public  void testApi(){
        UtilisateurModel utilisateurModel=new UtilisateurModel("nomStr","prenomStr","123","123","123","123");

        ApiService apiService= RetrofitClient.getClient(URL_SERVER,null).create(ApiService.class);
        Call<UtilisateurModel> postCall = apiService.setUtilisateur(utilisateurModel);
        System.out.println("ggg");
        postCall.enqueue(new Callback<UtilisateurModel>() {

            @Override
            public void onResponse(Call<UtilisateurModel> call, Response<UtilisateurModel> response) {
                UtilisateurModel data = response.body();
                System.out.println("ok ok ok ok ok o k:"+data.getFirst_name());
            }

            @Override
            public void onFailure(Call<UtilisateurModel> call, Throwable t) {
                System.out.println("mmmmmmmmmm");

            }
        });
    }
}