package com.joe.preview.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.joe.preview.factory.ViewModelFactory;
import com.joe.preview.viewmodel.MovieDetailViewModel;
import com.joe.preview.viewmodel.MovieListViewModel;
import com.joe.preview.viewmodel.MovieSearchViewModel;
import com.joe.preview.viewmodel.SeriesDetailViewModel;
import com.joe.preview.viewmodel.SeriesListViewModel;
import com.joe.preview.viewmodel.SeriesSearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    protected abstract ViewModel movieListViewModel(MovieListViewModel movieListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    protected abstract ViewModel movieDetailViewModel(MovieDetailViewModel movieDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieSearchViewModel.class)
    protected abstract ViewModel movieSearchViewModel(MovieSearchViewModel movieSearchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SeriesListViewModel.class)
    protected abstract ViewModel seriesListViewModel(SeriesListViewModel seriesListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SeriesDetailViewModel.class)
    protected abstract ViewModel seriesDetailViewModel(SeriesDetailViewModel seriesDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SeriesSearchViewModel.class)
    protected abstract ViewModel seriesSearchViewModel(SeriesSearchViewModel seriesSearchViewModel);

}
