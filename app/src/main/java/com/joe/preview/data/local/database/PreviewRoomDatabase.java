package com.joe.preview.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.joe.preview.data.local.converters.CastConverter;
import com.joe.preview.data.local.converters.CreditResponseConverter;
import com.joe.preview.data.local.converters.CrewConverter;
import com.joe.preview.data.local.converters.GenreConverter;
import com.joe.preview.data.local.converters.MovieConverter;
import com.joe.preview.data.local.converters.ReviewConverter;
import com.joe.preview.data.local.converters.SeriesConverter;
import com.joe.preview.data.local.converters.StringListConverter;
import com.joe.preview.data.local.converters.VideoConverter;
import com.joe.preview.data.local.dao.MovieDao;
import com.joe.preview.data.local.dao.SeriesDao;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.local.entity.Series;

@Database(entities = {Movie.class, Series.class}, version = 1, exportSchema = false)
@TypeConverters({GenreConverter.class, VideoConverter.class, CreditResponseConverter.class, MovieConverter.class, CastConverter.class,
        CrewConverter.class, StringListConverter.class, SeriesConverter.class, ReviewConverter.class})
public abstract class PreviewRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();
    public abstract SeriesDao seriesDao();

    private static volatile PreviewRoomDatabase INSTANCE;

    public static PreviewRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PreviewRoomDatabase.class) {
                if (INSTANCE == null) {
                    String database_name = "Entertainment";
                    INSTANCE = Room.databaseBuilder(context,
                            PreviewRoomDatabase.class,
                            database_name)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
