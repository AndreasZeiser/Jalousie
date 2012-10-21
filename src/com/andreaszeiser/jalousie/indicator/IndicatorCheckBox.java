package com.andreaszeiser.jalousie.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import com.andreaszeiser.jalousie.R;

public class IndicatorCheckBox extends CheckBox implements IndicatorElement {

	private static final String TAG = IndicatorCheckBox.class.getSimpleName();

	/**
	 * State of this indicated element. Default state is collapsed.
	 */
	private int mState = IndicatorElement.STATE_COLLAPSED;

	/**
	 * This string will be shown, when this view is in expanded state.
	 * 
	 * @see #mState
	 */
	private String mExpandIndicatorText;

	/**
	 * This string will be shown, when this view is in collapsed state.
	 * 
	 * @see #mState
	 */
	private String mCollapseIndicatorText;

	public IndicatorCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context, attrs);
	}

	public IndicatorCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs);
	}

	public IndicatorCheckBox(Context context) {
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
		}

		setText(mExpandIndicatorText);
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

	@Override
	public void show() {
		setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		setVisibility(View.GONE);
	}

}
