package org.danh.project.image.producers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.danh.project.image.components.IImageComponent;

public class ComponentImageProducer implements IImageProducer {
	List<IImageComponent> components = new ArrayList<IImageComponent>();
	Color background = Color.WHITE;
	Lock lock = new ReentrantLock(true);
	Dimension dimension = new Dimension(1024, 1024);

	public BufferedImage produceImage() {
		BufferedImage image = new BufferedImage((int) this.dimension.getWidth(), (int) this.dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.lock.lock();
		try {
			Graphics2D g = image.createGraphics();
			g.setColor(this.background);
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for(IImageComponent component : this.components) {
				component.visit(g);
			}
		} finally {
			this.lock.unlock();
		}
		return image;
	}

	public void addImageComponent(IImageComponent imageComponent) {
		this.lock.lock();
		try {
			this.components.add(imageComponent);
		} finally {
			this.lock.unlock();
		}
	}

	public void clear() {
		this.lock.lock();
		try {
			this.components.clear();
		} finally {
			this.lock.unlock();
		}
	}

	public void setBackgroundColor(Color color) {
		this.background = color;
	}

	public Color getBackgroundColor() {
		return this.background;
	}

	public void setDimensions(int width, int height) {
		this.dimension.setSize(width, height);
	}

	public Dimension getDimensions() {
		return this.dimension;
	}
}