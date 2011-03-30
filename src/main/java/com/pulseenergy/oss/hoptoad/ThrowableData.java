package com.pulseenergy.oss.hoptoad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThrowableData {
	private String className;
	private String message;
	private List<StackTraceElement> stackTrace = new ArrayList<StackTraceElement>();
	private ThrowableData cause;

	public ThrowableData(final String className, final String message) {
		this.className = className;
		this.message = message;
	}

	public static ThrowableData fromThrowable(final Throwable throwable) {
		ThrowableData throwableData = new ThrowableData(throwable.getClass().getName(), throwable.getMessage());
		throwableData.setStackTrace(throwable.getStackTrace());
		Throwable cause = throwable.getCause();
		if (cause != null) {
			throwableData.setCause(fromThrowable(cause));

		}
		return throwableData;
	}

	public void addStackTraceElement(final StackTraceElement stackTraceElement) {
		stackTrace.add(stackTraceElement);
	}

	public void setCause(final ThrowableData cause) {
		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	public List<StackTraceElement> getStackTrace() {
		return stackTrace;
	}

	public ThrowableData getCause() {
		return cause;
	}

	public void setStackTrace(final StackTraceElement[] stackTrace) {
		this.stackTrace.clear();
		this.stackTrace.addAll(Arrays.asList(stackTrace));
	}

	public String getClassName() {
		return className;
	}
}
