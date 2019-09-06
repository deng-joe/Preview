package com.joe.preview.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.R;
import com.joe.preview.data.local.entity.Movie;
import com.joe.preview.databinding.MoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchListAdapter extends RecyclerView.Adapter<MovieSearchListAdapter.MovieSearchViewHolder> {

    private Activity activity;
    private List<Movie> trailers;

    public MovieSearchListAdapter(Activity activity) {
        this.activity = activity;
        trailers = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(inflater, parent, false);
        return new MovieSearchViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSearchViewHolder holder, int position) {
        Movie trailer = getItem(position);
        holder.bindTo(trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public Movie getItem(int position) {
        return trailers.get(position);
    }

    public void setItems(List<Movie> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }


    class MovieSearchViewHolder extends RecyclerView.ViewHolder {

        private MoviesListItemBinding binding;

        MovieSearchViewHolder(MoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            itemView.setLayoutParams(new RecyclerView.LayoutParams(Float.valueOf(width * 0.85f).intValue(),
                    RecyclerView.LayoutParams.WRAP_CONTENT));
        }

        private void bindTo(Movie trailer) {
            Picasso.get()
                    .load(trailer.getPosterPath())
                    .placeholder(R.drawable.ic_image)
                    .into(binding.image);
        }
    }

}
