package org.danh.project.image.producers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.danh.project.file.FileManager;
import org.danh.project.file.InvalidPathException;
import org.danh.project.file.NotInitialisedException;
import org.danh.project.image.components.IImageComponent;

public class LimitedSizeImageProducer implements IImageProducer {
	List<IImageComponent> components = new ArrayList<IImageComponent>();
	int limit;
	int currentCount;
	Color backgroundColor = Color.WHITE;
	Lock lock = new ReentrantLock(true);
	int canvasId;
	Dimension dimension = new Dimension(1024, 1024);
	BufferedImage currentImage;
	boolean redraw = true;
	int sinceSaveCount = 0;

	public LimitedSizeImageProducer(int canvasId, int limit) {
		this.canvasId = canvasId;
		this.limit = limit;
		this.currentImage = new BufferedImage((int) this.dimension.getWidth(), (int) this.dimension.getHeight(), 2);
		try {
			BufferedImage image = FileManager.getInstance().getBufferedImage(canvasId);
			this.currentImage.createGraphics().drawImage(image, 0, 0, (int) this.dimension.getWidth(), (int) this.dimension.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
		} catch (NotInitialisedException e) {
			e.printStackTrace();
		} catch (InvalidPathException e) {
			clear();
		}
	}

	public BufferedImage produceImage() {
		this.lock.lock();
		try {
			if (this.redraw) {
				Graphics2D g = this.currentImage.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				for(IImageComponent component : this.components) {
					component.visit(g);
					this.sinceSaveCount++;
				}
				
				this.components.clear();
				this.redraw = false;
				if (this.sinceSaveCount >= this.limit) {
					doSave();
				}
			}
			
			ColorModel colorModel = this.currentImage.getColorModel();
			boolean preMult = this.currentImage.isAlphaPremultiplied();
			WritableRaster raster = this.currentImage.copyData(null);
			
			return new BufferedImage(colorModel, raster, preMult, null);
		} finally {
			this.lock.unlock();
		}
	}

	public void addImageComponent(IImageComponent imageComponent) {
		this.lock.lock();
		try {
			this.components.add(imageComponent);
			this.redraw = true;
		} finally {
			this.lock.unlock();
		}
	}

	public void clear() {
		this.lock.lock();
		try {
			Graphics2D g = this.currentImage.createGraphics();
			g.setColor(this.backgroundColor);
			g.fillRect(0, 0, this.currentImage.getWidth(), this.currentImage.getHeight());
			this.components.clear();
			doSave();
		} finally {
			this.lock.unlock();
		}
	}

	public void setBackgroundColor(Color color) {
		this.lock.lock();
		try {
			this.backgroundColor = color;
		} finally {
			this.lock.unlock();
		}
	}

	public Color getBackgroundColor() {
		this.lock.lock();
		try {
			return this.backgroundColor;
		} finally {
			this.lock.unlock();
		}
	}

	public void setDimensions(int width, int height) {
		this.lock.lock();
		try {
			Dimension dimension = new Dimension(width, height);
			if (!this.dimension.equals(dimension)) {
				this.dimension = dimension;
			}
		} finally {
			this.lock.unlock();
		}
	}

	public Dimension getDimensions() {
		return this.dimension;
	}

	void doSave() {
		try {
			FileManager.getInstance().writeImage(this.canvasId, this.currentImage);
		} catch (Exception e) {
		}
		this.sinceSaveCount = 0;
	}
}