package com.joe.preview.di;

import android.app.Application;

import androidx.room.Room;

import com.joe.preview.data.local.dao.MovieDao;
import com.joe.preview.data.local.dao.SeriesDao;
import com.joe.preview.data.local.database.PreviewRoomDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomDatabaseModule {

    @Provides
    @Singleton
    PreviewRoomDatabase provideRoomDatabase(Application application) {
        String database_name = "Entertainment;";
        return Room.databaseBuilder(application,
                PreviewRoomDatabase.class,
                database_name)
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(PreviewRoomDatabase database) {
        return database.movieDao();
    }

    @Provides
    @Singleton
    SeriesDao provideSeriesDao(PreviewRoomDatabase database) {
        return database.seriesDao();
    }

}
