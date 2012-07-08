package com.andreaszeiser.jalousie;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class Indicator extends LinearLayout implements IndicatorElement {

	/**
	 * Set this variable to true, if you want to receive debug information.
	 */
	private static final boolean DEBUG = true;

	private static final String TAG = IndicatedLinearLayoutJalousie.class
			.getSimpleName();

	private int mState = IndicatorElement.STATE_COLLAPSED;

	private IndicatorImage mIndicatorImage;

	private IndicatorText mIndicatorText;

	public Indicator(Context context, AttributeSet attrs) {

		super(context, attrs);

		init(context, attrs);
	}

	public Indicator(Context context) {

		super(context);

		init(context, null);
	}

	/**
	 * Should only be called from constructor.
	 */
	private void init(final Context context, final AttributeSet attrs) {

		setClickable(true);
	}

	@Override
	protected void onFinishInflate() {

		if (DEBUG) {
			Log.v(TAG, "[onFinishInflate]");
		}

		super.onFinishInflate();

		final int childCount = getChildCount();
		View view;
		for (int i = 0; i < childCount; i++) {
			view = getChildAt(i);
			if (view instanceof IndicatorImage) {
				mIndicatorImage = (IndicatorImage) view;
			} else if (view instanceof IndicatorText) {
				mIndicatorText = (IndicatorText) view;
			}
		}

		Log.v(TAG, "[onFinishInflate] indicatorImage=" + mIndicatorImage
				+ ", indicatorText=" + mIndicatorText);
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
		case IndicatorElement.STATE_COLLAPSED: // valid states
		case IndicatorElement.STATE_EXPANDED:

			mState = indicatorState;

			if (mIndicatorImage != null) {
				mIndicatorImage.setState(mState);
			}

			if (mIndicatorText != null) {
				mIndicatorText.setState(mState);
			}

			break;

		default: // do nothing
			break;
		}
	}

}
