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

/**
 * Basic functionaliyt for an element which indicates the state of a Jalousie
 * component.
 * 
 * @author Andreas Zeiser
 * 
 */
public interface IndicatorElement {

	/**
	 * Expanded state of an indicator element.
	 * 
	 * @see #STATE_COLLAPSED
	 */
	public static final int STATE_EXPANDED = 1;

	/**
	 * Collapsed state of an indicator element.
	 * 
	 * @see #STATE_EXPANDED
	 */
	public static final int STATE_COLLAPSED = 2;

	/**
	 * Returns the state of the indicator.
	 * 
	 * @return either {@link #STATE_EXPANDED} or {@link #STATE_COLLAPSED}
	 */
	public int getState();

	/**
	 * Sets the state of the indicator element and its two known children
	 * IndicatorText and Indicatorimage.
	 * 
	 * @param indicatorState
	 *            either {@link #STATE_EXPANDED} or {@link #STATE_COLLAPSED}
	 */
	public void setState(final int indicatorState);

	/**
	 * Shows view element with <code>View.Visible</code>.
	 */
	public void show();

	/**
	 * Hides view element with <code>View.Gone</code>.
	 */
	public void hide();

}
