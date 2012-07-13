package com.andreaszeiser.jalousie;

import com.andreaszeiser.jalousie.indicator.IndicatorElement;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class IndicatedLinearLayoutJalousie extends LinearLayout {

	/**
	 * Set this variable to true, if you want to receive debug information.
	 */
	private static final boolean DEBUG = true;

	private static final String TAG = IndicatedLinearLayoutJalousie.class
			.getSimpleName();

	/**
	 * Content gravity is based on the LinearLayout orientation mode.
	 * <code>LinearLayout.HORIZONTAL</code> leads to
	 * <code>Expandable.GRAVITY_HORIZONTAL</code>, otherwise this variable has a
	 * value of <code>Expandable.GRAVITY_VERTICAL</code>.
	 */
	private int mContentGravity;

	private LinearLayoutJalousie mLinearLayoutJalousie;

	private IndicatorElement mIndicator;

	public IndicatedLinearLayoutJalousie(Context context, AttributeSet attrs) {

		super(context, attrs);

		init();
	}

	public IndicatedLinearLayoutJalousie(Context context) {

		super(context);

		init();
	}

	private void init() {

		mContentGravity = (getOrientation() == LinearLayout.HORIZONTAL) ? Jalousie.GRAVITY_HORIZONTAL
				: Jalousie.GRAVITY_VERTICAL;

		if (DEBUG) {
			Log.v(TAG,
					"[init] gravity="
							+ ((mContentGravity == LinearLayout.HORIZONTAL) ? "horizontal"
									: "vertical"));
		}
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

			if (view instanceof LinearLayoutJalousie) {
				mLinearLayoutJalousie = (LinearLayoutJalousie) view;
				mLinearLayoutJalousie.setJalousieListener(mJalousieListener);
			} else if (view instanceof IndicatorElement) {
				mIndicator = (IndicatorElement) view;
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		Log.v(TAG, "[onSizeChanged] w=" + w + ", h=" + h + ", oldw=" + oldw
				+ ", oldh=" + oldh);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			mLinearLayoutJalousie.toggle();
			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	private JalousieListener mJalousieListener = new JalousieListener() {

		@Override
		public void onActionStart(final int action, final int animationDuration) {
		}

		@Override
		public void onActionEnd(int action) {
			switch (action) {
			case JalousieListener.ACTION_COLLAPSE:

				mIndicator.setState(IndicatorElement.STATE_EXPANDED);
				break;

			case JalousieListener.ACTION_EXPAND:

				mIndicator.setState(IndicatorElement.STATE_COLLAPSED);
				break;

			default:
				break;
			}
		}
	};

	public LinearLayoutJalousie getLinearLayoutJalousie() {
		return mLinearLayoutJalousie;
	}

	public IndicatorElement getIndicator() {
		return mIndicator;
	}
}
