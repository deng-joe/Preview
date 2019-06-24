package com.joe.preview.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.databinding.SimilarMoviesListItemBinding;
import com.joe.preview.glide.GlideApp;

import java.util.List;

public class SimilarMoviesListAdapter extends RecyclerView.Adapter<SimilarMoviesListAdapter.SimilarMoviesViewHolder> {

    private Activity activity;
    private List<Movie> movies;

    public SimilarMoviesListAdapter(Activity activity, List<Movie> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    @NonNull
    @Override
    public SimilarMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SimilarMoviesListItemBinding itemBinding = SimilarMoviesListItemBinding.inflate(inflater, parent, false);
        return new SimilarMoviesViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarMoviesViewHolder holder, int position) {
        Movie movie = getItem(position);
        String imageUrl = movie.getPosterPath();
        GlideApp.with(activity).load(imageUrl).into(holder.binding.itemImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }


    class SimilarMoviesViewHolder extends RecyclerView.ViewHolder {

        private SimilarMoviesListItemBinding binding;

        public SimilarMoviesViewHolder(SimilarMoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
