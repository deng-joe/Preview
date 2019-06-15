package com.joe.preview.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.data.remote.model.Review;
import com.joe.preview.databinding.ReviewItemBinding;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public ReviewListAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReviewItemBinding itemBinding = ReviewItemBinding.inflate(inflater, parent, false);
        return new ReviewViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getItem(position);
        holder.binding.itemReviewTitle.setText(review.getAuthor());
        holder.binding.itemReviewDesc.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public Review getItem(int position) {
        return reviews.get(position);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ReviewItemBinding binding;

        ReviewViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
