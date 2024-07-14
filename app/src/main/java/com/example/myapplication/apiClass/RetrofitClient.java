package com.example.myapplication.apiClass;
import static com.example.myapplication.allConstant.Allconstant.ROUTE_URL;

import com.example.myapplication.apiService.Openrouteservice;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl, String authToken) {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.addInterceptor(loggingInterceptor);

            if (authToken != null) {
                AuthInterceptor authInterceptor = new AuthInterceptor(authToken);
                clientBuilder.addInterceptor(authInterceptor);
            }
            OkHttpClient client = clientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Openrouteservice getApiRoute(){
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(new TLSSocketFactory(), new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROUTE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Openrouteservice service = retrofit.create(Openrouteservice.class);
        return service;
    }
}

