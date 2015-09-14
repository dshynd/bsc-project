package org.danh.project.image.producers;

public class LimitedSavesImageProducer extends FileImageProducer {
	int modCount = 0;

	public LimitedSavesImageProducer(int canvasId, int limit) {
		super(canvasId);
	}
}