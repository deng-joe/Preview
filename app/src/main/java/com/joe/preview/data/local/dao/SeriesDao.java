package com.joe.preview.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.joe.preview.data.local.entity.Series;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SeriesDao {

    @Insert(onConflict = REPLACE)
    void insertShows(List<Series> shows);

    @Insert(onConflict = REPLACE)
    void insertShow(Series series);

    @Update(onConflict = REPLACE)
    void updateShow(Series series);

    @Query("SELECT * FROM tv_shows WHERE id = :id")
    Series getSeriesById(Long id);

    @Query("SELECT * FROM tv_shows WHERE page = :page")
    List<Series> getSeriesByPage(Long page);

}
