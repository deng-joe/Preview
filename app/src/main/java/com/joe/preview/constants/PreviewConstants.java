package com.joe.preview.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface PreviewConstants {

    int PAGE_LIMIT = 10;

    String CREDIT_CAST = "cast";
    String CREDIT_CREW = "crew";

    String INTENT_MOVIE = "movie";
    String INTENT_CATEGORY = "category";
    String INTENT_VIDEO_KEY = "intent_video_key";

    String TRANSITION_IMAGE_NAME = "image";

    String TYPE_MOVIES = "movie";
    String TYPE_SERIES = "series";
    String MOVIES_POPULAR = "popular";
    String MOVIES_UPCOMING = "upcoming";
    String MOVIES_TOP_RATED = "top_rated";
    String SERIES_ON_THE_AIR = "on_the_air";

    String MOVIE_STATUS_RELEASED = "Released";

    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_URL = "https://image.tmdb.org/t/p/w500%s";

    String TMDB_API_KEY = "f1b94f3174280b784b294d79761cddec";
    String YOUTUBE_API_KEY = "AIzaSyBPn61RbVfxmKBr5iZhFsb-XNn1E1U4cL4";

    Map<Integer, String> MENU_MOVIE_ITEM = Collections.unmodifiableMap(
            new HashMap<Integer, String>() {{
                put(0, MOVIES_POPULAR);
                put(1, MOVIES_UPCOMING);
                put(2, MOVIES_TOP_RATED);
            }}
    );

    Map<Integer, String> MENU_SERIES_ITEM = Collections.unmodifiableMap(
            new HashMap<Integer, String>() {{
                put(0, MOVIES_POPULAR);
                put(1, SERIES_ON_THE_AIR);
                put(2, MOVIES_TOP_RATED);
            }}
    );

}
