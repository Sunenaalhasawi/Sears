package com.hasawi.sears.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
//        public static final String BASE_URL = "https://preprod.acekuwait.com/api/v0/";
    public static final String BASE_URL = "https://searskuwait.com/api/v0/";
    private static RetrofitApiClient instance = null;
    private ApiInterface apiInterface;

    private RetrofitApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitApiClient getInstance() {
        if (instance == null) {
            instance = new RetrofitApiClient();
        }
        return instance;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

}
