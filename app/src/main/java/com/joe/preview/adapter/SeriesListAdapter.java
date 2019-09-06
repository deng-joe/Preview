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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SeriesListAdapter extends RecyclerView.Adapter<SeriesListAdapter.SeriesViewHolder> {

    private Activity activity;
    private List<Series> seriesList;

    public SeriesListAdapter(Activity activity) {
        this.activity = activity;
        seriesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(inflater, parent, false);
        return new SeriesViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public Series getItem(int position) {
        return seriesList.get(position);
    }

    public void setItems(List<Series> seriesList) {
        int startPosition = this.seriesList.size();
        this.seriesList.addAll(seriesList);
        notifyItemRangeChanged(startPosition, seriesList.size());
    }


    class SeriesViewHolder extends RecyclerView.ViewHolder {

        private MoviesListItemBinding binding;

        SeriesViewHolder(MoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            itemView.setLayoutParams(new RecyclerView.LayoutParams(Float.valueOf(width * 0.85f).intValue(),
                    RecyclerView.LayoutParams.WRAP_CONTENT));
        }

        void bindTo(Series series) {
            Picasso.get()
                    .load(series.getPosterPath())
                    .placeholder(R.drawable.ic_image)
                    .into(binding.image);
        }
    }

}
