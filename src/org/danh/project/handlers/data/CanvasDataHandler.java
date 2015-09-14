package org.danh.project.handlers.data;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.canvas.CanvasManager;
import org.danh.project.canvas.CanvasNotFoundException;
import org.danh.project.file.FileManager;
import org.danh.project.image.CanvasImageNotFoundException;
import org.danh.project.image.ImageManager;
import org.danh.project.image.components.BrushedLineComponentV2;
import org.danh.project.image.components.DropComponent;
import org.danh.project.image.components.IImageComponent;
import org.danh.project.image.components.LineComponent;
import org.danh.project.image.components.PointComponent;
import org.danh.project.image.components.SquareComponent;
import org.danh.project.image.components.ThrowComponent;
import org.danh.project.json.ColorDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CanvasDataHandler implements IDataHandler {
	Map<Long, String> paths = new HashMap<Long, String>();
	Gson gson;

	public CanvasDataHandler() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Color.class, new ColorDeserializer());
		this.gson = builder.create();
	}

	public void getData(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/plain");
		try {
			long canvasId = Long.parseLong(req.getParameter("canvasId"));
			ImageManager manager = ImageManager.getInstance();
			String dataType = req.getParameter("dataType");
			if ("exists".equals(dataType)) {
				String exists = "false";
				try {
					CanvasManager.getInstance().getCanvas(canvasId);
					exists = "true";
				} catch (CanvasNotFoundException e) {
				}

				try {
					resp.getWriter().write(exists);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if ("url".equals(dataType)) {
				if (this.paths.get(canvasId) == null) {
					setupImagePath(canvasId);
				}
				try {
					resp.getWriter().print((String) this.paths.get(canvasId));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if ("bgColor".equals(dataType)) {
				try {
					manager.setupCanvas(canvasId);
					Color color = manager.getBGColor(canvasId);
					resp.getWriter().print(toRGBArray(color));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (NumberFormatException e) {
		}
	}

	public void receiveData(HttpServletRequest req, HttpServletResponse resp) {
		try {
			long canvasId = Long.parseLong(req.getParameter("canvasId"));
			if (this.paths.get(canvasId) == null) {
				setupImagePath(canvasId);
			}
			ImageManager manager = ImageManager.getInstance();
			try {
				String tool = req.getParameter("tool");
				if ("clear".equals(tool)) {
					manager.clear(canvasId);
				} else if ("setBG".equals(tool)) {
					manager.setBackgroundColor(canvasId, (Color) this.gson.fromJson(req.getParameter("json"), Color.class));
				} else if ("circle".equals(tool)) {
					manager.addImageComponent(canvasId, (IImageComponent) this.gson.fromJson(req.getParameter("json"), PointComponent.class));
				} else if ("square".equals(tool)) {
					manager.addImageComponent(canvasId, (IImageComponent) this.gson.fromJson(req.getParameter("json"), SquareComponent.class));
				} else {
					IImageComponent imageComponent;
					if ("line".equals(tool)) {
						imageComponent = (LineComponent) this.gson.fromJson(req.getParameter("json"), LineComponent.class);
						if (!((LineComponent) imageComponent).hasZeroLength()) {
							manager.addImageComponent(canvasId, imageComponent);
						}
					} else if ("brush".equals(tool)) {
						imageComponent = (BrushedLineComponentV2) this.gson.fromJson(req.getParameter("json"), BrushedLineComponentV2.class);
						if (!((BrushedLineComponentV2) imageComponent).hasZeroLength()) {
							manager.addImageComponent(canvasId, imageComponent);
						}
					} else if ("drop".equals(tool)) {
						imageComponent = (DropComponent) this.gson.fromJson(req.getParameter("json"), DropComponent.class);
						((DropComponent) imageComponent).setDimensions(manager.getDimensions(canvasId));
						manager.addImageComponent(canvasId, imageComponent);
					} else if ("throw".equals(tool)) {
						imageComponent = (ThrowComponent) this.gson.fromJson(req.getParameter("json"), ThrowComponent.class);
						((ThrowComponent) imageComponent).setDimensions(manager.getDimensions(canvasId));
						manager.addImageComponent(canvasId, imageComponent);
					}
				}
			} catch (CanvasImageNotFoundException e) {
			}
		} catch (NumberFormatException e) {
		}
	}

	void setupImagePath(long canvasId) {
		try {
			this.paths.put(Long.valueOf(canvasId), FileManager.getInstance().getImagePath(canvasId).replace(File.separatorChar, '/'));
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	String toRGBArray(Color color) {
		StringBuilder sb = new StringBuilder("[");
		sb.append(color.getRed() + ",");
		sb.append(color.getGreen() + ",");
		sb.append(color.getBlue() + "]");
		return sb.toString();
	}
}
