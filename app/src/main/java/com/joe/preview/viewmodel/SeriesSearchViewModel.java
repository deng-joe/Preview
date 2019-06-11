package com.joe.preview.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.joe.preview.data.local.dao.SeriesDao;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.api.SeriesApiService;
import com.joe.preview.data.remote.resources.Resource;
import com.joe.preview.repository.SeriesRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SeriesSearchViewModel extends BaseViewModel {

    private SeriesRepository seriesRepository;
    private MutableLiveData<Resource<List<Series>>> seriesLiveData = new MutableLiveData<>();

    @Inject
    public SeriesSearchViewModel(SeriesDao seriesDao, SeriesApiService seriesApiService) {
        seriesRepository = new SeriesRepository(seriesDao, seriesApiService);
    }

    public MutableLiveData<Resource<List<Series>>> getSeriesLiveData() {
        return seriesLiveData;
    }

    public void searchSeries(String text) {
        seriesRepository.searchSeries(1L, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResource -> getSeriesLiveData().postValue(listResource));
    }
}
