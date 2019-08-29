package com.joe.preview.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.R;
import com.joe.preview.data.local.entity.Series;
import com.joe.preview.databinding.MoviesListItemBinding;
import com.joe.preview.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class SeriesSearchListAdapter extends RecyclerView.Adapter<SeriesSearchListAdapter.SeriesSearchViewHolder> {

    private Activity activity;
    private List<Series> trailers;

    public SeriesSearchListAdapter(Activity activity) {
        this.activity = activity;
        trailers = new ArrayList<>();
    }

    @NonNull
    @Override
    public SeriesSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(inflater, parent, false);
        return new SeriesSearchViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesSearchViewHolder holder, int position) {
        Series trailer = getItem(position);
        holder.bindTo(trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public Series getItem(int position) {
        return trailers.get(position);
    }

    public void setTrailers(List<Series> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }


    class SeriesSearchViewHolder extends RecyclerView.ViewHolder {

        private MoviesListItemBinding binding;

        SeriesSearchViewHolder(MoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            itemView.setLayoutParams(new RecyclerView.LayoutParams(Float.valueOf(width * 0.85f).intValue(),
                    RecyclerView.LayoutParams.WRAP_CONTENT));
        }

        private void bindTo(Series trailer) {
            GlideApp.with(activity).load(trailer.getPosterPath()).placeholder(R.drawable.ic_image).into(binding.image);
        }
    }

}
