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

import android.view.animation.Interpolator;

/**
 * Basic functionality of a jalousie component.
 * 
 * @author Andreas Zeiser
 * 
 */
public interface Jalousie {

	/**
	 * Indicates that the content of this view flows in horizontal direction.
	 * 
	 * @see #GRAVITY_VERTICAL
	 */
	public static final int GRAVITY_HORIZONTAL = 0;

	/**
	 * Indicates that the content of this view flows in vertical direction.
	 * 
	 * @see #GRAVITY_HORIZONTAL
	 */
	public static final int GRAVITY_VERTICAL = 1;

	/**
	 * If view is expandable and is collapsed at the time of call, the view will
	 * expand animated.
	 * 
	 * If you want your view expand immediately without animation, please have a
	 * look on the method {@link #expand(boolean)}.
	 * 
	 * @see #expand(boolean)
	 * @see #collapse()
	 * @see #toggle()
	 * @see #isExpandable()
	 * 
	 * @return true, if view will expand.
	 */
	public boolean expand();

	/**
	 * If view is expandable and is collapsed at the time of call, the view will
	 * expand, either animated or not.
	 * 
	 * If you want your view expand immediately without animation, please have a
	 * look on the method {@link #expand(boolean)}.
	 * 
	 * @see #expand()
	 * @see #collapse()
	 * @see #toggle()
	 * @see #isExpandable()
	 * 
	 * @param animated
	 *            expanding will be done animated, if value is true, otherwise
	 *            vieW will expand immediately without animation.
	 * @return true, if view will expand.
	 */
	public boolean expand(final boolean animated);

	/**
	 * If view is expanded at the time of call of this method, the view will
	 * collapse animated.
	 * 
	 * If you want your view collapse immediately without animation, please have
	 * a look on the method {@link #collapse(boolean)}.
	 * 
	 * @see #collapse(boolean)
	 * @see #expand()
	 * @see #toggle()
	 * @see #isExpanded()
	 * 
	 * @return true, if view will collapse.
	 */
	public boolean collapse();

	/**
	 * If view is expanded at the time of call of this method, the view will
	 * collapse, either animated or not.
	 * 
	 * @see #collapse()
	 * @see #toggle()
	 * @see #isExpanded()
	 * 
	 * @param animated
	 *            collapsing will be done animated, if value is true, otherwise
	 *            view will collapse immediately without animation.
	 * @return
	 */
	public boolean collapse(final boolean animated);

	/**
	 * If view is collapsed, then view will be expanded through
	 * {@link #expand()}, otherwise view will be collapsed {@link #collapse()}.
	 * 
	 * @return true, if view was expanded or collapsed
	 * 
	 * @see #expand()
	 * @see #collapse()
	 * @see #isExpanded()
	 * @see #isExpandable()
	 */
	public boolean toggle();

	/**
	 * If view is collapsed, then view will be expanded through
	 * {@link #expand(boolean)}, otherwise view will be collapsed
	 * {@link #collapse(boolean)}.
	 * 
	 * @param animated
	 *            if true, expand / collapse will be animated, otherwise not.
	 * @return
	 */
	public boolean toggle(final boolean animated);

	/**
	 * If view is expanded, this method returns true.
	 * 
	 * @return true, if view is expanded
	 */
	public boolean isExpanded();

	/**
	 * If view is collapsed, this method returns true.
	 * 
	 * @return true, if view is collapsed
	 */
	public boolean isCollapsed();

	/**
	 * If view can be expanded, this method returns true.
	 * 
	 * @return true, if view can be expanded
	 */
	public boolean isExpandable();

	/**
	 * Do not allow this view to collapse itself.
	 * 
	 * @param alwaysExpanded
	 *            true, if view should not be able to collapse
	 */
	public void setIsAlwaysExpanded(final boolean alwaysExpanded);

	/**
	 * Make this not expandable.
	 * 
	 * @param alwaysCollapsed
	 *            true, if view should not be able to expand
	 */
	public void setIsAlwaysCollapsed(final boolean alwaysCollapsed);

	/**
	 * Returns the gravity of the content. Valid values are
	 * {@link #GRAVITY_HORIZONTAL} or {@link #GRAVITY_VERTICAL}.
	 * 
	 * @return gravity of view's content
	 * 
	 * @see #GRAVITY_HORIZONTAL
	 * @see #GRAVITY_VERTICAL
	 */
	public int getContentGravity();

	/**
	 * Get the view's width at the time of method call. If view is expandable
	 * and collapsed, the return value of this method is less than the return
	 * value of this method, when the view is in expanded state.
	 * 
	 * @return current width of view
	 */
	public int getCurrentWidth();

	/**
	 * Sets the current width of this view.
	 * 
	 * @param currentWidth
	 */
	public void setCurrentWidth(final int currentWidth);

	/**
	 * Get the view's height at the time of method call. If view is expandable
	 * and collapsed, the return value of this method is less than the return
	 * value of this method, when the view is in expanded state.
	 * 
	 * @return current height of view
	 */
	public int getCurrentHeight();

	/**
	 * Sets the current height of this view.
	 * 
	 * @param currentHeight
	 */
	public void setCurrentHeight(final int currentHeight);

	/**
	 * If view should animate its state change, provide true as an argument.
	 * 
	 * @param animationEnabled
	 *            true, if view should animate its state change
	 */
	public void setAnimationEnabled(final boolean animationEnabled);

	/**
	 * Sets the duration of both, the expand and collapse animation.
	 * 
	 * @param animationDuration
	 */
	public void setAnimationDuration(final int animationDuration);

	/**
	 * Sets the interpolator, which should be used during expand and collapse
	 * animation.
	 * 
	 * @param interpolator
	 */
	public void setInterpolator(final Interpolator interpolator);

}
