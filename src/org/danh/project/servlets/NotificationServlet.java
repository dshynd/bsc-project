package org.danh.project.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.handlers.coord.ICoordinator;

@WebServlet(asyncSupported = true, value = { "/NotifyServlet" })
public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 2101176599018719276L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getCoordinator().handleNotificationsRequest(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getCoordinator().handleNewData(req, resp);
	}

	ICoordinator getCoordinator() {
		return (ICoordinator) getServletContext().getAttribute(CoordinatorService.COORDINATOR_KEY);
	}
}