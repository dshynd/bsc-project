package org.danh.project.canvas;

import org.danh.project.image.producers.FileImageProducer;
import org.danh.project.image.producers.IImageProducer;

public class Canvas {
	long id;
	IImageProducer imageProducer;

	public Canvas(long canvasId) {
		this.id = canvasId;
		this.imageProducer = new FileImageProducer(canvasId);
	}

	public long getIdCode() {
		return this.id;
	}

	public IImageProducer getImageProducer() {
		return this.imageProducer;
	}
}
