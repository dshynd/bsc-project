package org.danh.project.image.components;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DrawImageComponent implements IImageComponent {
	BufferedImage image;
	int x;
	int y;

	public DrawImageComponent(BufferedImage image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	public void visit(Graphics2D graphics2D) {
		graphics2D.drawImage(this.image, this.x, this.y, graphics2D.getBackground(), null);
	}
}
