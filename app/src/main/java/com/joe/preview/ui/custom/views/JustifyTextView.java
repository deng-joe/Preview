package com.joe.preview.ui.custom.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class JustifyTextView extends AppCompatTextView {

    private int lineY;
    private int viewWidth;

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        viewWidth = getMeasuredWidth();

        String text = getText().toString();

        lineY = 0;
        lineY += getTextSize();

        Layout layout = getLayout();
        if (layout == null)
            return;

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        int textHeight = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);
            if (needScale(line) && i < layout.getLineCount() - 1)
                drawScaledText(canvas, lineStart, line, width);
            else
                canvas.drawText(line, 0, lineY, textPaint);

            lineY += textHeight;
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float width) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, lineY, getPaint());

            float blanksWidth = StaticLayout.getDesiredWidth(blanks, getPaint());

            x += blanksWidth;

            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;

        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cardWidth = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, lineY, getPaint());
            x += cardWidth;
            i += 2;
        }

        float distance = (viewWidth - width) / gapCount;

        for (; i < line.length(); i++) {
            String string = String.valueOf(line.charAt(i));
            float cardWidth = StaticLayout.getDesiredWidth(string, getPaint());
            canvas.drawText(string, x, lineY, getPaint());
            x += cardWidth + distance;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0)
            return false;
        else
            return line.charAt(line.length() - 1) != '\n';
    }
}
