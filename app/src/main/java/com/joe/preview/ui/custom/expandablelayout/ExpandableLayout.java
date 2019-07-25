package com.joe.preview.ui.custom.expandablelayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.joe.preview.R;

import static com.joe.preview.ui.custom.expandablelayout.ExpandableLayout.State.COLLAPSED;
import static com.joe.preview.ui.custom.expandablelayout.ExpandableLayout.State.COLLAPSING;
import static com.joe.preview.ui.custom.expandablelayout.ExpandableLayout.State.EXPANDED;
import static com.joe.preview.ui.custom.expandablelayout.ExpandableLayout.State.EXPANDING;

public class ExpandableLayout extends FrameLayout {

    public static final String KEY_SUPER_STATE = "super_state";
    public static final String KEY_EXPANSION = "expansion";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final int DEFAULT_DURATION = 300;

    private int duration = DEFAULT_DURATION;
    private float parallax;
    private float expansion;
    private int orientation;
    private int state;

    private Interpolator interpolator = new FastOutSlowInInterpolator();
    private ValueAnimator animator;

    private OnExpansionUpdateListener listener;

    public ExpandableLayout(@NonNull Context context) {
        this(context, null);
    }

    public ExpandableLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
            duration = typedArray.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION);
            expansion = typedArray.getBoolean(R.styleable.ExpandableLayout_el_expanded, false) ? 1 : 0;
            orientation = typedArray.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL);
            parallax = typedArray.getFloat(R.styleable.ExpandableLayout_el_parallax, 1);

            typedArray.recycle();

            state = expansion == 0 ? COLLAPSED : EXPANDED;

            setParallax(parallax);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle bundle = new Bundle();

        expansion = isExpanded() ? 1 : 0;

        bundle.putFloat(KEY_EXPANSION, expansion);
        bundle.putParcelable(KEY_SUPER_STATE, superState);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        expansion = bundle.getFloat(KEY_EXPANSION);
        state = expansion == 1 ? EXPANDED : COLLAPSED;
        Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);

        super.onRestoreInstanceState(superState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int size = orientation == LinearLayout.HORIZONTAL ? width : height;

        setVisibility(expansion == 0 && size == 0 ? GONE : VISIBLE);

        int expansionDelta = size - Math.round(size * expansion);

        if (parallax > 0) {
            float parallaxDelta = expansionDelta * parallax;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (orientation == HORIZONTAL) {
                    int direction = -1;
                    if (getLayoutDirection() == LAYOUT_DIRECTION_RTL)
                        direction = 1;
                    child.setTranslationX(direction * parallaxDelta);
                } else
                    child.setScaleY(-parallaxDelta);
            }
        }

        if (orientation == HORIZONTAL)
            setMeasuredDimension(width - expansionDelta, height);
        else
            setMeasuredDimension(width, height - expansionDelta);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (animator != null)
            animator.cancel();
        super.onConfigurationChanged(newConfig);
    }

    public int getState() {
        return state;
    }

    public boolean isExpanded() {
        return state == EXPANDING || state == EXPANDED;
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (isExpanded())
            collapse(animate);
        else
            expand(animate);
    }

    public void expand() {
        expand(true);
    }

    public void expand(boolean animate) {
        setExpanded(true, animate);
    }

    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean animate) {
        setExpanded(false, animate);
    }

    public void setExpanded(boolean expand) {
        setExpanded(expand, true);
    }

    public void setExpanded(boolean expand, boolean animate) {
        if (expand == isExpanded())
            return;

        int targetExpansion = expand ? 1 : 0;

        if (animate)
            animateSize(targetExpansion);
        else
            setExpansion(targetExpansion);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public float getExpansion() {
        return expansion;
    }

    public void setExpansion(float expansion) {
        if (this.expansion == expansion)
            return;

        // Infer state from previous value
        float delta = expansion - this.expansion;
        if (expansion == 0)
            state = COLLAPSED;
        else if (expansion == 1)
            state = EXPANDED;
        else if (delta < 0)
            state = COLLAPSING;
        else if (delta > 0)
            state = EXPANDING;

        setVisibility(state == COLLAPSED ? GONE : VISIBLE);

        this.expansion = expansion;

        requestLayout();

        if (listener != null)
            listener.onExpansionUpdate(expansion, state);
    }

    public float getParallax() {
        return parallax;
    }

    public void setParallax(float parallax) {
        // Make sure parallax is between 0 and 1
        parallax = Math.min(1, Math.max(0, parallax));
        this.parallax = parallax;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        if (orientation < 0 || orientation > 1)
            throw new IllegalArgumentException("Orientation must be either 0 (horizontal) or 1 (vertical)");
        this.orientation = orientation;
    }

    public void setListener(OnExpansionUpdateListener listener) {
        this.listener = listener;
    }

    private void animateSize(int targetExpansion) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }

        animator = ValueAnimator.ofFloat(expansion, targetExpansion);
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.addUpdateListener(valueAnimator -> setExpansion((float) valueAnimator.getAnimatedValue()));
        animator.addListener(new ExpansionListener(targetExpansion));
        animator.start();
    }


    public interface State {
        int COLLAPSED = 0;
        int COLLAPSING = 1;
        int EXPANDING = 2;
        int EXPANDED = 3;
    }

    public interface OnExpansionUpdateListener {
        void onExpansionUpdate(float expansionFraction, int state);
    }

    private class ExpansionListener implements Animator.AnimatorListener {

        private int targetExpansion;
        private boolean canceled;

        ExpansionListener(int targetExpansion) {
            this.targetExpansion = targetExpansion;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            state = targetExpansion == 0 ? COLLAPSING : EXPANDING;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!canceled) {
                state = targetExpansion == 0 ? COLLAPSED : EXPANDED;
                setExpansion(targetExpansion);
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            canceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

}
