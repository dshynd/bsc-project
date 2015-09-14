package org.danh.project.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.danh.project.file.FileManager;
import org.danh.project.handlers.coord.CanvasCoordinator;
import org.danh.project.handlers.login.ILoginManager;
import org.danh.project.handlers.login.SimpleLoginManager;

@WebListener
public class CoordinatorService implements ServletContextListener {
	public static final String COORDINATOR_KEY = "coordinator";
	public static final String LOGIN_MANAGER_KEY = "loginManager";
	ILoginManager loginManager;

	public void contextInitialized(ServletContextEvent sce) {
		FileManager.init(sce.getServletContext().getRealPath(""));
		sce.getServletContext().setAttribute(COORDINATOR_KEY, new CanvasCoordinator());
		this.loginManager = new SimpleLoginManager();
		this.loginManager.startUp();
		sce.getServletContext().setAttribute(LOGIN_MANAGER_KEY, this.loginManager);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		this.loginManager.shutDown();
	}
}