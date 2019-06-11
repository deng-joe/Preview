package com.joe.preview.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.joe.preview.data.local.dao.SeriesDao;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.api.SeriesApiService;
import com.joe.preview.data.remote.resources.Resource;
import com.joe.preview.repository.SeriesRepository;

import java.util.List;

import javax.inject.Inject;

public class SeriesListViewModel extends BaseViewModel {

    private String type;
    private SeriesRepository seriesRepository;
    private MutableLiveData<Resource<List<Series>>> seriesLiveData = new MutableLiveData<>();

    @Inject
    public SeriesListViewModel(SeriesDao seriesDao, SeriesApiService seriesApiService) {
        seriesRepository = new SeriesRepository(seriesDao, seriesApiService);
    }

    public void setType(String type) {
        this.type = type;
    }

    public MutableLiveData<Resource<List<Series>>> getSeriesLiveData() {
        return seriesLiveData;
    }

    public void loadMoreSeries(Long currentPage) {
        seriesRepository.loadSeriesByType(currentPage, type)
                .doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(listResource -> getSeriesLiveData().postValue(listResource));
    }

    public boolean isLastPage() {
        return (seriesLiveData.getValue() != null &&
                !seriesLiveData.getValue().data.isEmpty()) && seriesLiveData.getValue().data.get(0)
                .isLastPage();
    }
}
