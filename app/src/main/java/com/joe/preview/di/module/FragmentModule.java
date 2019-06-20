package com.joe.preview.di.module;

import com.joe.preview.ui.fragment.MoviesListFragment;
import com.joe.preview.ui.fragment.SeriesListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MoviesListFragment contributeMovieListFragment();

    @ContributesAndroidInjector
    abstract SeriesListFragment contributeSeriesListFragment();

}
