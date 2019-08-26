package com.joe.preview.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.data.local.entity.Series;
import com.joe.preview.databinding.SimilarMoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarSeriesListAdapter extends RecyclerView.Adapter<SimilarSeriesListAdapter.SimilarSeriesViewHolder> {

    private Activity activity;
    private List<Series> seriesList;

    public SimilarSeriesListAdapter(Activity activity, List<Series> seriesList) {
        this.activity = activity;
        this.seriesList = seriesList;
    }

    @NonNull
    @Override
    public SimilarSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SimilarMoviesListItemBinding itemBinding = SimilarMoviesListItemBinding.inflate(inflater, parent, false);
        return new SimilarSeriesViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarSeriesViewHolder holder, int position) {
        Series series = getItem(position);
        String imageUrl = series.getPosterPath();
        Picasso.get().load(imageUrl).into(holder.binding.itemImage);
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public Series getItem(int position) {
        return seriesList.get(position);
    }


    class SimilarSeriesViewHolder extends RecyclerView.ViewHolder {

        private SimilarMoviesListItemBinding binding;

        SimilarSeriesViewHolder(SimilarMoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
