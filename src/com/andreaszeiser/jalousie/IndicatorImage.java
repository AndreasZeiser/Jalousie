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

package com.andreaszeiser.jalousie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView class which implements the IndicatorElement interface for
 * indicating the state of a Jalousie component.
 * 
 * Currently, this class supports two defined XML attributes: <br />
 * - expandIndicator and <br />
 * - collapseIndicator
 * 
 * @author Andreas Zeiser
 * 
 */
public class IndicatorImage extends ImageView implements IndicatorElement {

	private static final String TAG = IndicatorImage.class.getSimpleName();

	/**
	 * Indicates the state of this view. Default state is collapsed state.
	 */
	private int mState = IndicatorElement.STATE_COLLAPSED;

	/**
	 * If the state of this view is expanded, this drawable will be shown.
	 * 
	 * @see #mState
	 */
	private Drawable mExpandIndicator;

	/**
	 * If the state of this view is collapsed, this drawable will be shown.
	 * 
	 * @see #mState
	 */
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
