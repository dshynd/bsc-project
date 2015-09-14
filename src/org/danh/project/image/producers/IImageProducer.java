package org.danh.project.image.producers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.danh.project.image.components.IImageComponent;

public abstract interface IImageProducer {
	public abstract void addImageComponent(IImageComponent imageComponent);

	public abstract BufferedImage produceImage();

	public abstract void setDimensions(int width, int height);

	public abstract Dimension getDimensions();

	public abstract void setBackgroundColor(Color color);

	public abstract Color getBackgroundColor();

	public abstract void clear();
}