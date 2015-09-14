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

import org.danh.project.file.FileManager;
import org.danh.project.file.InvalidPathException;
import org.danh.project.image.components.IImageComponent;

public class FileImageProducer implements IImageProducer {
	List<IImageComponent> components = new ArrayList<IImageComponent>();
	Color background = Color.WHITE;
	Lock lock = new ReentrantLock(true);
	long canvasId;
	boolean toClear = false;
	Dimension dimension = new Dimension(1024, 1024);

	public FileImageProducer(long canvasId) {
		this.canvasId = canvasId;
	}

	public BufferedImage produceImage() {
		BufferedImage image = new BufferedImage((int) this.dimension.getWidth(), (int) this.dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.lock.lock();
		try {
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			try {
				FileManager manager = FileManager.getInstance();
				if (this.toClear) {
					g.setColor(this.background);
					g.fillRect(0, 0, image.getWidth(), image.getHeight());
					this.toClear = false;
				} else {
					try {
						BufferedImage prevImage = manager.getBufferedImage(this.canvasId);
						g.drawImage(prevImage, 0, 0, (int) this.dimension.getWidth(), (int) this.dimension.getHeight(), 0, 0, prevImage.getWidth(), prevImage.getHeight(), null);
					} catch (InvalidPathException e) {
					}
				}
				
				if (this.components.size() > 0) {
					for(IImageComponent component : this.components) {
						component.visit(g);
					}
					this.components.clear();
				}
				
				manager.writeImage(this.canvasId, image);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return image;
		} finally {
			this.lock.unlock();
		}
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
			this.toClear = true;
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