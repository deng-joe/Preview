package com.joe.preview.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.joe.preview.data.local.dao.MovieDao;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.remote.api.MovieApiService;
import com.joe.preview.data.remote.resources.Resource;
import com.joe.preview.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

public class MovieListViewModel extends BaseViewModel {

    private String type;
    private MovieRepository movieRepository;
    private MutableLiveData<Resource<List<Movie>>> moviesLiveData = new MutableLiveData<>();

    @Inject
    public MovieListViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    public void setType(String type) {
        this.type = type;
    }

    public MutableLiveData<Resource<List<Movie>>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public void loadMoreMovies(Long currentPage) {
        movieRepository.loadMoviesByType(currentPage, type)
                .doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(listResource -> getMoviesLiveData().postValue(listResource));
    }

    public boolean isLastPage() {
        return (moviesLiveData.getValue() != null &&
                !moviesLiveData.getValue().data.isEmpty()) && moviesLiveData.getValue().data.get(0)
                .isLastPage();
    }
}
