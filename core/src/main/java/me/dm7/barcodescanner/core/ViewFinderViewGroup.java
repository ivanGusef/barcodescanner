package me.dm7.barcodescanner.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewFinderViewGroup extends LinearLayout {

    private static final String TAG = "ViewFinderView";

    private TextView mDescriptionTextView, mResultTextView;
    private CaptureView mCaptureView;

    public ViewFinderViewGroup(Context context) {
        this(context, null);
    }

    public ViewFinderViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ViewFinderViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        initViews(context);
    }

    private void initViews(Context context) {
        inflate(context, R.layout.merge_view_finder_view_group, this);

        mDescriptionTextView = (TextView) findViewById(R.id.description_text_view);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mCaptureView = (CaptureView) findViewById(R.id.capture_view);
    }

    public Rect getFramingRect() {
        return mCaptureView.getFramingRect();
    }

    public void setDescriptionText(CharSequence text) {
        mDescriptionTextView.setText(text);
    }

    public void setDescriptionText(int textRes) {
        mDescriptionTextView.setText(textRes);
    }

    public void setDescriptionTextSize(int textSizeInSp) {
        mDescriptionTextView.setTextSize(textSizeInSp);
    }

    public void setDescriptionTextColor(ColorStateList textColor) {
        mDescriptionTextView.setTextColor(textColor);
    }

    public void setResultText(CharSequence text) {
        mResultTextView.setText(text);
    }

    public void setResultText(int textRes) {
        mResultTextView.setText(textRes);
    }

    public void setResultTextSize(int textSizeInSp) {
        mResultTextView.setTextSize(textSizeInSp);
    }

    public void setResultTextColor(ColorStateList textColor) {
        mResultTextView.setTextColor(textColor);
    }
}
