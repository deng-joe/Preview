package com.joe.preview.ui.custom.collectionpicker;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.joe.preview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CollectionPicker extends LinearLayout {

    private List<String> genreList = Arrays.asList("#febf9b", "#f47f87", "#6ac68d", "#fbe0a5");

    private LayoutInflater inflater;

    List<String> items = new ArrayList<>();

    private LinearLayout row;

    private HashMap<String, Object> checkedItems = new HashMap<>();

    private OnItemClickListener clickListener;

    private int width;

    private int itemMargin = 10;

    private int textPaddingStart = 5;
    private int textPaddingEnd = 5;
    private int textPaddingTop = 5;
    private int textPaddingBottom = 5;

    private int addIcon = android.R.drawable.ic_menu_add;
    private int cancelIcon = android.R.drawable.ic_menu_close_clear_cancel;

    private int layoutBackgroundColorNormal = R.color.blue;
    private int layoutBackgroundColorPressed = R.color.red;
    private int textColor = android.R.color.white;

    private int radius = 5;

    private boolean initialized;
    private boolean simplifiedTags;
    private boolean useRandomColor;

    public CollectionPicker(Context context) {
        this(context, null);
    }

    public CollectionPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollectionPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CollectionPicker);
        itemMargin = (int) typedArray.getDimension(R.styleable.CollectionPicker_cp_itemMargin, dpToPx(getContext(), itemMargin));
        textPaddingStart = (int) typedArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingStart,
                dpToPx(getContext(), textPaddingStart));
        textPaddingEnd = (int) typedArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingEnd,
                dpToPx(getContext(), textPaddingEnd));
        textPaddingTop = (int) typedArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingTop,
                dpToPx(getContext(), textPaddingTop));
        textPaddingBottom = (int) typedArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingBottom,
                dpToPx(getContext(), textPaddingBottom));
        addIcon = typedArray.getResourceId(R.styleable.CollectionPicker_cp_addIcon, addIcon);
        cancelIcon = typedArray.getResourceId(R.styleable.CollectionPicker_cp_cancelIcon, cancelIcon);
        typedArray.getColor(R.styleable.CollectionPicker_cp_itemBackgroundNormal,
                ContextCompat.getColor(getContext(), layoutBackgroundColorNormal));
        typedArray.getColor(R.styleable.CollectionPicker_cp_itemBackgroundPressed,
                ContextCompat.getColor(getContext(), layoutBackgroundColorPressed));
        radius = (int) typedArray.getDimension(R.styleable.CollectionPicker_cp_itemRadius, radius);
        typedArray.getColor(R.styleable.CollectionPicker_cp_itemTextColor, ContextCompat.getColor(getContext(), textColor));
        simplifiedTags = typedArray.getBoolean(R.styleable.CollectionPicker_cp_simplified, false);

        typedArray.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.START);

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            if (!initialized) {
                initialized = true;
                drawItemView();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
    }

    public HashMap<String, Object> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(HashMap<String, Object> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public void drawItemView() {
        if (!initialized)
            return;

        clearUi();

        float totalPadding = getPaddingStart() + getPaddingEnd();

        int indexFrontView = 0;

        LayoutParams itemParams = getItemLayoutParams();

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);

            View itemLayout = createItemView(item);

            TextView itemTextView = itemLayout.findViewById(R.id.item_text);
            itemTextView.setAllCaps(true);
            itemTextView.setTextSize(10);
            itemTextView.setText(item);
            itemTextView.setPadding(textPaddingStart, textPaddingTop, textPaddingEnd, textPaddingBottom);
            itemTextView.setTextColor(ContextCompat.getColor(getContext(), textColor));

            float itemWidth = itemTextView.getPaint().measureText(item) + textPaddingStart + textPaddingEnd;
            itemWidth += dpToPx(getContext(), 20) + textPaddingStart + textPaddingEnd;

            if (width <= (itemWidth + totalPadding)) {
                totalPadding = getPaddingStart() + getPaddingEnd();
                indexFrontView = i;
                addItemView(itemLayout, itemParams, true, i);
            } else {
                if (i != indexFrontView) {
                    itemParams.rightMargin = itemMargin;
                    totalPadding += itemMargin;
                }
                addItemView(itemLayout, itemParams, false, i);
            }
            totalPadding += itemWidth;
        }
    }

    private View createItemView(String item) {
        View view = inflater.inflate(R.layout.list_item_genre, this, false);
        view.setBackground(getSelector(item));
        return view;
    }

    private LayoutParams getItemLayoutParams() {
        LayoutParams itemParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemParams.bottomMargin = itemMargin / 2;
        itemParams.topMargin = 0;
        itemParams.rightMargin = itemMargin;

        return itemParams;
    }

    private int getItemIcon(Boolean isSelected) {
        return isSelected ? cancelIcon : addIcon;
    }

    private void clearUi() {
        removeAllViews();
        row = null;
    }

    private void addItemView(View view, ViewGroup.LayoutParams layoutParams, boolean newLine, int position) {
        if (row == null || newLine) {
            row = new LinearLayout(getContext());
            row.setGravity(Gravity.START);
            row.setOrientation(HORIZONTAL);

            ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            row.setLayoutParams(params);

            addView(row);
        }
    }

    private StateListDrawable getSelector(String item) {
        return getSelectorNormal();
    }

    private StateListDrawable getSelectorNormal() {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ContextCompat.getColor(getContext(), layoutBackgroundColorPressed));
        gradientDrawable.setCornerRadius(radius);

        states.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable);

        gradientDrawable = new GradientDrawable();

        int index = new Random().nextInt(genreList.size());

        int color = ContextCompat.getColor(getContext(), layoutBackgroundColorNormal);
        if (useRandomColor) {
            color = Color.parseColor(genreList.get(index));
        }

        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);

        states.addState(new int[]{}, gradientDrawable);

        return states;
    }

    public void getSelector(int colorCode) {
        layoutBackgroundColorNormal = colorCode;
        getSelectorNormal();
    }

    public boolean isUseRandomColor() {
        return useRandomColor;
    }

    public void setUseRandomColor(boolean useRandomColor) {
        this.useRandomColor = useRandomColor;
    }

    private StateListDrawable getSelectorSelected() {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ContextCompat.getColor(getContext(), layoutBackgroundColorNormal));
        gradientDrawable.setCornerRadius(radius);

        states.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable);

        gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ContextCompat.getColor(getContext(), layoutBackgroundColorPressed));
        gradientDrawable.setCornerRadius(radius);

        states.addState(new int[]{}, gradientDrawable);

        return states;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
        drawItemView();
    }

    public void clearItems() {
        items.clear();
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    private void animateView(View view) {
        view.setScaleY(1f);
        view.setScaleX(1f);
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).setStartDelay(0)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                reverseAnimation(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void reverseAnimation(View view) {
        view.setScaleY(1.2f);
        view.setScaleX(1.2f);
        view.animate().scaleX(1f).scaleY(1f).setDuration(100).setListener(null).start();
    }

    private void animateItemView(View view, int position) {
        long animationDelay = 600;
        animationDelay += position * 30;

        view.setScaleY(0);
        view.setScaleX(0);
        view.animate().scaleY(1).scaleX(1).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(null)
                .setStartDelay(animationDelay).start();
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    public interface OnItemClickListener {
        void onClick(String s, int position);
    }

}
