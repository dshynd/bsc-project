package org.danh.project.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, value = { "/DownloadServlet" })
public class DownloadServlet extends HttpServlet {
	Set<String> allowedMimeTypes = new HashSet<String>();

	public void init() throws ServletException {
		super.init();
		this.allowedMimeTypes.add("image/png");
		this.allowedMimeTypes.add("image/jpeg");
		this.allowedMimeTypes.add("image/gif");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String location = req.getParameter("location");
		try {
			URI uri = new URI(location);
			String path = uri.getPath().replace(req.getContextPath(), "");
			File file = new File(req.getServletContext().getRealPath(path));
			String contentType = URLConnection.guessContentTypeFromName(location);
			if ((file.exists()) && (this.allowedMimeTypes.contains(contentType))) {
				resp.setContentType(contentType);
				resp.setContentLength((int) file.length());
				resp.setHeader("Content-disposition", "attachment;fileName=" + file.getName());
				ServletOutputStream sos = resp.getOutputStream();
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					sos = resp.getOutputStream();
					byte[] arrayOfByte = new byte[10000];
					int i = 0;
					while ((i = fis.read(arrayOfByte)) >= 0) {
						sos.write(arrayOfByte, 0, i);
					}
				} finally {
					if (fis != null) {
						fis.close();
					}
				}
			} else {
				resp.setStatus(404);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}