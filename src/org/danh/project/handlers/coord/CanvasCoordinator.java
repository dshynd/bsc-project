package org.danh.project.handlers.coord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.danh.project.handlers.data.CanvasDataHandler;
import org.danh.project.handlers.data.IDataHandler;
import org.danh.project.handlers.watchers.CanvasWatchersManager;
import org.danh.project.handlers.watchers.IWatchersManager;

public class CanvasCoordinator implements ICoordinator {
	IWatchersManager manager = new CanvasWatchersManager();
	IDataHandler data = new CanvasDataHandler();

	public void handleNotificationsRequest(HttpServletRequest req, HttpServletResponse resp) {
		this.manager.startWatching(req, resp);
	}

	public void handleNewData(HttpServletRequest req, HttpServletResponse resp) {
		this.data.receiveData(req, resp);
		this.manager.notifyWatchers(req, resp);
	}

	public void handleRequestData(HttpServletRequest req, HttpServletResponse resp) {
		this.data.getData(req, resp);
	}
}
