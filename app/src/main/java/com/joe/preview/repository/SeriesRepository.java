package com.joe.preview.repository;

import androidx.annotation.NonNull;

import com.joe.preview.data.local.dao.SeriesDao;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.api.SeriesApiService;
import com.joe.preview.data.remote.model.SeriesApiResponse;
import com.joe.preview.data.remote.resources.NetworkBoundResource;
import com.joe.preview.data.remote.resources.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class SeriesRepository {

    private SeriesDao seriesDao;
    private SeriesApiService seriesApiService;

    public SeriesRepository(SeriesDao seriesDao, SeriesApiService seriesApiService) {
        this.seriesDao = seriesDao;
        this.seriesApiService = seriesApiService;
    }

    public Observable<Resource<List<Series>>> loadSeriesByType(Long page, String type) {
        return new NetworkBoundResource<List<Series>, SeriesApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull SeriesApiResponse item) {
                List<Series> seriesList = new ArrayList<>();
                for (Series series : item.getResults()) {
                    Series storedShow = seriesDao.getSeriesById(series.getId());
                    if (storedShow == null)
                        series.setCategoryTypes(Collections.singletonList(type));
                    else {
                        List<String> categories = storedShow.getCategoryTypes();
                        categories.add(type);
                        series.setCategoryTypes(categories);
                    }

                    series.setPage(item.getPage());
                    series.setTotalPages(item.getTotalPages());
                    seriesList.add(series);
                }

                seriesDao.insertShows(seriesList);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<Series>> loadFromDb() {
                List<Series> seriesList = seriesDao.getSeriesByPage(page);
                if (seriesList == null || seriesList.isEmpty())
                    return Flowable.empty();
                return null;
            }

            @NonNull
            @Override
            protected Observable<Resource<SeriesApiResponse>> createCall() {
                return seriesApiService.fetchSeriesByType(type, page)
                        .flatMap(seriesApiResponse -> Observable.just(seriesApiResponse == null
                                ? Resource.error("Error, could not fetch data", new SeriesApiResponse())
                                : Resource.success(seriesApiResponse)));
            }
        }.getAsObservable();
    }

    public Observable<Resource<Series>> fetchSeriesDetails(Long seriesId) {
        return new NetworkBoundResource<Series, Series>() {

            @Override
            protected void saveCallResult(@NonNull Series item) {
                Series series = seriesDao.getSeriesById(item.getId());
                if (series == null)
                    seriesDao.insertShow(item);
                else
                    seriesDao.updateShow(item);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<Series> loadFromDb() {
                Series series = seriesDao.getSeriesById(seriesId);
                if (series == null)
                    return Flowable.empty();
                return Flowable.just(series);
            }

            @NonNull
            @Override
            protected Observable<Resource<Series>> createCall() {
                String id = String.valueOf(seriesId);
                return Observable.combineLatest(
                        seriesApiService.fetchSeriesDetails(id),
                        seriesApiService.fetchSeriesVideo(id),
                        seriesApiService.fetchCastDetails(id),
                        seriesApiService.fetchSimilarSeries(id, 1),
                        seriesApiService.fetchSeriesReviews(id),
                        (series, videoResponse, creditResponse, seriesApiResponse, reviewApiResponse) -> {
                            if (videoResponse != null)
                                series.setVideos(videoResponse.getResults());
                            if (creditResponse != null) {
                                series.setCrews(creditResponse.getCrews());
                                series.setCasts(creditResponse.getCasts());
                            }
                            if (seriesApiResponse != null)
                                series.setSimilarSeries(seriesApiResponse.getResults());
                            if (reviewApiResponse != null)
                                series.setReviews(reviewApiResponse.getResults());

                            return Resource.success(series);
                        }
                );
            }
        }.getAsObservable();
    }

    public Observable<Resource<List<Series>>> searchSeries(Long page, String query) {
        return new NetworkBoundResource<List<Series>, SeriesApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull SeriesApiResponse item) {
                List<Series> seriesList = new ArrayList<>();
                for (Series series : item.getResults()) {
                    Series storedShow = seriesDao.getSeriesById(series.getId());
                    if (storedShow == null)
                        series.setCategoryTypes(Collections.singletonList(query));
                    else {
                        List<String> categories = storedShow.getCategoryTypes();
                        categories.add(query);
                        series.setCategoryTypes(categories);
                    }

                    series.setPage(item.getPage());
                    series.setTotalPages(item.getTotalPages());
                    seriesList.add(series);
                }

                seriesDao.insertShows(seriesList);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<Series>> loadFromDb() {
                List<Series> seriesList = seriesDao.getSeriesByPage(page);
                if (seriesList == null || seriesList.isEmpty())
                    return Flowable.empty();
                return null;
            }

            @NonNull
            @Override
            protected Observable<Resource<SeriesApiResponse>> createCall() {
                return seriesApiService.searchSeriesByQuery(query, "1")
                        .flatMap(seriesApiResponse -> Observable.just(seriesApiResponse == null
                                ? Resource.error("Error, could not fetch data", new SeriesApiResponse())
                                : Resource.success(seriesApiResponse)));
            }
        }.getAsObservable();
    }

}
