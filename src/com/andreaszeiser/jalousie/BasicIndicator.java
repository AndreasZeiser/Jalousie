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
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.andreaszeiser.jalousie.util.Log;

/**
 * Wrapper class for a basic indicator used in IndicatedLinearLayoutJalousie,
 * which is based on the IndicatorElement interface.
 * 
 * This class contains two known children: IndicatorText and IndicatorImage.
 * This class task is to control the states of its children, e.g. switching
 * between collapse and expand text on the IndicatorText class.
 * 
 * @author Andreas Zeiser
 * 
 */
public class BasicIndicator extends LinearLayout implements IndicatorElement {

	private static final String TAG = IndicatedLinearLayoutJalousie.class
			.getSimpleName();

	/**
	 * Indicates the state of this indicator element. The default value is set
	 * to collapsed state.
	 */
	private int mState = IndicatorElement.STATE_COLLAPSED;

	/**
	 * Reference to the IndicatorImage view. May be null.
	 */
	private IndicatorImage mIndicatorImage;

	/**
	 * Reference to the IndicatorText view. May be null.
	 */
	private IndicatorText mIndicatorText;

	public BasicIndicator(Context context, AttributeSet attrs) {

		super(context, attrs);

		init(context, attrs);
	}

	public BasicIndicator(Context context) {

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
		Log.v(TAG, "[onFinishInflate]");

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
