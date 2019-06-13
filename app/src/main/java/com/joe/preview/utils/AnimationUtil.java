package com.joe.preview.utils;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

    public static Animation createBackgroundImageInAnimation(int fromX, int toX, int transitionDuration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, 0, 0);
        translateAnimation.setDuration(transitionDuration);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }

    public static Animation createBackgroundImageOutAnimation(int fromX, int toX, int transitionDuration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, 0, 0);
        translateAnimation.setDuration(transitionDuration);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        return translateAnimation;
    }
}
