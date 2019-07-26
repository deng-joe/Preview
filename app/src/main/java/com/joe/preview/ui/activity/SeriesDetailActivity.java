package com.joe.preview.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.joe.preview.R;
import com.joe.preview.adapter.CreditListAdapter;
import com.joe.preview.adapter.ReviewListAdapter;
import com.joe.preview.adapter.SimilarSeriesListAdapter;
import com.joe.preview.adapter.VideoListAdapter;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.data.remote.model.Cast;
import com.joe.preview.data.remote.model.Crew;
import com.joe.preview.data.remote.model.Review;
import com.joe.preview.data.remote.model.Video;
import com.joe.preview.databinding.DetailActivityBinding;
import com.joe.preview.factory.ViewModelFactory;
import com.joe.preview.ui.custom.recyclerview.RecyclerItemClickListener;
import com.joe.preview.utils.NavigationUtil;
import com.joe.preview.utils.PreviewUtil;
import com.joe.preview.viewmodel.SeriesDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SeriesDetailActivity extends BaseActivity {

    DetailActivityBinding binding;
    SeriesDetailViewModel seriesDetailViewModel;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Series series = getIntent().getParcelableExtra(INTENT_MOVIE);
        Picasso.get().load(series.getPosterPath()).into(binding.image);
        ViewCompat.setTransitionName(binding.image, TRANSITION_IMAGE_NAME);
        binding.expandButton.setPaintFlags(binding.expandButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initializeViewModel() {
        seriesDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(SeriesDetailViewModel.class);
        seriesDetailViewModel.fetchSeriesDetails(getIntent().getParcelableExtra(INTENT_MOVIE));
        seriesDetailViewModel.getSeriesDetailsLiveData().observe(this, series -> {
            updateSeriesDetailsView(series);
            if (series.getVideos() != null && !series.getVideos().isEmpty())
                updateSeriesVideos(series.getVideos());
            if (series.getCrews() != null && !series.getCrews().isEmpty())
                updateSeriesCrewDetails(series.getCrews());
            if (series.getCasts() != null && !series.getCasts().isEmpty()) {
                binding.expandButton.setVisibility(View.VISIBLE);
                updateSeriesCastDetails(series.getCasts());
            }
            if (series.getSimilarSeries() != null && !series.getSimilarSeries().isEmpty())
                updateSimilarSeriesView(series.getSimilarSeries());
            if (series.getReviews() != null && !series.getReviews().isEmpty())
                updateSeriesReviews(series.getReviews());
            else
                binding.includedReviewsLayout.reviews.setVisibility(View.GONE);
        });
    }

    private void updateSeriesDetailsView(Series series) {
        binding.movieTitle.setText(series.getHeader());
        binding.movieDesc.setText(series.getDescription());
        if (series.getStatus() != null)
            binding.movieStatus.setItems(Collections.singletonList(series.getStatus()));
        binding.collectionItemPicker.setUseRandomColor(true);
        if (series.getGenres() != null)
            binding.collectionItemPicker.setItems(PreviewUtil.getGenres(series.getGenres()));
        if (series.getNumberOfSeasons() != null)
            binding.runtime.setText(PreviewUtil.getSeasonNumber(series.getNumberOfSeasons()));
    }

    private void updateSeriesVideos(List<Video> videos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.smoothScrollToPosition(1);

        VideoListAdapter videoListAdapter = new VideoListAdapter(getApplicationContext(), videos);
        binding.recyclerView.setAdapter(videoListAdapter);
        binding.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (parentView, childView, position) ->
                NavigationUtil.redirectToVideo(this, videoListAdapter.getItem(position).getKey())));
    }

    private void updateSeriesCastDetails(List<Cast> casts) {
        binding.includedLayout.castList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.castList.setVisibility(View.VISIBLE);

        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), casts);
        binding.includedLayout.castList.setAdapter(creditListAdapter);
    }

    private void updateSeriesCrewDetails(List<Crew> crews) {
        binding.includedLayout.crewList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.crewList.setVisibility(View.VISIBLE);

        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), CREDIT_CREW, crews);
        binding.includedLayout.crewList.setAdapter(creditListAdapter);
    }

    private void updateSeriesReviews(List<Review> reviews) {
        binding.includedReviewsLayout.reviewsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.includedReviewsLayout.reviewsList.setVisibility(View.VISIBLE);

        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviews);
        binding.includedReviewsLayout.reviewsList.setAdapter(reviewListAdapter);
        binding.includedReviewsLayout.reviews.setVisibility(View.VISIBLE);
    }

    private void updateSimilarSeriesView(List<Series> seriesList) {
        binding.includedSimilarLayout.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedSimilarLayout.moviesList.setVisibility(View.VISIBLE);

        SimilarSeriesListAdapter similarSeriesListAdapter = new SimilarSeriesListAdapter(this, seriesList);
        binding.includedSimilarLayout.moviesList.setAdapter(similarSeriesListAdapter);

        binding.includedSimilarLayout.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(
                this, (parentView, childView, position) -> {
            Series series = similarSeriesListAdapter.getItem(position);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    new Pair(childView, TRANSITION_IMAGE_NAME));
            NavigationUtil.redirectToSeriesDetails(this, series, optionsCompat);
        }));
        binding.includedSimilarLayout.movieSimilarTitle.setVisibility(View.VISIBLE);
    }

    public void handleExpandAction(View view) {
        if (binding.includedLayout.expandableLayout.isExpanded()) {
            binding.expandButton.setText(getString(R.string.read_more));
            binding.includedLayout.expandableLayout.collapse();
        } else {
            binding.expandButton.setText(getString(R.string.read_less));
            binding.includedLayout.expandableLayout.expand();
        }
    }

}
