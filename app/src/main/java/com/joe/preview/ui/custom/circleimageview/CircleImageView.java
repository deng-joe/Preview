package com.joe.preview.ui.custom.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.joe.preview.R;

public class CircleImageView extends AppCompatImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private static final int COLOR_DRAWABLE_DIMENSION = 2;
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;

    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF DRAWABLE_RECT = new RectF();
    private final RectF BORDER_RECT = new RectF();

    private final Matrix SHADER_MATRIX = new Matrix();
    private final Paint BITMAP_PAINT = new Paint();
    private final Paint BORDER_PAINT = new Paint();
    private final Paint FILL_PAINT = new Paint();

    private int borderColor = DEFAULT_BORDER_COLOR;
    private int borderWidth = DEFAULT_BORDER_WIDTH;
    private int fillColor = DEFAULT_FILL_COLOR;

    private Bitmap bitmap;
    private BitmapShader bitmapShader;
    private int bitmapWidth;
    private int bitmapHeight;

    private float drawableRadius;
    private float borderRadius;

    private ColorFilter colorFilter;

    private boolean ready;
    private boolean setupPending;
    private boolean borderOverlay;
    private boolean disableCircularTransformation;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);

        borderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, DEFAULT_BORDER_WIDTH);
        borderColor = typedArray.getColor(R.styleable.CircleImageView_civ_border_color, DEFAULT_BORDER_COLOR);
        borderOverlay = typedArray.getBoolean(R.styleable.CircleImageView_civ_border_overlay, DEFAULT_BORDER_OVERLAY);
        fillColor = typedArray.getColor(R.styleable.CircleImageView_civ_fill_color, DEFAULT_FILL_COLOR);

        typedArray.recycle();

        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        ready = true;
        if (setupPending) {
            setup();
            setupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE)
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds)
            throw new IllegalArgumentException("adjustViewBounds not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (disableCircularTransformation) {
            super.onDraw(canvas);
            return;
        }

        if (bitmap == null)
            return;

        if (fillColor != Color.TRANSPARENT)
            canvas.drawCircle(DRAWABLE_RECT.centerX(), DRAWABLE_RECT.centerY(), drawableRadius, FILL_PAINT);

        canvas.drawCircle(DRAWABLE_RECT.centerX(), DRAWABLE_RECT.centerY(), drawableRadius, BITMAP_PAINT);

        if (borderWidth > 0)
            canvas.drawCircle(BORDER_RECT.centerX(), BORDER_RECT.centerY(), borderRadius, BORDER_PAINT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int start, int top, int end, int bottom) {
        super.setPadding(start, top, end, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == this.borderColor)
            return;

        this.borderColor = borderColor;
        BORDER_PAINT.setColor(this.borderColor);
        invalidate();
    }

    @Deprecated
    public void setBorderColorResource(@ColorRes int borderColorResource) {
        setBorderColor(getContext().getResources().getColor(borderColorResource));
    }

    @Deprecated
    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(@ColorInt int fillColor) {
        if (fillColor == this.fillColor)
            return;

        this.fillColor = fillColor;
        FILL_PAINT.setColor(fillColor);
        invalidate();
    }

    @Deprecated
    public void setFillColorResource(@ColorRes int fillColorResource) {
        setFillColor(getContext().getResources().getColor(fillColorResource));
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == this.borderWidth)
            return;

        this.borderWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return borderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == this.borderOverlay)
            return;

        this.borderOverlay = borderOverlay;
        setup();
    }

    public boolean isDisableCircularTransformation() {
        return disableCircularTransformation;
    }

    public void setDisableCircularTransformation(boolean disableCircularTransformation) {
        if (this.disableCircularTransformation == disableCircularTransformation)
            return;

        this.disableCircularTransformation = disableCircularTransformation;
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == colorFilter)
            return;

        colorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return colorFilter;
    }

    private void applyColorFilter() {
        BITMAP_PAINT.setColorFilter(colorFilter);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null)
            return null;

        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable)
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            else
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {
        if (disableCircularTransformation)
            bitmap = null;
        else
            bitmap = getBitmapFromDrawable(getDrawable());

        setup();
    }

    private void setup() {
        if (!ready) {
            setupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0)
            return;

        if (bitmap == null) {
            invalidate();
            return;
        }

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        BITMAP_PAINT.setAntiAlias(true);
        BITMAP_PAINT.setShader(bitmapShader);

        BORDER_PAINT.setStyle(Paint.Style.STROKE);
        BORDER_PAINT.setAntiAlias(true);
        BORDER_PAINT.setColor(borderColor);
        BORDER_PAINT.setStrokeWidth(borderWidth);

        FILL_PAINT.setStyle(Paint.Style.FILL);
        FILL_PAINT.setAntiAlias(true);
        FILL_PAINT.setColor(fillColor);

        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();

        BORDER_RECT.set(calculateBounds());
        borderRadius = Math.min((BORDER_RECT.height() - borderWidth) / 2.0f, (BORDER_RECT.width() - borderWidth) / 2.0f);

        DRAWABLE_RECT.set(BORDER_RECT);
        if (!borderOverlay && borderWidth > 0)
            DRAWABLE_RECT.inset(borderWidth - 1.0f, borderWidth - 1.0f);
        drawableRadius = Math.min(DRAWABLE_RECT.height() / 2.0f, DRAWABLE_RECT.width() / 2.0f);

        applyColorFilter();
        updateShaderMatrix();
        invalidate();
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingStart() - getPaddingEnd();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float start = getPaddingStart() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(start, top, start + sideLength, top + sideLength);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        SHADER_MATRIX.set(null);

        if (bitmapWidth * DRAWABLE_RECT.height() > DRAWABLE_RECT.width() * bitmapHeight) {
            scale = DRAWABLE_RECT.height() / bitmapHeight;
            dx = (DRAWABLE_RECT.width() - bitmapWidth * scale) * 0.5f;
        } else {
            scale = DRAWABLE_RECT.width() / bitmapWidth;
            dy = (DRAWABLE_RECT.height() - bitmapHeight * scale) * 0.5f;
        }

        SHADER_MATRIX.setScale(scale, scale);
        SHADER_MATRIX.postTranslate((dx + 0.5f) + DRAWABLE_RECT.left, (dy + 0.5f) + DRAWABLE_RECT.top);

        bitmapShader.setLocalMatrix(SHADER_MATRIX);
    }

}
