package com.hasawi.sears_outlet.data.api;

import android.os.Build;

import com.hasawi.sears_outlet.utils.Connectivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    public static final String BASE_URL = "https://preprod.acekuwait.com/api/v0/";
    //    public static final String BASE_URL = "https://searskuwait.com/api/v0/";
    private static RetrofitApiClient instance = null;
    private ApiInterface apiInterface;

    private RetrofitApiClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("OS_VERSION", Build.VERSION.RELEASE)
                                        .addHeader("IP_ADDRESS", Connectivity.getIPAddress(true))
                                        .build();
                                return chain.proceed(request);
                            }
                        }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(defaultHttpClient)
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
