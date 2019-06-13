package com.joe.preview.ui.custom.menu;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.joe.preview.R;
import com.joe.preview.utils.PreviewUtil;

import java.util.ArrayList;
import java.util.List;

class ViewAnimator {

    private final int ANIMATION_DURATION = 175;

    private int screenWidth;
    private int screenHeight;
    private int selectedPosition = 0;

    private AppCompatActivity appCompatActivity;
    private List<SlideMenuItem> slideMenuItems;
    private DrawerLayout drawerLayout;
    private ViewAnimatorListener animatorListener;

    private List<View> viewList = new ArrayList<>();

    ViewAnimator(AppCompatActivity appCompatActivity,
                 List<SlideMenuItem> slideMenuItems,
                 DrawerLayout drawerLayout,
                 ViewAnimatorListener animatorListener) {
        this.appCompatActivity = appCompatActivity;
        this.slideMenuItems = slideMenuItems;
        this.drawerLayout = drawerLayout;
        this.animatorListener = animatorListener;

        screenWidth = PreviewUtil.getScreenWidth(appCompatActivity);
        screenHeight = PreviewUtil.getScreenHeight(appCompatActivity);
    }

    private void setViewsClickable() {
        animatorListener.updateHomeButton(false);
        for (View view : viewList)
            view.setEnabled(false);
    }

    void displayMenuContent() {
        setViewsClickable();
        viewList.clear();

        double size = slideMenuItems.size();
        for (int i = 0; i < size; i++) {
            View view = appCompatActivity.getLayoutInflater().inflate(R.layout.list_item_menu, null);

            updateMenuItemImageView(i, view);
            updateMenuItemContainerView(i, view);

            viewList.add(view);
            animatorListener.addViewToContainer(view);

            if (i == selectedPosition)
                viewList.get(i).setSelected(true);

            animateMenuItem((double) i, size);
        }
    }

    private void updateMenuItemImageView(int position, View view) {
        ImageView imageView = view.findViewById(R.id.menu_item_image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        double aspectRatioWidth = 11.1;
        Double width = Math.ceil((aspectRatioWidth * screenWidth) / 100);

        double aspectRatioHeight = 6.756;
        Double height = Math.ceil((aspectRatioHeight * screenHeight) / 100);

        layoutParams.width = width.intValue();
        layoutParams.height = height.intValue();

        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(slideMenuItems.get(position).getImageResolution());
    }

    private void updateMenuItemContainerView(int position, View viewMenu) {
        View container = viewMenu.findViewById(R.id.menu_item_container);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        container.setLayoutParams(layoutParams);

        double aspectRatioContainerHeight = 15.9;
        Double containerHeight = Math.ceil((aspectRatioContainerHeight * screenHeight) / 100);
        layoutParams.height = containerHeight.intValue();
        container.setLayoutParams(layoutParams);

        viewMenu.setVisibility(View.GONE);
        viewMenu.setEnabled(true);

        viewMenu.setOnClickListener(view -> {
            int[] location = {0, 0};
            view.getLocationOnScreen(location);
            switchItem(view, position, location[1] + view.getHeight() / 2);
        });
    }

    private void animateMenuItem(double position, double totalSize) {
        double delay = 3 * ANIMATION_DURATION * (position / totalSize);
        new Handler().postDelayed(() -> {
            if (position < viewList.size())
                animateView((int) position);
        }, (long) delay);
    }

    private void animateView(int position) {
        View view = viewList.get(position);
        view.setVisibility(View.VISIBLE);

        FlipAnimation flipAnimation = new FlipAnimation(90, 0, 0.0f, view.getHeight() / 2.0f);
        flipAnimation.setDuration(ANIMATION_DURATION);
        flipAnimation.setFillAfter(true);
        flipAnimation.setInterpolator(new AccelerateInterpolator());
        flipAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(flipAnimation);
    }

    private void animateHideView(int position) {
        View view = viewList.get(position);
        FlipAnimation flipAnimation = new FlipAnimation(0, 90, 0.0f, view.getHeight() / 2.0f);
        flipAnimation.setDuration(ANIMATION_DURATION);
        flipAnimation.setFillAfter(true);
        flipAnimation.setInterpolator(new AccelerateInterpolator());
        flipAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
                if (position == viewList.size() - 1) {
                    animatorListener.updateHomeButton(true);
                    drawerLayout.closeDrawers();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(flipAnimation);
    }

    private void hideMenuContent() {
        setViewsClickable();

        double size = slideMenuItems.size();

        for (int i = slideMenuItems.size(); i >= 0; i--) {
            double position = i;
            double delay = 3 * ANIMATION_DURATION * (position / size);

            new Handler().postDelayed(() -> {
                if (position < viewList.size())
                    animateHideView((int) position);
            }, (long) delay);
        }
    }

    private void switchItem(View view, int selectedPosition, int topPosition) {
        if (getSelectedPosition() != selectedPosition && selectedPosition != viewList.size() - 1) {
            view.setSelected(true);
            updateSelectedView(view);
            this.selectedPosition = selectedPosition;
            animatorListener.onSwitch(selectedPosition, topPosition);
        }

        hideMenuContent();
    }

    private void updateSelectedView(View view) {
        for (int i = 0; i < viewList.size(); i++) {
            if (view.getId() != viewList.get(0).getId())
                viewList.get(i).setSelected(false);
        }
    }

    int getSelectedPosition() {
        return selectedPosition;
    }

    public interface ViewAnimatorListener {
        void onSwitch(int selectedPosition, int topPosition);

        void updateHomeButton(boolean enabled);

        void addViewToContainer(View view);
    }

}
