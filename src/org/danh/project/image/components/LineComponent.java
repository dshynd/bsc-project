package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Graphics2D;

public class LineComponent implements IImageComponent {
	Color color;
	int x1;
	int y1;
	int x2;
	int y2;

	public LineComponent(int x1, int y1, int x2, int y2, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}

	public void visit(Graphics2D graphics2D) {
		graphics2D.setColor(this.color);
		graphics2D.drawLine(this.x1, this.y1, this.x2, this.y2);
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LineComponent)) {
			return false;
		}
		LineComponent other = (LineComponent) obj;
		return (this.x1 == other.x1)
				&& (this.y1 == other.y1)
				&& (this.x2 == other.x2)
				&& (this.y2 == other.y2)
				&& (this.color.equals(other.color));
	}

	public int hashCode() {
		return this.x1 ^ this.y1 ^ this.x2 ^ this.y2 ^ this.color.hashCode();
	}

	public boolean hasZeroLength() {
		return (this.x1 == this.x2) && (this.y1 == this.y2);
	}
}
