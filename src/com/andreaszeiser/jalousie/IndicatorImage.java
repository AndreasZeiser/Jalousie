package com.andreaszeiser.jalousie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class IndicatorImage extends ImageView implements IndicatorElement {

	private static final String TAG = IndicatorImage.class.getSimpleName();

	private int mState = IndicatorElement.STATE_COLLAPSED;

	private Drawable mExpandIndicator;

	private Drawable mCollapseIndicator;

	public IndicatorImage(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		init(context, attrs);
	}

	public IndicatorImage(Context context, AttributeSet attrs) {

		super(context, attrs);

		init(context, attrs);
	}

	public IndicatorImage(Context context) {

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
				mExpandIndicator = a
						.getDrawable(R.styleable.Indicator_expandIndicator);
				if (mExpandIndicator == null) {
					mExpandIndicator = getResources().getDrawable(
							R.drawable.ic_down);
				}

				mCollapseIndicator = a
						.getDrawable(R.styleable.Indicator_collapseIndicator);
				if (mCollapseIndicator == null) {
					mCollapseIndicator = getResources().getDrawable(
							R.drawable.ic_up);
				}
			} finally {
				a.recycle();
			}
		} else {
			setDefaults();
		}

		// set indicator
		setImageDrawable(mExpandIndicator);
	}

	private void setDefaults() {

		mExpandIndicator = getResources().getDrawable(R.drawable.ic_down);
		mCollapseIndicator = getResources().getDrawable(R.drawable.ic_up);
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

			setImageDrawable(mCollapseIndicator);

			mState = IndicatorElement.STATE_COLLAPSED;
			break;

		case IndicatorElement.STATE_EXPANDED:

			setImageDrawable(mExpandIndicator);

			mState = IndicatorElement.STATE_EXPANDED;
			break;

		default:
			break;
		}
	}

}
