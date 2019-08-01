package com.joe.preview.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.joe.preview.data.local.dao.MovieDao;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.remote.api.MovieApiService;
import com.joe.preview.data.remote.resources.Resource;
import com.joe.preview.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieSearchViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<List<Movie>>> moviesLiveData = new MutableLiveData<>();

    @Inject
    public MovieSearchViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    public MutableLiveData<Resource<List<Movie>>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public void searchMovie(String text) {
        movieRepository.searchMovies(1L, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResource -> getMoviesLiveData().postValue(listResource));
    }
}
