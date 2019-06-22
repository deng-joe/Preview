package com.joe.preview.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SnapHelper;

import com.joe.preview.R;
import com.joe.preview.adapter.SeriesListAdapter;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.databinding.MoviesListFragmentBinding;
import com.joe.preview.factory.ViewModelFactory;
import com.joe.preview.ui.activity.MainActivity;
import com.joe.preview.ui.custom.recyclerview.PagerSnapHelper;
import com.joe.preview.ui.custom.recyclerview.RecyclerItemClickListener;
import com.joe.preview.ui.custom.recyclerview.RecyclerViewPaginator;
import com.joe.preview.utils.NavigationUtil;
import com.joe.preview.viewmodel.SeriesListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SeriesListFragment extends BaseFragment implements RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    private SeriesListViewModel seriesListViewModel;
    private SeriesListAdapter seriesListAdapter;
    private MoviesListFragmentBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        initializeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        seriesListViewModel.onStop();
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME));
        NavigationUtil.redirectToSeriesDetails(requireActivity(), seriesListAdapter.getItem(position), optionsCompat);
    }

    private void initializeView() {
        seriesListAdapter = new SeriesListAdapter(activity);
        binding.moviesList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.moviesList.setAdapter(seriesListAdapter);
        binding.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));

        SnapHelper snapHelper = new PagerSnapHelper(position -> {
            Series series = seriesListAdapter.getItem(position);
            ((MainActivity) activity).updateBackground(series.getPosterPath());
        });

        snapHelper.attachToRecyclerView(binding.moviesList);

        binding.moviesList.addOnScrollListener(new RecyclerViewPaginator(binding.moviesList) {
            @Override
            public boolean isLastPage() {
                return seriesListViewModel.isLastPage();
            }

            @Override
            public void loadMore(Long page) {
                seriesListViewModel.loadMoreSeries(page);
            }

            @Override
            public void loadFirstData(Long page) {
                displayLoader();
                seriesListViewModel.loadMoreSeries(page);
            }
        });
    }

    private void initializeViewModel() {
        seriesListViewModel = ViewModelProviders.of(this, viewModelFactory).get(SeriesListViewModel.class);
        seriesListViewModel.setType(MENU_SERIES_ITEM.get(getArguments() == null ? 0 : getArguments().getInt(INTENT_CATEGORY)));
        seriesListViewModel.getSeriesLiveData().observe(this, listResource -> {
            if (listResource.isLoading()) {

            } else if (!listResource.data.isEmpty())
                updateSeriesList(listResource.data);
            else
                handleErrorResponse();
        });
    }

    private void updateSeriesList(List<Series> seriesList) {
        hideLoader();
        binding.emptyLayout.emptyContainer.setVisibility(View.GONE);
        binding.moviesList.setVisibility(View.VISIBLE);
        seriesListAdapter.setItems(seriesList);
    }

    private void handleErrorResponse() {
        hideLoader();
        binding.moviesList.setVisibility(View.GONE);
        binding.emptyLayout.emptyContainer.setVisibility(View.VISIBLE);
        ((MainActivity) activity).clearBackground();
    }

    private void displayLoader() {
        binding.moviesList.setVisibility(View.GONE);
        binding.loaderLayout.rootView.setVisibility(View.VISIBLE);
        binding.loaderLayout.loader.start();
        ((MainActivity) activity).hideToolbar();
    }

    private void hideLoader() {
        binding.moviesList.setVisibility(View.VISIBLE);
        binding.loaderLayout.rootView.setVisibility(View.GONE);
        binding.loaderLayout.loader.stop();
        ((MainActivity) activity).displayToolbar();
    }

}
