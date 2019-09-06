package com.joe.preview.data.remote.interceptors;

import androidx.annotation.NonNull;

import com.joe.preview.constants.PreviewConstants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl httpUrl = request.url();

        HttpUrl url = httpUrl.newBuilder()
                .addQueryParameter("api_key", PreviewConstants.TMDB_API_KEY)
                .build();

        Request.Builder builder = request
                .newBuilder()
                .url(url);

        Request newRequest = builder.build();

        return chain.proceed(newRequest);
    }

}
