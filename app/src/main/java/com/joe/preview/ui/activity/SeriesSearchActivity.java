package com.joe.preview.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SnapHelper;

import com.joe.preview.R;
import com.joe.preview.adapter.SeriesSearchListAdapter;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.databinding.SearchActivityBinding;
import com.joe.preview.factory.ViewModelFactory;
import com.joe.preview.ui.custom.recyclerview.PagerSnapHelper;
import com.joe.preview.ui.custom.recyclerview.RecyclerItemClickListener;
import com.joe.preview.utils.NavigationUtil;
import com.joe.preview.utils.PreviewUtil;
import com.joe.preview.viewmodel.SeriesSearchViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SeriesSearchActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    SeriesSearchViewModel seriesSearchViewModel;
    SearchActivityBinding binding;
    SeriesSearchListAdapter seriesSearchListAdapter;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initializeView();
        initializeViewModel();
    }

    private void initializeView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        binding.search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        binding.search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.search.setIconifiedByDefault(false);
        binding.search.setOnQueryTextListener(this);

        EditText searchEditText = binding.search.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));

        seriesSearchListAdapter = new SeriesSearchListAdapter(this);
        binding.includedLayout.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.moviesList.setAdapter(seriesSearchListAdapter);

        SnapHelper startSnapHelper = new PagerSnapHelper(position -> {
            Series trailer = seriesSearchListAdapter.getItem(position);
            updateBackground(trailer.getPosterPath());
        });

        startSnapHelper.attachToRecyclerView(binding.includedLayout.moviesList);
        binding.includedLayout.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), this));
    }

    private void initializeViewModel() {
        seriesSearchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SeriesSearchViewModel.class);
    }

    private void updateBackground(String url) {
        binding.overlayLayout.updateCurrentBackground(url);
    }

    private void querySearch(String text) {
        seriesSearchViewModel.searchSeries(text);
        seriesSearchViewModel.getSeriesLiveData().observe(this, listResource -> {
            if (listResource.isLoading())
                displayLoader();
            else if (listResource.data != null && !listResource.data.isEmpty())
                handleSuccessResponse(listResource.data);
            else
                handleErrorResponse();
        });
    }

    private void handleSuccessResponse(List<Series> seriesList) {
        hideLoader();
        binding.includedLayout.emptyLayout.emptyContainer.setVisibility(View.GONE);
        binding.includedLayout.moviesList.setVisibility(View.VISIBLE);
        seriesSearchListAdapter.setTrailers(seriesList);
        new Handler().postDelayed(() -> {
            if (seriesSearchListAdapter.getItemCount() > 0)
                updateBackground(seriesSearchListAdapter.getItem(0).getPosterPath());
        }, 400);
    }

    private void handleErrorResponse() {
        hideLoader();
        binding.includedLayout.moviesList.setVisibility(View.GONE);
        binding.includedLayout.emptyLayout.emptyContainer.setVisibility(View.VISIBLE);
    }

    private void displayLoader() {
        binding.includedLayout.moviesList.setVisibility(View.GONE);
        binding.includedLayout.loaderLayout.rootView.setVisibility(View.VISIBLE);
        binding.includedLayout.loaderLayout.loader.start();
    }

    private void hideLoader() {
        binding.includedLayout.moviesList.setVisibility(View.VISIBLE);
        binding.includedLayout.loaderLayout.rootView.setVisibility(View.GONE);
        binding.includedLayout.loaderLayout.loader.stop();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        PreviewUtil.closeKeyboard(this);
        querySearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        seriesSearchViewModel.getSeriesLiveData().removeObservers(this);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                new Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME));
        NavigationUtil.redirectToSeriesDetails(this, seriesSearchListAdapter.getItem(position), optionsCompat);
    }

}
