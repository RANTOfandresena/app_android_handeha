package com.example.myapplication;


import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;
import static com.example.myapplication.outile.EncodeurTableauFixe.decodeString;
import static com.example.myapplication.outile.EncodeurTableauFixe.encodeArray;

import android.content.res.Resources;

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.RouteResponse;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.BitSet;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException, ParseException {
        for(int i=0;i!=390;i++){

            System.out.println("image="+String.valueOf(getImageDirection(i)));
        }
    }

    public int getImageDirection(double degrees) {
        int a= (int) (degrees/22.6);
        return a;
    }
}