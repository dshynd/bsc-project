package org.danh.project.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;

import org.danh.project.image.components.DrawImageComponent;
import org.danh.project.image.components.IImageComponent;

public class ImageManager {
	private static final ImageManager instance = new ImageManager();
	Map<Long, CanvasImage> images = Collections.synchronizedMap(new HashMap<Long, CanvasImage>());

	public static ImageManager getInstance() {
		return instance;
	}

	public int getCurrentCode(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			return image.getCurrentCode();
		}
		throw new CanvasImageNotFoundException();
	}

	public void incrementCode(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage localCanvasImage = this.images.get(canvasId);
		if (localCanvasImage != null) {
			localCanvasImage.incrementCode();
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public void addListener(long canvasId, AsyncContext listener) throws CanvasImageNotFoundException {
		CanvasImage localCanvasImage = this.images.get(canvasId);
		if (localCanvasImage != null) {
			localCanvasImage.addListener(listener);
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public void removeListener(long canvasId, AsyncContext listener) throws CanvasImageNotFoundException {
		CanvasImage localCanvasImage = this.images.get(canvasId);
		if (localCanvasImage != null) {
			localCanvasImage.removeListener(listener);
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public List<AsyncContext> getListeners(long listeners) throws CanvasImageNotFoundException {
		CanvasImage localCanvasImage = this.images.get(listeners);
		if (localCanvasImage != null) {
			return localCanvasImage.getListeners();
		}
		throw new CanvasImageNotFoundException();
	}

	public boolean isSetup(long canvasId) {
		return this.images.containsKey(canvasId);
	}

	public void setupCanvas(long canvasId) {
		if (!isSetup(canvasId)) {
			CanvasImage image = new CanvasImage(canvasId);
			this.images.put(canvasId, image);
		}
	}

	public void setupCanvas(long canvasId, BufferedImage image) {
		if (!isSetup(canvasId)) {
			setupCanvas(canvasId);
			CanvasImage prevImage = this.images.get(canvasId);
			prevImage.clear();
			int x = (int) Math.round(prevImage.getDimensions().getWidth() / 2.0 - image.getWidth() / 2.0);
			int y = (int) Math.round(prevImage.getDimensions().getHeight() / 2.0 - image.getHeight() / 2.0);
			prevImage.addComponent(new DrawImageComponent(image, x, y));
			prevImage.redraw();
		}
	}

	public Dimension getDimensions(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			return image.getDimensions();
		}
		throw new CanvasImageNotFoundException();
	}

	public void addImageComponent(long canvasId, IImageComponent imageComponent) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			image.addComponent(imageComponent);
			image.redraw();
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public void clear(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			image.clear();
			image.redraw();
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public void setBackgroundColor(long canvasId, Color color) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			image.setBackgroundColor(color);
			image.redraw();
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public void forceRedraw(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			image.redraw();
		} else {
			throw new CanvasImageNotFoundException();
		}
	}

	public BufferedImage getImage(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			return image.getImage();
		}
		throw new CanvasImageNotFoundException();
	}

	public Color getBGColor(long canvasId) throws CanvasImageNotFoundException {
		CanvasImage image = this.images.get(canvasId);
		if (image != null) {
			return image.getBGColor();
		}
		throw new CanvasImageNotFoundException();
	}
}