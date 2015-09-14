package org.danh.project.user.results;

public class ResultMiscError extends Result {
	public ResultMiscError(String errorMsg) {
		this.message = ("Error: " + errorMsg);
	}
}