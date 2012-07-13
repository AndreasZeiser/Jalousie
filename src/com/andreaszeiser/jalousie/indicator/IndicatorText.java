/*
 * Copyright (C) 2012 Andreas Zeiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreaszeiser.jalousie.indicator;

import com.andreaszeiser.jalousie.R;
import com.andreaszeiser.jalousie.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * A TextView which implements the IndicatorElement interface for indicating the
 * state of a jalousie component.
 * 
 * This class supports two custom attributes which can be defined in XML: <br />
 * - expandIndicatorText and <br />
 * - collapseIndicatorText
 * 
 * @author Andreas Zeiser
 * 
 */
public class IndicatorText extends TextView implements IndicatorElement {

	private static final String TAG = IndicatorText.class.getSimpleName();

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

	public void setExpandIndicatorText(final String expandIndicatorText) {

		mExpandIndicatorText = expandIndicatorText;

		if (mState == IndicatorElement.STATE_EXPANDED) {
			setText(mExpandIndicatorText);
		}
	}

	public void setCollapseIndicatorText(final String collapseIndicatorText) {

		mCollapseIndicatorText = collapseIndicatorText;

		if (mState == IndicatorElement.STATE_COLLAPSED) {
			setText(mCollapseIndicatorText);
		}
	}

}
