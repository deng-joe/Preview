package com.joe.preview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.joe.preview.R;
import com.joe.preview.constants.PreviewConstants;
import com.joe.preview.data.remote.model.Video;
import com.joe.preview.databinding.VideoListItemBinding;
import com.joe.preview.utils.PreviewUtil;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private int screenWidth;
    private int screenHeight;

    private List<Video> videos;

    public VideoListAdapter(Context context, List<Video> videos) {
        this.videos = videos;
        screenWidth = PreviewUtil.getScreenWidth(context);
        screenHeight = PreviewUtil.getScreenHeight(context);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VideoListItemBinding itemBinding = VideoListItemBinding.inflate(inflater, parent, false);
        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.binding.youtubeThumbnail.initialize(PreviewConstants.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeThumbnailLoader youTubeThumbnailLoader) {
                Video video = getItem(position);
                youTubeThumbnailLoader.setVideo(video.getKey());
                youTubeThumbnailView.setImageBitmap(null);

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        holder.binding.videoFrame.setVisibility(View.VISIBLE);
                        holder.binding.buttonPlay.setImageResource(R.drawable.ic_play);
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                                 YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public Video getItem(int position) {
        return videos.get(position);
    }


    class VideoViewHolder extends RecyclerView.ViewHolder {

        private VideoListItemBinding binding;

        VideoViewHolder(VideoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            ViewGroup.LayoutParams layoutParams = binding.youtubeThumbnail.getLayoutParams();

            double aspectRatioWidth = 61.1;
            double aspectRatioHeight = 20.27;

            Double width = Math.ceil((aspectRatioWidth * screenWidth) / 100);
            Double height = Math.ceil((aspectRatioHeight * screenHeight) / 100);

            layoutParams.width = width.intValue();
            layoutParams.height = height.intValue();
        }
    }

}
