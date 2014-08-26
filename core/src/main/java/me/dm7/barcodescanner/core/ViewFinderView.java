package me.dm7.barcodescanner.core;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ViewFinderView extends View {
    private static final String TAG = "ViewFinderView";

    private Rect mFramingRect;
    private Rect mOffsetFramingRect;

    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;

    private static final float LANDSCAPE_WIDTH_RATIO = 5f / 8;
    private static final float LANDSCAPE_HEIGHT_RATIO = 5f / 8;
    private static final int LANDSCAPE_MAX_FRAME_WIDTH = (int) (1920 * LANDSCAPE_WIDTH_RATIO); // = 5/8 * 1920
    private static final int LANDSCAPE_MAX_FRAME_HEIGHT = (int) (1080 * LANDSCAPE_HEIGHT_RATIO); // = 5/8 * 1080

    private static final float PORTRAIT_WIDTH_RATIO = 8f / 8;
    private static final float PORTRAIT_HEIGHT_RATIO = 3f / 8;
    private static final int PORTRAIT_MAX_FRAME_WIDTH = (int) (1080 * PORTRAIT_WIDTH_RATIO); // = 8/8 * 1080
    private static final int PORTRAIT_MAX_FRAME_HEIGHT = (int) (1920 * PORTRAIT_HEIGHT_RATIO); // = 3/8 * 1920

    public ViewFinderView(Context context) {
        super(context);
    }

    public ViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setupViewFinder() {
        updateFramingRect();
        invalidate();
    }

    public Rect getFramingRect() {
        return mOffsetFramingRect;
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
        paint.setColor(resources.getColor(R.color.viewfinder_mask_test));

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawRect(0, 0, width, mFramingRect.top, paint);
        canvas.drawRect(0, mFramingRect.top, mFramingRect.left, mFramingRect.bottom + 1, paint);
        canvas.drawRect(mFramingRect.right + 1, mFramingRect.top, width, mFramingRect.bottom + 1, paint);
        canvas.drawRect(0, mFramingRect.bottom + 1, width, height, paint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Paint paint = new Paint();
        Resources resources = getResources();
        paint.setColor(resources.getColor(R.color.viewfinder_border));
        paint.setStyle(Paint.Style.STROKE);
        int lineWidth = resources.getInteger(R.integer.viewfinder_border_width);
        float halfLineWidth = (float) lineWidth / 2;
        paint.setStrokeWidth(lineWidth);

        float padding = resources.getDimension(R.dimen.viewfinder_border_padding);
        float halfPadding = padding / 2;

        float leftX = mFramingRect.left + padding;
        float rightX = mFramingRect.right - padding;
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
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        if (viewResolution == null) {
            return;
        }
        int width;
        int height;
        int orientation = DisplayUtils.getScreenOrientation(getContext());

        if (orientation != Configuration.ORIENTATION_PORTRAIT) {
            width = findDesiredDimensionInRange(LANDSCAPE_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, LANDSCAPE_MAX_FRAME_WIDTH);
            height = findDesiredDimensionInRange(LANDSCAPE_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, LANDSCAPE_MAX_FRAME_HEIGHT);
        } else {
            width = findDesiredDimensionInRange(PORTRAIT_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, PORTRAIT_MAX_FRAME_WIDTH);
            height = findDesiredDimensionInRange(PORTRAIT_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, PORTRAIT_MAX_FRAME_HEIGHT);
        }

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);

        int offset = getResources().getDimensionPixelSize(R.dimen.viewfinder_border_padding);
        mOffsetFramingRect = new Rect(mFramingRect.left + offset, mFramingRect.top, mFramingRect.right - offset, mFramingRect.bottom);
    }

    private static int findDesiredDimensionInRange(float ratio, int resolution, int hardMin, int hardMax) {
        int dim = (int) (ratio * resolution);
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }

}
