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

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * A UniversalIndicator lets you create complex indicators. Therefore this class
 * is based on the powerful RelativeLayout class. You are totally free to place
 * you children whereever you like. However, there is one requirements for your
 * children: They have to implement the IndicatorElement interface.
 * 
 * @author Andreas Zeiser
 * 
 */
public class RelativeLayoutIndicator extends RelativeLayout implements
		IndicatorElement {

	/**
	 * Indicates the state of this indicator element. The default value is set
	 * to collapsed state.
	 */
	private int mState = IndicatorElement.STATE_COLLAPSED;

	private ArrayList<IndicatorElement> mIndicatorElements = new ArrayList<IndicatorElement>();

	public RelativeLayoutIndicator(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public RelativeLayoutIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public RelativeLayoutIndicator(Context context) {
		super(context);

		init();
	}

	private void init() {
		setClickable(true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = getChildCount();
		View view;
		for (int i = 0; i < childCount; i++) {
			view = getChildAt(i);
			if (view instanceof IndicatorElement) {
				mIndicatorElements.add((IndicatorElement) view);
			}
		}
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

			for (IndicatorElement el : mIndicatorElements) {
				el.setState(mState);
			}

			break;

		default: // do nothing
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
