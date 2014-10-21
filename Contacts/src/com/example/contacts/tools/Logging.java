package com.example.contacts.tools;

import android.util.Log;

public class Logging {

	public static void logEntrance() {
		logEntrance(getStackTraceElement(), null, null, LoggingType.DEBUG);
	}

	public static void logEntranceWithTag(String tag) {
		logEntrance(getStackTraceElement(), tag, null, LoggingType.DEBUG);
	}

	public static void logEntrance(String extra) {
		logEntrance(getStackTraceElement(), null, extra, LoggingType.INFO);
	}

	public static void logEntrance(String tag, String extra) {
		logEntrance(getStackTraceElement(), tag, extra, LoggingType.INFO);
	}



	public static void logEntrance(Throwable e) {
		logEntrance(getStackTraceElement(), null, null, e, LoggingType.ERROR);
	}

	public static void logEntranceWithTag(String tag, Throwable e) {
		logEntrance(getStackTraceElement(), tag, null, e, LoggingType.ERROR);
	}

	public static void logEntrance(String extra, Throwable e) {
		logEntrance(getStackTraceElement(), null, extra, e, LoggingType.ERROR);
	}

	public static void logEntrance(String tag, String extra, Throwable e) {
		logEntrance(getStackTraceElement(), tag, extra, e, LoggingType.ERROR);
	}

	private static StackTraceElement getStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}



	private static void logEntrance(StackTraceElement element, String tag, String extra, LoggingType loggingType) {
		logEntrance(element, tag, extra, null, loggingType);
	}

	private static void logEntrance(StackTraceElement element, String tag, String extra, Throwable e, LoggingType t) {
		if (tag == null) {
			tag = getShortClassName(element);
		}
		String msg = String.format("%s # %s", element.getClassName(), element.getMethodName());
		if (extra != null && !extra.isEmpty()) {
			msg += ". " + extra;
		}


		switch (t) {
		case DEBUG:
			Log.d(tag, msg);
			break;
		case INFO:
			Log.i(tag, msg);
			break;
		case ERROR:
			Log.e(tag, msg, e);
			break;
		default:
			Log.i(tag, msg);
		}
	}

	private static String getShortClassName(StackTraceElement element) {
		String name = element.getClassName();
		int indexOfClassName = name.lastIndexOf('.');
		int indexOfSubclassName = name.lastIndexOf('$');
		
		int index = indexOfSubclassName != -1 ? indexOfSubclassName : indexOfClassName;
		return name.substring(index + 1);
	}

	private static enum LoggingType {
		INFO, DEBUG, ERROR, WARNING
	}
}
