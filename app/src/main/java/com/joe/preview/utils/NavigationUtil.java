package com.joe.preview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.joe.preview.constants.PreviewConstants;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.ui.activity.MovieDetailActivity;
import com.joe.preview.ui.activity.MovieSearchActivity;
import com.joe.preview.ui.activity.SeriesDetailActivity;
import com.joe.preview.ui.activity.SeriesSearchActivity;
import com.joe.preview.ui.activity.VideoActivity;

public class NavigationUtil implements PreviewConstants {

    public static void redirectToMovieDetails(Activity activity, Movie movie, ActivityOptionsCompat optionsCompat) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(INTENT_MOVIE, movie);
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    public static void redirectToSeriesDetails(Activity activity, Series series, ActivityOptionsCompat optionsCompat) {
        Intent intent = new Intent(activity, SeriesDetailActivity.class);
        intent.putExtra(INTENT_MOVIE, series);
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    public static void redirectToVideo(Context context, String videoKey) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(INTENT_VIDEO_KEY, videoKey);
        context.startActivity(intent);
    }

    public static void redirectToMovieSearch(Activity activity) {
        Intent intent = new Intent(activity, MovieSearchActivity.class);
        activity.startActivity(intent);
    }

    public static void redirectToSeriesSearch(Activity activity) {
        Intent intent = new Intent(activity, SeriesSearchActivity.class);
        activity.startActivity(intent);
    }

}
