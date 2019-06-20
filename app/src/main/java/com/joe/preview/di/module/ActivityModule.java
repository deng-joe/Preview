package com.joe.preview.di.module;

import com.joe.preview.ui.activity.MainActivity;
import com.joe.preview.ui.activity.MovieDetailActivity;
import com.joe.preview.ui.activity.MovieSearchActivity;
import com.joe.preview.ui.activity.SeriesDetailActivity;
import com.joe.preview.ui.activity.SeriesSearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract MovieDetailActivity contributeMovieDetailActivity();

    @ContributesAndroidInjector()
    abstract SeriesDetailActivity contributeSeriesDetailActivity();

    @ContributesAndroidInjector()
    abstract MovieSearchActivity contributeMovieSearchActivity();

    @ContributesAndroidInjector()
    abstract SeriesSearchActivity contributeSeriesSearchActivity();

}
