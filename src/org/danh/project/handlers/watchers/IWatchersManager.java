package org.danh.project.handlers.watchers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface IWatchersManager {
	public abstract void startWatching(HttpServletRequest req, HttpServletResponse resp);

	public abstract void notifyWatchers(HttpServletRequest req, HttpServletResponse resp);
}
