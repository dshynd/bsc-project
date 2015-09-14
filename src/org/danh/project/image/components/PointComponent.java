package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class PointComponent extends Point implements IImageComponent {
	private static final long serialVersionUID = 8787103248671561424L;
	Color color;
	int radius;

	public PointComponent(int x, int y, Color color, int radius) {
		super(x, y);
		this.color = color;
		this.radius = radius;
	}

	public void visit(Graphics2D graphics2D) {
		graphics2D.setColor(this.color);
		int i = this.radius / 2;
		graphics2D.fillOval(this.x - i, this.y - i, this.radius, this.radius);
	}

	public Color getColor() {
		return this.color;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PointComponent)) {
			return false;
		}
		PointComponent other = (PointComponent) obj;
		return (super.equals(obj)) && (this.color.equals(other.color)) && (this.radius == other.radius);
	}

	public int hashCode() {
		return super.hashCode() ^ this.color.hashCode() ^ this.radius;
	}
}
