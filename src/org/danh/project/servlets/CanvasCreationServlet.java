package org.danh.project.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.danh.project.handlers.login.ILoginManager;
import org.danh.project.image.ImageManager;
import org.danh.project.user.usertypes.IUser;

import com.google.gson.Gson;

@WebServlet(asyncSupported = true, value = { "/CreationServlet" })
@MultipartConfig
public class CanvasCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 2101176599018719276L;
	ILoginManager manager;
	long lastCanvasId = -1;
	Lock lastIDlock = new ReentrantLock();
	static final int MOST_RECENT_SIZE = 5;
	Deque<Long> mostRecent = new ArrayDeque<Long>(5);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		synchronized (this.mostRecent) {
			resp.setContentType("application/json");
			resp.getWriter().write(new Gson().toJson(this.mostRecent));
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		IUser user = this.manager.getCurrentUser(req.getSession());
		if (user != null) {
			long canvasId = getNewCanvasId();
			Part part = req.getPart("initialImage");
			if ((part != null) && (part.getSize() > 0)) {
				BufferedImage image = ImageIO.read(part.getInputStream());
				ImageManager.getInstance().setupCanvas(canvasId, image);
			} else {
				ImageManager.getInstance().setupCanvas(canvasId);
			}
			user.addCanvasId(canvasId);
			addToMostRecent(canvasId);
			resp.sendRedirect("canvas.html?canvasId=" + canvasId);
		} else {
			resp.sendRedirect("login.html");
		}
	}

	public void init() throws ServletException {
		super.init();
		this.manager = ((ILoginManager) getServletContext().getAttribute("loginManager"));
	}

	void addToMostRecent(long canvasId) {
		synchronized (this.mostRecent) {
			this.mostRecent.addFirst(canvasId);
			if (this.mostRecent.size() > 5) {
				this.mostRecent.removeLast();
			}
		}
	}

	long getNewCanvasId() {
		this.lastIDlock.lock();
		try {
			long canvasId = Math.max(System.currentTimeMillis(), this.lastCanvasId + 1);
			this.lastCanvasId = canvasId;
			return canvasId;
		} finally {
			this.lastIDlock.unlock();
		}
	}
}