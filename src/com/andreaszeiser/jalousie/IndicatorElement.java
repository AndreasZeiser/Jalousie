package com.andreaszeiser.jalousie;

public interface IndicatorElement {

	public static final int STATE_EXPANDED = 1;

	public static final int STATE_COLLAPSED = 2;

	public int getState();

	public void setState(final int indicatorState);

}
