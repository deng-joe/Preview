package com.joe.preview.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.joe.preview.R;
import com.joe.preview.constants.PreviewConstants;
import com.joe.preview.databinding.VideoActivityBinding;

import es.dmoral.toasty.Toasty;

import static com.joe.preview.constants.PreviewConstants.INTENT_VIDEO_KEY;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    VideoActivityBinding binding;
    String videoKey;
    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
    }

    private void initializeView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        binding.youtubeView.initialize(PreviewConstants.YOUTUBE_API_KEY, this);
        videoKey = getIntent().getStringExtra(INTENT_VIDEO_KEY);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setFullscreen(true);
        youTubePlayer.loadVideo(videoKey);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError())
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toasty.error(this, error, Toast.LENGTH_LONG).show();
        }
    }

}
