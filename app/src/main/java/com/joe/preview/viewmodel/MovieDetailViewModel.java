package com.joe.preview.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.joe.preview.data.local.dao.MovieDao;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.remote.api.MovieApiService;
import com.joe.preview.repository.MovieRepository;

import javax.inject.Inject;

public class MovieDetailViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<Movie> movieDetailsLiveData = new MutableLiveData<>();

    @Inject
    MovieDetailViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    public MutableLiveData<Movie> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }

    public void fetchMovieDetails(Movie movie) {
        movieRepository.fetchMovieDetails(movie.getId())
                .subscribe(movieResource -> {
                    if (movieResource.isLoaded())
                        getMovieDetailsLiveData().postValue(movieResource.data);
                });
    }
}
