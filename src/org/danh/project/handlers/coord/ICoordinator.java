package org.danh.project.handlers.coord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface ICoordinator {
	public abstract void handleNotificationsRequest(HttpServletRequest req, HttpServletResponse resp);

	public abstract void handleNewData(HttpServletRequest req, HttpServletResponse resp);

	public abstract void handleRequestData(HttpServletRequest req, HttpServletResponse resp);
}
