package org.danh.project.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.file.FileManager;
import org.danh.project.file.InvalidPathException;
import org.danh.project.file.NotInitialisedException;

@WebServlet(urlPatterns = { "*.png" })
public class ImageServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		String contentType = URLConnection.guessContentTypeFromName(path);
		resp.setContentType(contentType);

		if ((path.startsWith("/" + FileManager.PREPENDPATH)) && (path.endsWith("/img.png"))) {
			FileManager manager = null;
			try {
				manager = FileManager.getInstance();
			} catch (NotInitialisedException e) {
				FileManager.init(getServletContext().getRealPath(""));
				try {
					manager = FileManager.getInstance();
				} catch (NotInitialisedException ex) {
					ex.printStackTrace();
				}
			}
			
			try {
				if (manager != null) {
					manager.readImage(manager.getCanvasId(path), resp);
				}
			} catch (InvalidPathException e) {
				resp.setStatus(404);
			}
		} else {
			File file = new File(req.getServletContext().getRealPath(path));
			resp.setContentLength((int) file.length());
			ServletOutputStream sos = resp.getOutputStream();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				sos = resp.getOutputStream();
				byte[] buffer = new byte[10000];
				int len = 0;
				while ((len = fis.read(buffer)) >= 0) {
					sos.write(buffer, 0, len);
				}
			} finally {
				if (fis != null) {
					fis.close();
				}
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
}