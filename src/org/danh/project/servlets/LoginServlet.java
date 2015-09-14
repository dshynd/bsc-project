package org.danh.project.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.handlers.login.ILoginManager;
import org.danh.project.user.results.Result;
import org.danh.project.user.results.ResultMiscError;
import org.danh.project.user.usertypes.BasicUser;
import org.danh.project.user.usertypes.IUser;

import com.google.gson.Gson;

@WebServlet(asyncSupported = true, value = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 2101176599018719276L;
	ILoginManager manager;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		IUser user = this.manager.getCurrentUser(req.getSession());
		if (user != null) {
			resp.getWriter().write(new Gson().toJson(user.getSafeUser()));
		} else {
			resp.getWriter().write(new Gson().toJson(null));
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String str = req.getParameter("action");
		if (str != null) {
			resp.setContentType("application/json");
			String username;
			String password;
			if ("login".equals(str)) {
				username = req.getParameter("username");
				password = req.getParameter("password");
				Result result = this.manager.doLogin(username, password, req.getSession());
				resp.getWriter().write(new Gson().toJson(result));
			} else if ("register".equals(str)) {
				username = req.getParameter("username");
				password = req.getParameter("password");
				BasicUser user = new BasicUser(username, password);
				Result result = this.manager.registerUser(user);
				if (result.isSuccessful()) {
					this.manager.doLogin(username, password, req.getSession());
				}
				resp.getWriter().write(new Gson().toJson(result));
			} else if ("logout".equals(str)) {
				this.manager.doLogout(req.getSession());
			} else if ("changePW".equals(str)) {
				IUser user = this.manager.getCurrentUser(req.getSession());
				Result result;
				if (user != null) {
					result = this.manager.setUserPassword(user.getUsername(), req.getParameter("oldPassword"), req.getParameter("newPassword"));
				} else {
					result = new ResultMiscError("No user currently logged in");
				}
				resp.getWriter().write(new Gson().toJson(result));
			}
		}
	}

	public void init() throws ServletException {
		super.init();
		this.manager = ((ILoginManager) getServletContext().getAttribute("loginManager"));
	}
}