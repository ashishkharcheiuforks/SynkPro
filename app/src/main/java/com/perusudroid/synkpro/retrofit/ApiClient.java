package com.perusudroid.synkpro.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.perusudroid.synkpro.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static volatile ApiClient instance;
    private final ApiInterface servicesApi;
    private static final int TIME_OUT=1;

    public static ApiClient getInstance() {
        ApiClient localInstance = instance;
        if (localInstance == null) {
            synchronized (ApiClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApiClient();
                }
            }
        }
        return localInstance;
    }

    private ApiClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = clientBuilder
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(TIME_OUT,TimeUnit.MINUTES)
                .writeTimeout(TIME_OUT,TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        servicesApi = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getServicesApi() {
        return servicesApi;
    }

}
