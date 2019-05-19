package com.joe.preview.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.joe.preview.data.local.entity.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Insert(onConflict = REPLACE)
    void insertMovies(List<Movie> movies);

    @Insert(onConflict = REPLACE)
    void insertMovie(Movie movie);

    @Update(onConflict = REPLACE)
    void updateMovie(Movie movie);

    @Query("SELECT * FROM films WHERE id = :id")
    Movie getMovieById(Long id);

    @Query("SELECT * FROM films WHERE page = :page")
    List<Movie> getMoviesByPage(Long page);

}
