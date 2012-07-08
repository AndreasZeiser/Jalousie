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

/**
 * Listener receiving events from a Jalousie class.
 * 
 * @author Andreas Zeiser
 * 
 */
public interface JalousieListener {

	/**
	 * Indicates an expand animation.
	 * 
	 * @see #ACTION_COLLAPSE
	 */
	public static final int ACTION_EXPAND = 1;

	/**
	 * Indicates a collpase animation.
	 * 
	 * @see #ACTION_EXPAND
	 */
	public static final int ACTION_COLLAPSE = 2;

	/**
	 * If the Jalousie view receives a command to expand or to collapse the
	 * jalousie, this method will be invoked.
	 * 
	 * @param action
	 *            either {@link #ACTION_EXPAND} or {@link #ACTION_COLLAPSE}
	 */
	public void onActionStart(final int action);

	/**
	 * If jalousie executed an expand or a collapse command, this method will be
	 * invoked.
	 * 
	 * @param action
	 *            either {@link #ACTION_EXPAND} or {@link #ACTION_COLLAPSE}
	 */
	public void onActionEnd(final int action);

}
