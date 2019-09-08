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
import com.joe.preview.adapter.SimilarMoviesListAdapter;
import com.joe.preview.adapter.VideoListAdapter;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.data.remote.model.Cast;
import com.joe.preview.data.remote.model.Crew;
import com.joe.preview.data.remote.model.Review;
import com.joe.preview.data.remote.model.Video;
import com.joe.preview.databinding.DetailActivityBinding;
import com.joe.preview.factory.ViewModelFactory;
import com.joe.preview.ui.custom.recyclerview.RecyclerItemClickListener;
import com.joe.preview.utils.NavigationUtil;
import com.joe.preview.utils.PreviewUtil;
import com.joe.preview.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MovieDetailActivity extends BaseActivity {

    DetailActivityBinding binding;
    MovieDetailViewModel movieDetailViewModel;

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
        Movie movie = getIntent().getParcelableExtra(INTENT_MOVIE);
        Picasso.get().load(movie.getPosterPath()).into(binding.image);
        ViewCompat.setTransitionName(binding.image, TRANSITION_IMAGE_NAME);
        binding.expandButton.setPaintFlags(binding.expandButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initializeViewModel() {
        movieDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel.class);
        movieDetailViewModel.fetchMovieDetails(getIntent().getParcelableExtra(INTENT_MOVIE));
        movieDetailViewModel.getMovieDetailsLiveData().observe(this, movie -> {
            updateMovieDetailsView(movie);
            if (movie.getVideos() != null && !movie.getVideos().isEmpty())
                updateMovieVideos(movie.getVideos());
            if (movie.getCrews() != null && !movie.getCrews().isEmpty())
                updateMovieCrewDetails(movie.getCrews());
            if (movie.getCasts() != null && !movie.getCasts().isEmpty()) {
                binding.expandButton.setVisibility(View.VISIBLE);
                updateMovieCastDetails(movie.getCasts());
            }
            if (movie.getSimilarMovies() != null && !movie.getSimilarMovies().isEmpty())
                updateSimilarMoviesView(movie.getSimilarMovies());
            if (movie.getReviews() != null && !movie.getReviews().isEmpty())
                updateMovieReviews(movie.getReviews());
            else
                binding.includedReviewsLayout.reviews.setVisibility(View.GONE);
        });
    }

    private void updateMovieDetailsView(Movie movie) {
        binding.movieTitle.setText(movie.getHeader());
        binding.movieDesc.setText(movie.getDescription());
        if (movie.getStatus() != null)
            binding.movieStatus.setItems(Collections.singletonList(movie.getStatus()));
        binding.collectionItemPicker.setUseRandomColor(true);
        if (movie.getGenres() != null)
            binding.collectionItemPicker.setItems(PreviewUtil.getGenres(movie.getGenres()));
        binding.runtime.setText(PreviewUtil.getRuntimeInMinutes(movie.getStatus(), movie.getRuntime(), movie.getReleaseDate()));
        binding.ratingLabel.setText(R.string.rating);
        binding.rating.setText(String.format("%s/10", movie.getVoteAverage()));
        binding.budgetLabel.setText(R.string.budget);
        binding.budget.setText(String.format("$%s", NumberFormat.getInstance(Locale.getDefault()).format(movie.getBudget())));
        binding.revenueLabel.setText(R.string.revenue);
        binding.revenue.setText(String.format("$%s", NumberFormat.getInstance(Locale.getDefault()).format(movie.getRevenue())));
    }

    private void updateMovieVideos(List<Video> videos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.smoothScrollToPosition(1);

        VideoListAdapter videoListAdapter = new VideoListAdapter(getApplicationContext(), videos);
        binding.recyclerView.setAdapter(videoListAdapter);
        binding.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (parentView, childView, position) ->
                NavigationUtil.redirectToVideo(this, videoListAdapter.getItem(position).getKey())
        ));
    }

    private void updateMovieCastDetails(List<Cast> casts) {
        binding.includedLayout.castList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.castList.setVisibility(View.VISIBLE);

        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), casts);
        binding.includedLayout.castList.setAdapter(creditListAdapter);
    }

    private void updateMovieCrewDetails(List<Crew> crews) {
        binding.includedLayout.crewList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.crewList.setVisibility(View.VISIBLE);

        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), CREDIT_CREW, crews);
        binding.includedLayout.crewList.setAdapter(creditListAdapter);
    }

    private void updateMovieReviews(List<Review> reviews) {
        binding.includedReviewsLayout.reviewsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.includedReviewsLayout.reviewsList.setVisibility(View.VISIBLE);

        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviews);
        binding.includedReviewsLayout.reviewsList.setAdapter(reviewListAdapter);
        binding.includedReviewsLayout.reviews.setVisibility(View.VISIBLE);
    }

    private void updateSimilarMoviesView(List<Movie> movies) {
        binding.includedSimilarLayout.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.includedSimilarLayout.moviesList.setVisibility(View.VISIBLE);

        SimilarMoviesListAdapter similarMoviesListAdapter = new SimilarMoviesListAdapter(this, movies);
        binding.includedSimilarLayout.moviesList.setAdapter(similarMoviesListAdapter);

        binding.includedSimilarLayout.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(
                this, (parentView, childView, position) -> {
            Movie movie = similarMoviesListAdapter.getItem(position);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    new Pair(childView, TRANSITION_IMAGE_NAME));
            NavigationUtil.redirectToMovieDetails(this, movie, optionsCompat);
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
