package com.joe.preview.data.remote.api;

import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.model.CreditResponse;
import com.joe.preview.data.remote.model.ReviewApiResponse;
import com.joe.preview.data.remote.model.SeriesApiResponse;
import com.joe.preview.data.remote.model.VideoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SeriesApiService {

    @GET("tv/{type}?language=en")
    Observable<SeriesApiResponse> fetchSeriesByType(@Path("type") String type, @Query("page") long page);

    @GET("/3/tv/{tvId}")
    Observable<Series> fetchSeriesDetails(@Path("tvId") String seriesId);

    @GET("/3/tv/{tvId}/videos")
    Observable<VideoResponse> fetchSeriesVideo(@Path("tvId") String seriesId);

    @GET("/3/tv/{tvId}/credits")
    Observable<CreditResponse> fetchCastDetails(@Path("tvId") String seriesId);

    @GET("/3/tv/{tvId}/similar")
    Observable<SeriesApiResponse> fetchSimilarSeries(@Path("tvId") String tvId, @Query("page") long page);

    @GET("/3/tv/{tvId}/reviews")
    Observable<ReviewApiResponse> fetchSeriesReviews(@Path("tvId") String tvId);

    @GET("/3/search/tv")
    Observable<SeriesApiResponse> searchSeriesByQuery(@Query("query") String query, @Query("page") String page);

}
