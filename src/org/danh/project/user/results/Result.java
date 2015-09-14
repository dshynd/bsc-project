package org.danh.project.user.results;

public abstract class Result {
	boolean successful;
	String message;

	public boolean isSuccessful() {
		return this.successful;
	}

	public String getMessage() {
		return this.message;
	}
}
