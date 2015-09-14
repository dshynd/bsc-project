package org.danh.project.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.handlers.coord.ICoordinator;

@WebServlet(asyncSupported = true, value = { "/DataServlet" })
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = -3009463463970143840L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getCoordinator().handleRequestData(req, resp);
	}

	ICoordinator getCoordinator() {
		return (ICoordinator) getServletContext().getAttribute(CoordinatorService.COORDINATOR_KEY);
	}
}