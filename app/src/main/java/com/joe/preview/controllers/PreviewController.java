package com.joe.preview.controllers;

import android.app.Activity;
import android.app.Application;

import com.joe.preview.di.component.DaggerPreviewComponent;
import com.joe.preview.di.module.ApiModule;
import com.joe.preview.di.module.RoomDatabaseModule;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class PreviewController extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerPreviewComponent.builder()
                .application(this)
                .apiModule(new ApiModule())
                .databaseModule(new RoomDatabaseModule())
                .build()
                .inject(this);
    }

}
