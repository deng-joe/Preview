package com.joe.preview.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.joe.preview.data.local.dao.SeriesDao;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.api.SeriesApiService;
import com.joe.preview.data.repository.SeriesRepository;

import javax.inject.Inject;

public class SeriesDetailViewModel extends BaseViewModel {

    private SeriesRepository seriesRepository;
    private MutableLiveData<Series> seriesDetailsLiveData = new MutableLiveData<>();

    @Inject
    public SeriesDetailViewModel(SeriesDao seriesDao, SeriesApiService seriesApiService) {
        seriesRepository = new SeriesRepository(seriesDao, seriesApiService);
    }

    public MutableLiveData<Series> getSeriesDetailsLiveData() {
        return seriesDetailsLiveData;
    }

    public void fetchSeriesDetails(Series series) {
        seriesRepository.fetchSeriesDetails(series.getId())
                .subscribe(seriesResource -> {
                    if (seriesResource.isLoaded())
                        getSeriesDetailsLiveData().postValue(seriesResource.data);
                });
    }
}
