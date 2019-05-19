package com.joe.preview.data.remote.api;

import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.remote.model.CreditResponse;
import com.joe.preview.data.remote.model.MovieApiResponse;
import com.joe.preview.data.remote.model.ReviewApiResponse;
import com.joe.preview.data.remote.model.VideoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("movie/{type}?language=en")
    Observable<MovieApiResponse> fetchMoviesByType(@Path("type") String type, @Query("page") long page);

    @GET("/3/movie/{movieId}")
    Observable<Movie> fetchMovieDetails(@Path("movieId") String movieId);

    @GET("/3/movie/{movieId}/videos")
    Observable<VideoResponse> fetchMovieVideo(@Path("movieId") String movieId);

    @GET("/3/movie/{movieId}/credits")
    Observable<CreditResponse> fetchCastDetails(@Path("movieId") String movieId);

    @GET("/3/movie/{movieId}/similar")
    Observable<MovieApiResponse> fetchSimilarMovies(@Path("movieId") String movieId, @Query("page") long page);

    @GET("/3/movie/{movieId}/reviews")
    Observable<ReviewApiResponse> fetchMovieReviews(@Path("movieId") String movieId);

    @GET("/3/search/movie")
    Observable<MovieApiResponse> searchMovieByQuery(@Query("query") String query, @Query("page") String page);

}
