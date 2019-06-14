package com.joe.preview.ui.custom.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

public class AspectRatioCardView extends MaterialCardView {

    public AspectRatioCardView(Context context) {
        this(context, null);
    }

    public AspectRatioCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float ratio = 1.4f;
        int ratioHeight = (int) (getMeasuredWidth() * ratio);
        setMeasuredDimension(getMeasuredWidth(), ratioHeight);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ratioHeight;
        setLayoutParams(layoutParams);
    }

}
