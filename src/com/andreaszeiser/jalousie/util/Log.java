package com.andreaszeiser.jalousie.util;

public final class Log {

	public static boolean DEBUG = false;

	public static void v(final String tag, final String msg) {
		if (DEBUG) {
			android.util.Log.v(tag, msg);
		}
	}

	public static void i(final String tag, final String msg) {
		if (DEBUG) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void d(final String tag, final String msg) {
		if (DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void w(final String tag, final String msg) {
		if (DEBUG) {
			android.util.Log.w(tag, msg);
		}
	}

	public static void e(final String tag, final String msg) {
		if (DEBUG) {
			android.util.Log.e(tag, msg);
		}
	}

}
