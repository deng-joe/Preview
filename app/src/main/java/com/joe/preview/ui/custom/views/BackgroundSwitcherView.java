package com.joe.preview.ui.custom.views;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.joe.preview.utils.AnimationUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BackgroundSwitcherView extends ImageSwitcher {

    private final int[] NORMAL_ORDER = new int[]{0, 1};

    private int backgroundImageGap;
    private int backgroundImageWidth;

    private Animation backgroundImageInLeftAnimation;
    private Animation backgroundImageOutLeftAnimation;

    private Animation backgroundImageInRightAnimation;
    private Animation backgroundImageOutRightAnimation;

    private AnimationDirection currentAnimationDirection;

    public BackgroundSwitcherView(Context context) {
        super(context);
        inflateAndInitialize(context);
    }

    public BackgroundSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateAndInitialize(context);
    }

    private void inflateAndInitialize(Context context) {
        setChildrenDrawingOrderEnabled(true);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthBackgroundImageGapPercent = 12;
        backgroundImageGap = (displayMetrics.widthPixels / 100) * widthBackgroundImageGapPercent;
        backgroundImageWidth = displayMetrics.widthPixels + backgroundImageGap * 2;

        this.setFactory(() -> {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LayoutParams(backgroundImageWidth, LayoutParams.MATCH_PARENT));
            imageView.setTranslationX(-backgroundImageGap);
            return imageView;
        });

        int movementDuration = 500;
        backgroundImageInLeftAnimation = AnimationUtil.createBackgroundImageInAnimation(backgroundImageGap, 0, movementDuration);
        backgroundImageOutLeftAnimation = AnimationUtil.createBackgroundImageOutAnimation(0, -backgroundImageGap, movementDuration);

        backgroundImageInRightAnimation = AnimationUtil.createBackgroundImageInAnimation(-backgroundImageGap, 0, movementDuration);
        backgroundImageOutRightAnimation = AnimationUtil.createBackgroundImageOutAnimation(0, backgroundImageGap, movementDuration);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return NORMAL_ORDER[i];
    }

    private synchronized void setImageBitmapWithAnimation(Bitmap bitmap, AnimationDirection animationDirection) {
        if (animationDirection == AnimationDirection.LEFT) {
            this.setInAnimation(backgroundImageInLeftAnimation);
            this.setOutAnimation(backgroundImageOutLeftAnimation);
            this.setImageBitmap(bitmap);
        } else if (animationDirection == AnimationDirection.RIGHT) {
            this.setInAnimation(backgroundImageInRightAnimation);
            this.setOutAnimation(backgroundImageOutRightAnimation);
            this.setImageBitmap(bitmap);
        }
    }

    public void updateCurrentBackground(String imageUrl) {
        currentAnimationDirection = AnimationDirection.RIGHT;

        ImageView imageView = (ImageView) this.getNextView();
        imageView.setImageDrawable(null);
        showNext();

        if (imageUrl == null)
            return;

        Picasso.get().load(imageUrl).noFade().noPlaceholder().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                setImageBitmapWithAnimation(bitmap, currentAnimationDirection);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("BackgroundSwitcherView", "Failed to load image. " + e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void setImageBitmap(Bitmap bitmap) {
        ImageView imageView = (ImageView) this.getNextView();
        imageView.setImageDrawable(null);

        int duration = 0;

        animate().alpha(0.0f).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                imageView.setImageBitmap(bitmap);
                new Handler().postDelayed(() -> animate().alpha(0.4f).setDuration(duration), 200);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        showNext();
    }

    public void clearImage() {
        ImageView imageView = (ImageView) this.getNextView();
        imageView.setImageDrawable(null);
        showNext();
    }

    public enum AnimationDirection {
        LEFT,
        RIGHT
    }
}
