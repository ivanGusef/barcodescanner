package me.dm7.barcodescanner.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ivan_Gusev1 on 8/27/2014.
 */
public class CaptureView extends View {

    private Rect mFramingRect;

    public CaptureView(Context context) {
        this(context, null);
    }

    public CaptureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mFramingRect == null) {
            return;
        }

        drawViewFinderMask(canvas);
        drawViewFinderBorder(canvas);
    }

    public void drawViewFinderMask(Canvas canvas) {
        Paint paint = new Paint();
        Resources resources = getResources();
        paint.setColor(resources.getColor(R.color.viewfinder_mask));

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawRect(0, 0, width, mFramingRect.top, paint);
        /*canvas.drawRect(0, mFramingRect.top, mFramingRect.left, mFramingRect.bottom + 1, paint);
        canvas.drawRect(mFramingRect.right + 1, mFramingRect.top, width, mFramingRect.bottom + 1, paint);*/
        canvas.drawRect(0, mFramingRect.bottom + 1, width, height, paint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Paint paint = new Paint();
        Resources resources = getResources();
        paint.setColor(resources.getColor(R.color.viewfinder_border));
        paint.setStyle(Paint.Style.STROKE);
        float lineWidth = resources.getDimension(R.dimen.viewfinder_border_width);
        float halfLineWidth = lineWidth / 2;
        paint.setStrokeWidth(lineWidth);

        float padding = resources.getDimension(R.dimen.viewfinder_border_padding);
        float halfPadding = padding / 2;

        float leftX = mFramingRect.left;
        float rightX = mFramingRect.right;
        float topStartY = mFramingRect.top;
        float topEndY = mFramingRect.top - halfPadding;

        // Line 1
        canvas.drawLine(leftX, topStartY, leftX, topEndY, paint);
        float leftXOffsetNegativeByLineWidth = leftX - halfLineWidth;
        float rightXOffsetPositiveByLineWidth = rightX + halfLineWidth;
        float topEndYOffsetNegativeByLineWidth = topEndY - halfLineWidth;
        // Line 2
        canvas.drawLine(leftXOffsetNegativeByLineWidth, topEndYOffsetNegativeByLineWidth, rightXOffsetPositiveByLineWidth, topEndYOffsetNegativeByLineWidth, paint);
        // Line 3
        canvas.drawLine(rightX, topStartY, rightX, topEndY, paint);

        float bottomStartY = mFramingRect.bottom + 1;
        float bottomEndY = mFramingRect.bottom + halfPadding;
        // Line 4
        canvas.drawLine(leftX, bottomStartY, leftX, bottomEndY, paint);
        float bottomEndYOffsetPositiveByLineWidth = bottomEndY + halfLineWidth;
        // Line 5
        canvas.drawLine(leftXOffsetNegativeByLineWidth, bottomEndYOffsetPositiveByLineWidth, rightXOffsetPositiveByLineWidth, bottomEndYOffsetPositiveByLineWidth, paint);
        // Line 6
        canvas.drawLine(rightX, bottomStartY, rightX, bottomEndY, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateFramingRect();
    }

    private void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());

        int width = getWidth();
        int height = getHeight();

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;

        int offset = getResources().getDimensionPixelSize(R.dimen.viewfinder_border_padding);
        mFramingRect = new Rect(leftOffset + offset, topOffset + offset, leftOffset + width - offset, topOffset + height - offset);
    }

    public Rect getFramingRect() {
        return mFramingRect;
    }
}
