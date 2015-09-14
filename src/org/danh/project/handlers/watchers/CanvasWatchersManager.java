package org.danh.project.handlers.watchers;

import java.io.IOException;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.image.CanvasImageNotFoundException;
import org.danh.project.image.ImageManager;

public class CanvasWatchersManager implements IWatchersManager {
	public void startWatching(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");

		try {
			final long canvasId = Long.parseLong(req.getParameter("canvasId"));
			final ImageManager manager = ImageManager.getInstance();
			if (!manager.isSetup(canvasId)) {
				manager.setupCanvas(canvasId);
			}
			try {
				String code = req.getParameter("code");
				if ((code == null) || (Integer.parseInt(code) < manager.getCurrentCode(canvasId))) {
					try {
						printCode(resp, canvasId);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					AsyncContext context = req.startAsync();
					context.setTimeout(0);
					context.addListener(new AsyncListener() {
						public void onTimeout(AsyncEvent ev) throws IOException {
							removeListener(ev.getAsyncContext());
						}

						public void onStartAsync(AsyncEvent ev) throws IOException {
						}

						public void onError(AsyncEvent ev) throws IOException {
							removeListener(ev.getAsyncContext());
						}

						public void onComplete(AsyncEvent ev) throws IOException {
							removeListener(ev.getAsyncContext());
						}

						void removeListener(AsyncContext context) {
							try {
								manager.removeListener(canvasId, context);
							} catch (CanvasImageNotFoundException e) {
								e.printStackTrace();
							}
						}
					});
					manager.addListener(canvasId, context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			printInvalidCanvasId(resp);
		}
	}

	public void notifyWatchers(HttpServletRequest req, HttpServletResponse resp) {
		try {
			long canvasId = Long.parseLong(req.getParameter("canvasId"));
			ImageManager manager = ImageManager.getInstance();
			manager.incrementCode(canvasId);
			List<AsyncContext> listeners = manager.getListeners(canvasId);
			synchronized (listeners) {
				for (AsyncContext listener : listeners) {
					try {
						printCode(listener.getResponse(), canvasId);
					} catch (IOException e) {
						e.printStackTrace();
					}
					listener.complete();
				}
			}
		} catch (Exception e) {
		}
	}

	public void printCode(ServletResponse resp, long canvasId) throws IOException {
		try {
			int code = ImageManager.getInstance().getCurrentCode(canvasId);
			resp.getWriter().print("{\"code\": " + code + ", \"canvasId\": " + canvasId + "}");
		} catch (CanvasImageNotFoundException e) {
			printInvalidCanvasId(resp);
		}
	}

	void printInvalidCanvasId(ServletResponse resp) {
		try {
			resp.getWriter().print("{\"canvasIDInvalid\": true}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
