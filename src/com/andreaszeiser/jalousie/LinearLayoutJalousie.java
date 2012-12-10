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

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.andreaszeiser.jalousie.util.Log;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Based on the class LinearLayout, this class provides the functionality to
 * expand or collapse the size of this view, so that more or less content is
 * visible.
 * 
 * @author Andreas Zeiser
 * 
 */
public class LinearLayoutJalousie extends LinearLayout implements Jalousie {

	private static final String TAG = LinearLayoutJalousie.class
			.getSimpleName();

	/**
	 * Defines the view current height property name, which the animator has to
	 * change during animation.
	 */
	private static final String PROPERTY_CURRENT_HEIGHT = "currentHeight";

	/**
	 * Defines the view current width property name, which the animator has to
	 * change during animation.
	 */
	private static final String PROPERTY_CURRENT_WIDTH = "currentWidth";

	/**
	 * Animation type for expand animation.
	 */
	private static final int ANIMATION_TYPE_EXPAND = 1;

	/**
	 * Animation type for collapse animation.
	 */
	private static final int ANIMATION_TYPE_COLLAPSE = 2;

	/**
	 * Sets the default duration of an animation.
	 */
	public static final int DEFAULT_ANIMATION_DURATION = 350; // ms

	/**
	 * Content gravity is based on the LinearLayout orientation mode.
	 * <code>LinearLayout.HORIZONTAL</code> leads to
	 * <code>Expandable.GRAVITY_HORIZONTAL</code>, otherwise this variable has a
	 * value of <code>Expandable.GRAVITY_VERTICAL</code>.
	 */
	private int mContentGravity;

	/**
	 * This is the reference to the separator within the layout, which separates
	 * visible and hidden content. This reference can be <code>null</code>.
	 * 
	 * Notice, that the correct interpretation of this value depends on the
	 * content gravity.
	 * 
	 * @see #mContentGravity
	 */
	private Separator mSeparator;

	/**
	 * During views's first measure in {@link #onMeasure(int, int)}, this
	 * variable gets the original height (expanded state) of the view.
	 * 
	 * Notice, that the correct interpretation of this value depends on the
	 * content gravity.
	 * 
	 * @see #mContentGravity
	 */
	private int mOriginalSize;

	/**
	 * During views's first measure in {@link #onMeasure(int, int)}, this
	 * variable gets size of the visible content (collapsed state).
	 */
	private int mVisibleContentSize;

	/**
	 * Indicates, whether the visible content height was already measured or
	 * not.
	 * 
	 * If false, calculation will be done in the next run of onMeasure()
	 * 
	 * @see #onMeasure(int, int)
	 */
	private boolean mVisibleContentSizeWasMeasured = false;

	/**
	 * If this is set to true, current measured content size
	 * {@link #mVisibleContentSize} will be set as dimension for this view in
	 * its {@link #onMeasure(int, int)} method.
	 * 
	 * @see #onMeasure(int, int)
	 * @see #mVisibleContentSizeWasMeasured
	 */
	private boolean mForceRelayout = false;

	/**
	 * Indicates whether this view can be expanded or not.
	 * 
	 * @see #isExpandable()
	 */
	private boolean mIsExpandable = false;

	/**
	 * True, if view is expanded, otherwise false.
	 * 
	 * @see #isExpanded()
	 */
	private boolean mIsExpanded = false;

	/**
	 * If view cannot be close, this variable has a value of true.
	 */
	private boolean mIsAlwaysExpanded = false;

	/**
	 * If view is always closed, this variable has a value of true.
	 */
	private boolean mIsAlwaysCollapsed = false;

	/**
	 * Enables animation during expanding / collapsing of view.
	 * 
	 * @see #setAnimationEnabled(boolean)
	 */
	private boolean mAnimationEnabled = true;

	/**
	 * Indicates, whether an expand or collapse animation is ongoing or not.
	 */
	private boolean mIsAnimating = false;

	/**
	 * If an expand or collapse animation is running, this variable has a
	 * variable references to the responsable animator object.
	 */
	private Animator mCurrentAnimator;

	/**
	 * Indicates which type of animation is running.
	 * 
	 * @see #ANIMATION_TYPE_COLLAPSE
	 * @see #ANIMATION_TYPE_EXPAND
	 */
	private int mAnimationType;

	/**
	 * Contains the duration of each animation. Is either the default value
	 * {@value #DEFAULT_ANIMATION_DURATION} or a user defined one.
	 * 
	 * @see #setAnimationDuration(int)
	 * @see #DEFAULT_ANIMATION_DURATION
	 */
	private int mAnimationDuration = DEFAULT_ANIMATION_DURATION;

	/**
	 * Contains a reference to the interpolator which will be used during
	 * animation.
	 */
	private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

	/**
	 * Bucket of JalousListener elements.
	 */
	private ArrayList<JalousieListener> mJalousieListeners;

	public LinearLayoutJalousie(Context context, AttributeSet attrs) {

		super(context, attrs);

		init(context, attrs);
	}

	public LinearLayoutJalousie(Context context) {

		super(context);

		init(context, null);
	}

	/**
	 * Should only be called from constructor.
	 */
	private void init(final Context context, final AttributeSet attrs) {
		mJalousieListeners = new ArrayList<JalousieListener>();

		mContentGravity = (getOrientation() == LinearLayout.HORIZONTAL) ? Jalousie.GRAVITY_HORIZONTAL
				: Jalousie.GRAVITY_VERTICAL;

		if (attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.Jalousie, 0, 0);

			try {
				mIsExpanded = a
						.getBoolean(R.styleable.Jalousie_expanded, false);

				mIsAlwaysExpanded = a.getBoolean(
						R.styleable.Jalousie_alwaysExpanded, false);
			} finally {
				a.recycle();
			}
		}

		Log.v(TAG, "[init] gravity="
				+ ((mContentGravity == LinearLayout.HORIZONTAL) ? "horizontal"
						: "vertical"));

	}

	@Override
	protected void onFinishInflate() {
		Log.v(TAG, "[onFinishInflate]");

		super.onFinishInflate();

		mSeparator = (Separator) findViewById(R.id.evg__separator);

		Log.v(TAG, "[onFinishInflate] separator found=" + mSeparator);
	}

	/**
	 * Is responsable for measuring the visible content size. If visible content
	 * size was already measured, measuring will be skipped.
	 * 
	 * Takes also care of specified content gravity in {@link #mContentGravity}.
	 * 
	 * This method takes care of limiting the number of runs of measuring
	 * visible content height.
	 * 
	 * @see #mVisibleContentHeightWasMeasured
	 * @see #mVisibleContentHeight
	 * @see #mContentGravity
	 */
	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		Log.v(TAG, "[onMeasure] widthMode=" + widthMode + ", heightMode="
				+ heightMode + ", widthSize=" + widthSize + ", heighSize="
				+ heightSize);

		Log.v(TAG, "[onMeasure] mVisibleContentSizeWasMeasured="
				+ mVisibleContentSizeWasMeasured);

		if (!mVisibleContentSizeWasMeasured) {
			if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
				Log.v(TAG, "[onMeasure] gravity=horizontal");

				// measure the maximum needed height for this view with
				// MeasureSpec.Unspecified
				// the framework itself will store the height in measured height
				// through a call of setMeasuredHeight()
				super.onMeasure(MeasureSpec.UNSPECIFIED, heightMeasureSpec);

				// get the measured width
				mOriginalSize = getMeasuredWidth();

				// calculate the height of visible content
				// this is calculated by cumulating the height of all views
				// which are positioned before the separator view
				// calling of getTop() is not an option, because it will slow
				// down the animation :/
				mVisibleContentSize = 0;
				int childCount = getChildCount();
				View view = null;
				for (int i = 0; i < childCount; i++) {
					view = getChildAt(i);
					// if the separator is found, stop cumulating here
					// if there is no separator in the ViewGroup, the visible
					// content height will be calculated to ViewGroup's height.
					if (view instanceof Separator) {
						break;
					}
					mVisibleContentSize += view.getMeasuredWidth();
				}
			} else {
				Log.v(TAG, "[onMeasure] gravity=vertical");

				// measure the maximum needed height for this view with
				// MeasureSpec.Unspecified
				// the framework itself will store the height in measured height
				// through a call of setMeasuredHeight()
				super.onMeasure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);

				// get the measured height
				mOriginalSize = getMeasuredHeight();

				// calculate the height of visible content
				// this is calculated by cumulating the height of all views
				// which are positioned before the separator view
				// calling of getTop() is not an option, because it will slow
				// down the animation :/
				mVisibleContentSize = 0;
				int childCount = getChildCount();
				View view = null;
				for (int i = 0; i < childCount; i++) {
					view = getChildAt(i);
					// if the separator is found, stop cumulating here
					// if there is no separator in the ViewGroup, the visible
					// content height will be calculated to ViewGroup's height.
					if (view instanceof Separator) {
						break;
					}
					mVisibleContentSize += view.getMeasuredHeight();
				}
			}

			Log.v(TAG, "[onMeasure] original size=" + mOriginalSize);
			Log.v(TAG, "[onMeasure] visible content size="
					+ mVisibleContentSize);

			if (mOriginalSize > mVisibleContentSize) {
				// ViewGroup is expandable
				mIsExpandable = true;
				Log.v(TAG, "[onMeasure] is expandable=true");
			}

			mVisibleContentSizeWasMeasured = true;
		}

		Log.v(TAG, "[onMeasure] mForceRelayout=" + mForceRelayout);
		Log.v(TAG, "[onMeasure] mIsAnimating=" + mIsAnimating);
		Log.v(TAG, "[onMeasure] mIsExpanded=" + mIsExpanded);
		Log.v(TAG, "[onMeasure] mIsAlwaysExpanded=" + mIsAlwaysExpanded);

		if (mForceRelayout && mIsExpanded) {
			mForceRelayout = false;

			if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
				setMeasuredDimension(mOriginalSize, getMeasuredHeight());
			} else {
				setMeasuredDimension(getMeasuredWidth(), mOriginalSize);
			}
		} else if (!mIsAnimating && !mIsExpanded && !mIsAlwaysExpanded) {
			Log.v(TAG, "[onMeasure] set self measured dimension");

			if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
				setMeasuredDimension(mVisibleContentSize, heightSize);
			} else {
				setMeasuredDimension(widthSize, mVisibleContentSize);
			}
		} else {
			Log.v(TAG, "[onMeasure] set Android framework measured dimension");

			if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
				if (widthMode == MeasureSpec.UNSPECIFIED) {
					super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				} else {
					setMeasuredDimension(widthSize, getMeasuredHeight());
				}
			} else { // gravity=vertical
				if (heightMode == MeasureSpec.UNSPECIFIED) {
					super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				} else {
					setMeasuredDimension(getMeasuredWidth(), heightSize);
				}
			}
		}

		Log.v(TAG, "[onMeasure] measured width=" + getMeasuredWidth());
		Log.v(TAG, "[onMeasure] measured height=" + getMeasuredHeight());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.v(TAG, "[onSizeChanged] w=" + w + ", h=" + h + ", oldw=" + oldw
				+ ", oldh=" + oldh);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * Call this method, if this view should set its dimension to freshly
	 * calculated {@link #mVisibleContentSize}. This is particularly helpful, if
	 * views were added or removed before.
	 */
	public void forceRelayout() {
		Log.v(TAG, "[forceRelayout]");

		mVisibleContentSizeWasMeasured = false;
		mForceRelayout = true;
	}

	@Override
	public void requestLayout() {
		Log.v(TAG, "[requestLayout]");

		mVisibleContentSizeWasMeasured = false;

		super.requestLayout();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Log.v(TAG, "[onSaveInstanceState]");

		Bundle bundle = new Bundle();

		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putBoolean("isExpanded", mIsExpanded);

		Log.v(TAG, "[onSaveInstanceState] mIsExpanded=" + mIsExpanded);
		Log.v(TAG, "[onSaveInstanceState] bundle=" + bundle);

		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Log.v(TAG, "[onRestoreInstanceState] state=" + state);

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;

			mIsExpanded = bundle.getBoolean("isExpanded");
			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));

			Log.v(TAG, "[onRestoreInstanceState] mIsExpanded=" + mIsExpanded);

			return;
		}

		super.onRestoreInstanceState(state);
	}

	@Override
	public int getContentGravity() {
		return mContentGravity;
	}

	@Override
	public int getCurrentWidth() {
		ViewGroup.LayoutParams params = getLayoutParams();

		if (params == null) {
			// no layout params are set right now, return 0 as value
			return 0;
		}

		return params.width;
	}

	@Override
	public void setCurrentWidth(int currentWidth) {
		if (mContentGravity != Jalousie.GRAVITY_HORIZONTAL) {
			// if content gravity is not horizontal, ignore new width
			return;
		}

		ViewGroup.LayoutParams params = getLayoutParams();

		if (params != null) {
			getLayoutParams().width = currentWidth;
			requestLayout();
		}
	}

	@Override
	public int getCurrentHeight() {
		ViewGroup.LayoutParams params = getLayoutParams();

		if (params == null) {
			// no layout params are set right now, return 0 as value
			return 0;
		}

		return params.height;
	}

	@Override
	public void setCurrentHeight(final int currentHeight) {
		if (mContentGravity != Jalousie.GRAVITY_VERTICAL) {
			// if content gravity is not vertical, ignore new height
			return;
		}

		ViewGroup.LayoutParams params = getLayoutParams();

		if (params != null) {
			getLayoutParams().height = currentHeight;
			requestLayout();
		}
	}

	/**
	 * Expands the view animated. Takes care of ongoing animation, content
	 * gravity and expand restriction, e.g. {@link #mIsAlwaysExpanded}.
	 * 
	 * Shortcut method for {@link #expand(boolean)}.
	 * 
	 * @see #expand(boolean)
	 * @see #collapse()
	 * @see #toggle()
	 */
	@Override
	public boolean expand() {
		Log.v(TAG, "[expand]");

		return expand(true, false);
	}

	/**
	 * Expands the view, either animated or not.
	 * 
	 * @see #collapse()
	 * @see #toggle()
	 */
	@Override
	public boolean expand(boolean animated) {
		Log.v(TAG, "[expand] animated=" + animated);

		return expand(animated, false);
	}

	/**
	 * Expands the view, either animated or not. Takes care of ongoing
	 * animation, content gravity and expand restriction, e.g.
	 * {@link #mIsAlwaysExpanded}.
	 * 
	 * @param animated
	 * @param fromTouch
	 * @return
	 */
	private boolean expand(boolean animated, final boolean fromTouch) {
		Log.v(TAG, "[expand] animated=" + animated + ", fromTouch=" + fromTouch);

		if (!mIsExpandable) {
			// if view cannot be expanded, stop here
			return false;
		}

		if (mIsAlwaysExpanded && mIsExpanded) {
			// if view is always expanded and is not collapsed at this time,
			// stop here
			return false;
		}

		if (!mIsAnimating && mIsExpanded) {
			// if view is already expanded, do not expand and return false as
			// result
			return false;
		}

		if (!mAnimationEnabled) {
			// if animations are disabled for this view, ensure that expand is done not animated
			Log.v(TAG,
					"requested animation on expand is cancelled due to a negative animationEnabled flag. Expand is done not animated");
			animated = false;
		}

		for (JalousieListener listener : mJalousieListeners) {
			if (listener.onBeforeActionStart(JalousieListener.ACTION_EXPAND,
					fromTouch)) {
				return false;
			}
		}

		String propertyName;
		if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
			propertyName = PROPERTY_CURRENT_WIDTH;
		} else {
			propertyName = PROPERTY_CURRENT_HEIGHT;
		}

		if (mSeparator != null) {
			if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
				// everything on the left side of the separator is the 'visible'
				// content
				mVisibleContentSize = mSeparator.getLeft();
			} else {
				// everything above the separator is the 'visible' content
				mVisibleContentSize = mSeparator.getTop();
			}
		}

		if (mCurrentAnimator != null && mCurrentAnimator.isRunning()) {
			mCurrentAnimator.cancel();
		}

		// configure animation duration
		final int animationDuration = animated ? mAnimationDuration : 0;

		mCurrentAnimator = ObjectAnimator.ofInt(this, propertyName,
				mVisibleContentSize, mOriginalSize);
		mCurrentAnimator.setDuration(animationDuration);
		mCurrentAnimator.setInterpolator(mInterpolator);
		mCurrentAnimator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {

				mIsAnimating = true;
				mAnimationType = ANIMATION_TYPE_EXPAND;

				notifiyOnAnimationStart(JalousieListener.ACTION_EXPAND,
						animationDuration);
			}

			@Override
			public void onAnimationEnd(Animator animation) {

				mIsAnimating = false;
				mCurrentAnimator = null;
				mAnimationType = 0;
				mIsExpanded = true;

				notifiyOnAnimationEnd(JalousieListener.ACTION_EXPAND);
			}
		});

		mCurrentAnimator.start();

		return true;
	}

	/**
	 * Collapses the view animated. Takes care of ongoing animation, content
	 * gravity and collapse restriction, e.g. {@link #mIsAlwaysExpanded}.
	 * 
	 * Shortcut method for {@link #collapse(boolean)}.
	 * 
	 * @see #collapse(boolean)
	 * @see #expand()
	 * @see #toggle()
	 */
	@Override
	public boolean collapse() {
		Log.v(TAG, "[collapse]");

		return collapse(true, false);
	}

	/**
	 * Collapses the view, either animated or not.
	 * 
	 * @see #expand()
	 * @see #toggle()
	 */
	@Override
	public boolean collapse(boolean animated) {
		return collapse(animated, false);
	}

	/**
	 * Collapses the view, either animated or not. Takes care of ongoing
	 * animation, content gravity and collapse restriction, e.g.
	 * {@link #mIsAlwaysExpanded}.
	 * 
	 * @see #expand()
	 * @see #toggle()
	 */
	private boolean collapse(boolean animated, final boolean fromTouch) {
		Log.v(TAG, "[collapse] animated=" + animated);

		if (!mIsExpandable) {
			// if view cannot be expanded, stop here
			return false;
		}

		if (mIsAlwaysExpanded) {
			// if view is always expanded and is not collapsed at this time,
			// stop here
			return false;
		}

		if (!mIsAnimating && !mIsExpanded) {
			// if view is already collapsed, do not collapse and return false as
			// result
			return false;
		}

		if (!mAnimationEnabled) {
			// if animations are disabled for this view, ensure that collapse is done not animated
			Log.v(TAG,
					"requested animation on collapse is cancelled due to a negative animationEnabled flag. Collapse is done not animated");
			animated = false;
		}

		for (JalousieListener listener : mJalousieListeners) {
			if (listener.onBeforeActionStart(JalousieListener.ACTION_COLLAPSE,
					fromTouch)) {
				return false;
			}
		}

		String propertyName;
		if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
			propertyName = PROPERTY_CURRENT_WIDTH;
		} else {
			propertyName = PROPERTY_CURRENT_HEIGHT;
		}

		if (mSeparator != null) {
			if (mContentGravity == Jalousie.GRAVITY_HORIZONTAL) {
				// everything on the left side of the separator is the 'visible'
				// content
				mVisibleContentSize = mSeparator.getLeft();
			} else {
				// everything above the separator is the 'visible' content
				mVisibleContentSize = mSeparator.getTop();
			}
		}

		if (mCurrentAnimator != null && mCurrentAnimator.isRunning()) {
			mCurrentAnimator.cancel();
		}

		// configure animation duration
		final int animationDuration = animated ? mAnimationDuration : 0;

		mCurrentAnimator = ObjectAnimator.ofInt(this, propertyName,
				mOriginalSize, mVisibleContentSize);
		mCurrentAnimator.setDuration(animationDuration);
		mCurrentAnimator.setInterpolator(mInterpolator);
		mCurrentAnimator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {

				mIsAnimating = true;
				mAnimationType = ANIMATION_TYPE_COLLAPSE;

				notifiyOnAnimationStart(JalousieListener.ACTION_COLLAPSE,
						animationDuration);
			}

			@Override
			public void onAnimationEnd(Animator animation) {

				mIsAnimating = false;
				mCurrentAnimator = null;
				mAnimationType = 0;
				mIsExpanded = false;

				notifiyOnAnimationEnd(JalousieListener.ACTION_COLLAPSE);
			}
		});

		mCurrentAnimator.start();

		return true;
	}

	/**
	 * Switches the state of this view. In detail, it expands the view, if the
	 * view is collapsed or an animation is currently running to collapse this
	 * view. If the view is expanded, the view will be collapsed.
	 * 
	 * @see #expand()
	 * @see #collapse()
	 */
	@Override
	public boolean toggle() {
		Log.v(TAG, "[toggle]");

		return toggle(true, false);
	}

	@Override
	public boolean toggle(boolean animated) {
		Log.v(TAG, "[toggle] animated=" + animated);

		return toggle(animated, false);
	}

	boolean toggle(boolean animated, boolean fromTouch) {
		Log.v(TAG, "[toggle] animated=" + animated + ", fromTouch=" + fromTouch);

		if (mIsAlwaysExpanded) {
			return false;
		}

		if ((mIsAnimating && mAnimationType == ANIMATION_TYPE_EXPAND)
				|| (!mIsAnimating && mIsExpanded)) {
			return collapse(animated, fromTouch);
		} else {
			return expand(animated, fromTouch);
		}
	}

	@Override
	public boolean isExpanded() {
		return mIsExpanded;
	}

	@Override
	public boolean isCollapsed() {
		return !mIsExpanded;
	}

	@Override
	public boolean isExpandable() {
		return mIsExpandable;
	}

	@Override
	public void setIsAlwaysExpanded(final boolean alwaysExpanded) {
		mIsAlwaysExpanded = alwaysExpanded;

		if (mIsAlwaysExpanded && !mIsExpanded) {
			if (!expand(false)) {
				mIsExpanded = true;
			}
		} else if (!mIsAlwaysExpanded && mIsExpanded) {
			if (!collapse(false)) {
				mIsExpanded = false;
			}
		}
	}

	@Override
	public void setIsAlwaysCollapsed(final boolean alwaysCollapsed) {
		mIsAlwaysCollapsed = alwaysCollapsed;

		if (mIsAlwaysCollapsed && mIsExpanded) {
			collapse(false);
		}
	}

	@Override
	public void setAnimationEnabled(final boolean animationEnabled) {
		mAnimationEnabled = animationEnabled;
	}

	@Override
	public void setAnimationDuration(final int animationDuration) {
		mAnimationDuration = animationDuration;
	}

	@Override
	public void setInterpolator(final Interpolator interpolator) {
		mInterpolator = interpolator;
	}

	public void addJalousieListener(final JalousieListener listener) {
		if (mJalousieListeners == null) {
			mJalousieListeners = new ArrayList<JalousieListener>();
		}

		mJalousieListeners.add(listener);
	}

	public boolean removeJalousieListener(final JalousieListener listener) {
		if (mJalousieListeners == null) {
			return false;
		}

		return mJalousieListeners.remove(listener);
	}

	private void notifiyOnAnimationStart(final int action,
			final int animationDuration) {
		for (JalousieListener listener : mJalousieListeners) {
			listener.onActionStart(action, animationDuration);
		}
	}

	private void notifiyOnAnimationEnd(final int action) {
		for (JalousieListener listener : mJalousieListeners) {
			listener.onActionEnd(action);
		}
	}

	/**
	 * Adds the given view at the index 0.
	 * 
	 * @param view
	 */
	public void prependViewBeforeSeparator(final View view) {
		Log.v(TAG, "[prependViewBeforeSeparator] view=" + view);

		addView(view, 0);

		forceRelayout();
		requestLayout();
	}

	/**
	 * Adds the given view at the index right before the separator's index.
	 * 
	 * @param view
	 */
	public void appendViewBeforeSeparator(final View view) {
		final int separatorIndex = indexOfChild(mSeparator);

		Log.v(TAG, "[appendViewBeforeSeparator] view=" + view
				+ ", separatorIndex=" + separatorIndex);

		if (separatorIndex >= 0) {
			addView(view, separatorIndex);
		}

		forceRelayout();
		requestLayout();
	}

	/**
	 * Adds the given view at the index right after the separator's index.
	 * 
	 * @param view
	 */
	public void prependViewAfterSeparator(final View view) {
		final int separatorIndex = indexOfChild(mSeparator);

		Log.v(TAG, "[prependViewAfterSeparator] view=" + view
				+ ", separatorIndex=" + separatorIndex);

		if (separatorIndex >= 0) {
			addView(view, separatorIndex + 1);
		}

		forceRelayout();
		requestLayout();
	}

	/**
	 * Adds the given view at the last index.
	 * 
	 * @param view
	 */
	public void appendViewAfterSeparator(final View view) {
		Log.v(TAG, "[appendViewAfterSeparator] view=" + view);

		addView(view, getChildCount());

		forceRelayout();
		requestLayout();
	}

	/**
	 * Removes all views which have an index < separator's index.
	 */
	public void removeViewsBeforeSeparator() {
		final int separatorIndex = indexOfChild(mSeparator);

		Log.v(TAG, "[removeViewsBeforeSeparator] separatorIndex="
				+ separatorIndex);

		if (separatorIndex >= 0) {
			removeViews(0, separatorIndex);
		}

		forceRelayout();
	}

	/**
	 * Removes all views which have an index > separator's index.
	 */
	public void removeViewsAfterSeparator() {
		final int separatorIndex = indexOfChild(mSeparator);
		final int removeChildrenCount = getChildCount() - 1 - separatorIndex;

		Log.v(TAG, "[removeViewsAfterSeparator] separatorIndex="
				+ separatorIndex + ", removeChildrenCount="
				+ removeChildrenCount);

		if (separatorIndex >= 0 && removeChildrenCount > 0) {
			removeViews(separatorIndex + 1, removeChildrenCount);
		}

		// always close jalousie if there is no content expandable
		collapse(false);

		// set state, so that this jalousie is no more expandable 
		mIsExpandable = false;
	}

}