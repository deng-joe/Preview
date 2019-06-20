package com.joe.preview.di.component;

import android.app.Application;

import com.joe.preview.di.module.ActivityModule;
import com.joe.preview.di.module.ApiModule;
import com.joe.preview.di.module.FragmentModule;
import com.joe.preview.di.module.RoomDatabaseModule;
import com.joe.preview.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ActivityModule.class,
        AndroidSupportInjectionModule.class,
        ApiModule.class, FragmentModule.class,
        RoomDatabaseModule.class,
        ViewModelModule.class}
)
public interface PreviewComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder apiModule(ApiModule apiModule);

        @BindsInstance
        Builder databaseModule(RoomDatabaseModule databaseModule);

        PreviewComponent build();

    }

}
