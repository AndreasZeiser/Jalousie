package com.andreaszeiser.jalousie;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class IndicatorText extends TextView implements IndicatorElement {

	private static final String TAG = IndicatorText.class.getSimpleName();

	private int mState = IndicatorElement.STATE_COLLAPSED;

	// to be documented
	private String mExpandIndicatorText;

	private String mCollapseIndicatorText;

	public IndicatorText(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		init(context, attrs);
	}

	public IndicatorText(Context context, AttributeSet attrs) {

		super(context, attrs);

		init(context, attrs);
	}

	public IndicatorText(Context context) {

		super(context);

		init(context, null);
	}

	/**
	 * Should only be called from constructor.
	 */
	private void init(final Context context, final AttributeSet attrs) {

		if (attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.Indicator, 0, 0);

			try {
				mExpandIndicatorText = a
						.getString(R.styleable.Indicator_expandIndicatorText);
				if (mExpandIndicatorText == null) {
					mExpandIndicatorText = "";
				}

				mCollapseIndicatorText = a
						.getString(R.styleable.Indicator_collapseIndicatorText);
				if (mCollapseIndicatorText == null) {
					mCollapseIndicatorText = "";
				}
			} finally {
				a.recycle();
			}
		} else {
			setDefaults();
		}

		Log.v(TAG, "[init] expandIndicatorText=" + mExpandIndicatorText);
		Log.v(TAG, "[init] collapseIndicatorText=" + mCollapseIndicatorText);

		setText(mExpandIndicatorText);
	}

	private void setDefaults() {

		mExpandIndicatorText = "";
		mCollapseIndicatorText = "";
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		Log.v(TAG, "[onSizeChanged] w=" + w + ", h=" + h + ", oldw=" + oldw
				+ ", oldh=" + oldh);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public int getState() {

		return mState;
	}

	@Override
	public void setState(int indicatorState) {
		switch (indicatorState) {
		case IndicatorElement.STATE_COLLAPSED:

			setText(mCollapseIndicatorText);

			mState = IndicatorElement.STATE_COLLAPSED;
			break;

		case IndicatorElement.STATE_EXPANDED:

			setText(mExpandIndicatorText);

			mState = IndicatorElement.STATE_EXPANDED;
			break;

		default:

			break;
		}
	}

}
