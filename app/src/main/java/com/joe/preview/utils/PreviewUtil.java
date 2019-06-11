package com.joe.preview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.joe.preview.R;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.model.Genre;
import com.joe.preview.ui.custom.SlideMenuItem;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.joe.preview.constants.PreviewConstants.MOVIE_STATUS_RELEASED;

public class PreviewUtil {

    private static Date getDate(String date) {
        ParsePosition parsePosition = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return simpleDateFormat.parse(date, parsePosition);
    }

    private static String getFormattedDate(String dateString) {
        Date date = getDate(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DATE);
        switch (day % 10) {
            case 1:
                return new SimpleDateFormat("MMMM d'st', yyyy", Locale.US).format(date);
            case 2:
                return new SimpleDateFormat("MMMM d'nd', yyyy", Locale.US).format(date);
            case 3:
                return new SimpleDateFormat("MMMM d'rd', yyyy", Locale.US).format(date);
            default:
                return new SimpleDateFormat("MMMM d'th', yyyy", Locale.US).format(date);
        }
    }

    public static List<String> getGenres(List<Genre> genres) {
        List<String> genreNames = new ArrayList<>(genres.size());
        for (Object object : genres) {
            if (object instanceof String)
                genreNames.add(Objects.toString(object, null));
            else
                genreNames.add(String.valueOf(((Genre) object).getName()));
        }
        return genreNames;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width;
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int height;
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        return height;
    }

    public static List<SlideMenuItem> getMenuList(Context context) {
        List<SlideMenuItem> menuItems = new ArrayList<>();
        List<String> menuTitles = Arrays.asList(context.getResources().getStringArray(R.array.menu_names));
        TypedArray menuIcons = context.getResources().obtainTypedArray(R.array.menu_icons);

        for (int i = 0; i < menuTitles.size(); i++) {
            SlideMenuItem slideMenuItem = new SlideMenuItem(menuTitles.get(i), menuIcons.getResourceId(i, -1));
            menuItems.add(slideMenuItem);
        }

        menuIcons.recycle();
        return menuItems;
    }

    public static List<Movie> getMoviesByType(String type, List<Movie> movies) {
        List<Movie> finalList = new ArrayList<>();
        for (Movie movie : movies) {
            boolean add = false;
            for (String categoryType : movie.getCategoryTypes()) {
                if (type.equalsIgnoreCase(categoryType))
                    add = true;
            }
            if (add)
                finalList.add(movie);
        }
        return finalList;
    }

    public static List<Series> getSeriesByType(String type, List<Series> seriesList) {
        List<Series> finalList = new ArrayList<>();
        for (Series series : seriesList) {
            boolean add = false;
            for (String categoryType : series.getCategoryTypes()) {
                if (type.equalsIgnoreCase(categoryType))
                    add = true;
            }
            if (add)
                finalList.add(series);
        }
        return finalList;
    }

    public static String getRuntimeInMinutes(String status, Long runtime, String releaseDate) {
        return MOVIE_STATUS_RELEASED.equalsIgnoreCase(status) && runtime != null
                ? String.format("%s minutes", String.valueOf(runtime)) : getFormattedDate(releaseDate);
    }

    public static String getSeasonNumber(Long seasonNumber) {
        return String.format("Season %s", seasonNumber);
    }

    public static void closeKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
