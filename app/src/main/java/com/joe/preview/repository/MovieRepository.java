package com.joe.preview.repository;

import androidx.annotation.NonNull;

import com.joe.preview.data.local.dao.MovieDao;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.remote.api.MovieApiService;
import com.joe.preview.data.remote.model.MovieApiResponse;
import com.joe.preview.data.remote.resources.NetworkBoundResource;
import com.joe.preview.data.remote.resources.Resource;
import com.joe.preview.utils.PreviewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class MovieRepository {

    private MovieDao movieDao;
    private MovieApiService movieApiService;

    public MovieRepository(MovieDao movieDao, MovieApiService movieApiService) {
        this.movieDao = movieDao;
        this.movieApiService = movieApiService;
    }

    public Observable<Resource<List<Movie>>> loadMoviesByType(Long page, String type) {
        return new NetworkBoundResource<List<Movie>, MovieApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieApiResponse item) {
                List<Movie> movies = new ArrayList<>();
                for (Movie movie : item.getResults()) {
                    Movie storedMovie = movieDao.getMovieById(movie.getId());
                    if (storedMovie == null)
                        movie.setCategoryTypes(Collections.singletonList(type));
                    else {
                        List<String> categories = storedMovie.getCategoryTypes();
                        categories.add(type);
                        movie.setCategoryTypes(categories);
                    }

                    movie.setPage(item.getPage());
                    movie.setTotalPages(item.getTotalPages());
                    movies.add(movie);
                }

                movieDao.insertMovies(movies);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<Movie>> loadFromDb() {
                List<Movie> movies = movieDao.getMoviesByPage(page);
                if (movies == null || movies.isEmpty())
                    return Flowable.empty();
                return Flowable.just(PreviewUtil.getMoviesByType(type, movies));
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieApiResponse>> createCall() {
                return movieApiService.fetchMoviesByType(type, page)
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("Error, could not fetch data", new MovieApiResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }

    public Observable<Resource<Movie>> fetchMovieDetails(Long movieId) {
        return new NetworkBoundResource<Movie, Movie>() {

            @Override
            protected void saveCallResult(@NonNull Movie item) {
                Movie movie = movieDao.getMovieById(item.getId());
                if (movie == null)
                    movieDao.insertMovie(item);
                else
                    movieDao.updateMovie(item);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<Movie> loadFromDb() {
                Movie movie = movieDao.getMovieById(movieId);
                if (movie == null)
                    return Flowable.empty();
                return Flowable.just(movie);
            }

            @NonNull
            @Override
            protected Observable<Resource<Movie>> createCall() {
                String id = String.valueOf(movieId);
                return Observable.combineLatest(
                        movieApiService.fetchMovieDetails(id),
                        movieApiService.fetchMovieVideo(id),
                        movieApiService.fetchCastDetails(id),
                        movieApiService.fetchSimilarMovies(id, 1),
                        movieApiService.fetchMovieReviews(id),
                        (movie, videoResponse, creditResponse, movieApiResponse, reviewApiResponse) -> {
                            if (videoResponse != null)
                                movie.setVideos(videoResponse.getResults());
                            if (creditResponse != null) {
                                movie.setCrews(creditResponse.getCrews());
                                movie.setCasts(creditResponse.getCasts());
                            }
                            if (movieApiResponse != null)
                                movie.setSimilarMovies(movieApiResponse.getResults());
                            if (reviewApiResponse != null)
                                movie.setReviews(reviewApiResponse.getResults());

                            return Resource.success(movie);
                        }
                );
            }
        }.getAsObservable();
    }

    public Observable<Resource<List<Movie>>> searchMovies(Long page, String query) {
        return new NetworkBoundResource<List<Movie>, MovieApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieApiResponse item) {
                List<Movie> movies = new ArrayList<>();
                for (Movie movie : item.getResults()) {
                    Movie storedMovie = movieDao.getMovieById(movie.getId());
                    if (storedMovie == null)
                        movie.setCategoryTypes(Collections.singletonList(query));
                    else {
                        List<String> categories = storedMovie.getCategoryTypes();
                        categories.add(query);
                        movie.setCategoryTypes(categories);
                    }

                    movie.setPage(item.getPage());
                    movie.setTotalPages(item.getTotalPages());
                    movies.add(movie);
                }

                movieDao.insertMovies(movies);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<Movie>> loadFromDb() {
                List<Movie> movies = movieDao.getMoviesByPage(page);
                if (movies == null || movies.isEmpty())
                    return Flowable.empty();
                return Flowable.just(PreviewUtil.getMoviesByType(query, movies));
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieApiResponse>> createCall() {
                return movieApiService.searchMovieByQuery(query, "1")
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("Error, could not fetch data", new MovieApiResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }

}
