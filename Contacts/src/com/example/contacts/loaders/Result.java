package com.example.contacts.loaders;

public class Result<T> {

	public static enum LoaderState {
		IN_PROGRESS,
		FINISHED,
		EXCEPTION,
		CANCELED
	}

	public static <T> Result<T> finished(T data) {
		return new Result<T>(LoaderState.FINISHED, data, -1, -1, null);
	}

	public static <T> Result<T> inProgress(int progress, int maxProgress) {
		return new Result<T>(LoaderState.IN_PROGRESS, null, progress, maxProgress, null);
	}

	public static <T> Result<T> exception(Throwable exception) {
		return new Result<T>(LoaderState.EXCEPTION, null, -1, -1, exception);
	}

	public static <T> Result<T> canceled() {
		return new Result<T>(LoaderState.CANCELED, null, -1, -1, null);
	}

	public final LoaderState state;

	public final T data;
	public final int progress;
	public final int maxProgress;
	public final Throwable exception;


	private Result(LoaderState state, T data, int progress, int maxProgress, Throwable exception) {
		this.state = state;
		this.data = data;
		this.progress = progress;
		this.maxProgress = maxProgress;
		this.exception = exception;
	}
}
