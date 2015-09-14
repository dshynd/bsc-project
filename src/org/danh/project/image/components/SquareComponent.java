package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class SquareComponent extends Point implements IImageComponent {
	private static final long serialVersionUID = 8787103248671561424L;
	Color color;
	int sideLength;

	public SquareComponent(int x, int y, Color color, int sideLength) {
		super(x, y);
		this.color = color;
		this.sideLength = sideLength;
	}

	public void visit(Graphics2D graphics2D) {
		graphics2D.setColor(this.color);
		int i = this.sideLength / 2;
		graphics2D.fillRect(this.x - i, this.y - i, this.sideLength, this.sideLength);
	}

	public Color getColor() {
		return this.color;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SquareComponent)) {
			return false;
		}
		SquareComponent other = (SquareComponent) obj;
		return (super.equals(obj)) && (this.color.equals(other.color)) && (this.sideLength == other.sideLength);
	}

	public int hashCode() {
		return super.hashCode() ^ this.color.hashCode() ^ this.sideLength;
	}
}