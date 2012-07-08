package com.andreaszeiser.jalousie;

public interface JalousieListener {

	public static final int ACTION_EXPAND = 1;
	public static final int ACTION_COLLAPSE = 2;

	public void onActionStart(final int action);

	public void onActionEnd(final int action);

}
