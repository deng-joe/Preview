package com.joe.preview;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.databinding.MoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {

    private Activity activity;
    private List<Movie> movies;

    public MoviesListAdapter(Activity activity) {
        this.activity = activity;
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(inflater, parent, false);
        return new MovieViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

    public void setItems(List<Movie> movies) {
        int startPosition = this.movies.size();
        this.movies.addAll(movies);
        notifyItemRangeChanged(startPosition, movies.size());
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {

        private MoviesListItemBinding binding;

        MovieViewHolder(MoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            itemView.setLayoutParams(new RecyclerView.LayoutParams(Float.valueOf(width * 0.85f).intValue(),
                    RecyclerView.LayoutParams.WRAP_CONTENT));
        }

        void bindTo(Movie movie) {
            Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.ic_image).into(binding.image);
        }
    }

}
