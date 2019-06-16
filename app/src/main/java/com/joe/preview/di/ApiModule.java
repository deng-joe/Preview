package com.joe.preview.di;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joe.preview.data.remote.api.MovieApiService;
import com.joe.preview.data.remote.api.SeriesApiService;
import com.joe.preview.data.remote.interceptors.NetworkInterceptor;
import com.joe.preview.data.remote.interceptors.RequestInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.joe.preview.constants.PreviewConstants.BASE_URL;

@Module
public class ApiModule {

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @Provides
    @Singleton
    Cache provideCache(Application application) {
        long cacheSize = 10 * 1024 * 1024;  // 10 MB
        File cacheDir = new File(application.getCacheDir(), "http-cache");
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    @Singleton
    NetworkInterceptor provideNetworkInterceptor(Application application) {
        return new NetworkInterceptor(application.getApplicationContext());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, NetworkInterceptor networkInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
        builder.addInterceptor(networkInterceptor);
        builder.addInterceptor(loggingInterceptor);
        builder.addNetworkInterceptor(new RequestInterceptor());
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    MovieApiService provideMovieApiService(Retrofit retrofit) {
        return retrofit.create(MovieApiService.class);
    }

    @Provides
    @Singleton
    SeriesApiService provideSeriesApiService(Retrofit retrofit) {
        return retrofit.create(SeriesApiService.class);
    }

}
