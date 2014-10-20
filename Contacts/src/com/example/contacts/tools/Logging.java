package com.example.contacts.tools;

import android.util.Log;

public class Logging {

	public static void logEntrance() {
		logEntrance(getStackTraceElement(), null, null);
	}

	public static void logEntranceWithTag(String tag) {
		logEntrance(getStackTraceElement(), tag, null);
	}

	public static void logEntrance(String extra) {
		logEntrance(getStackTraceElement(), null, extra);
	}

	public static void logEntrance(String tag, String extra) {
		logEntrance(getStackTraceElement(), tag, extra);
	}

	private static StackTraceElement getStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

	private static void logEntrance(StackTraceElement element, String tag, String extra) {
		String msg = String.format("%s # %s", element.getClassName(), element.getMethodName());
		if (extra != null && !extra.isEmpty()) {
			msg += ". " + extra;
		}
		Log.d(tag != null ? tag : getShortClassName(element), msg);
	}

	private static String getShortClassName(StackTraceElement element) {
		String name = element.getClassName();
		int indexOfClassName = name.lastIndexOf('.');
		int indexOfSubclassName = name.lastIndexOf('$');
		
		int index = indexOfSubclassName != -1 ? indexOfSubclassName : indexOfClassName;
		return name.substring(index + 1);
	}
}
