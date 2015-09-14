package org.danh.project.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.AsyncContext;

import org.danh.project.canvas.Canvas;
import org.danh.project.canvas.CanvasAlreadyExistsException;
import org.danh.project.canvas.CanvasManager;
import org.danh.project.canvas.CanvasNotFoundException;
import org.danh.project.image.components.IImageComponent;

public class CanvasImage {
	Canvas canvas;
	int currentCode;
	List<AsyncContext> listeners;
	BufferedImage latest;

	public CanvasImage(long canvasId) {
		CanvasManager manager = CanvasManager.getInstance();
		try {
			this.canvas = manager.getCanvas(canvasId);
		} catch (CanvasNotFoundException e) {
			try {
				this.canvas = manager.createCanvas(canvasId);
			} catch (CanvasAlreadyExistsException ex) {
			}
		}
		this.listeners = Collections.synchronizedList(new ArrayList<AsyncContext>());
		this.currentCode = 0;
		redraw();
	}

	public int getCurrentCode() {
		return this.currentCode;
	}

	public void incrementCode() {
		this.currentCode += 1;
	}

	public void addListener(AsyncContext listener) {
		this.listeners.add(listener);
	}

	public void removeListener(AsyncContext listener) {
		this.listeners.remove(listener);
	}

	public List<AsyncContext> getListeners() {
		return this.listeners;
	}

	public void addComponent(IImageComponent imageComponent) {
		this.canvas.getImageProducer().addImageComponent(imageComponent);
	}

	public void clear() {
		this.canvas.getImageProducer().clear();
	}

	public void setBackgroundColor(Color color) {
		this.canvas.getImageProducer().setBackgroundColor(color);
	}

	public void redraw() {
		this.latest = this.canvas.getImageProducer().produceImage();
	}

	public Dimension getDimensions() {
		return this.canvas.getImageProducer().getDimensions();
	}

	public BufferedImage getImage() {
		return this.latest;
	}

	public Color getBGColor() {
		return this.canvas.getImageProducer().getBackgroundColor();
	}
}