package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class BrushedLineComponent extends LineComponent {
	private static final int LINEGAP = 2;
	int width;
	Random random = new Random();

	public BrushedLineComponent(int x1, int y1, int x2, int y2, int width, Color color) {
		super(x1, y1, x2, y2, color);
		this.width = width;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BrushedLineComponent)) {
			return false;
		}
		BrushedLineComponent other = (BrushedLineComponent) obj;
		return (this.x1 == other.x1) && (this.y1 == other.y1) && (this.x2 == other.x2) && (this.y2 == other.y2) && (this.width == other.width) && (this.color.equals(other.color));
	}

	public int hashCode() {
		return this.x1 ^ this.y1 ^ this.x2 ^ this.y2 ^ this.width ^ this.color.hashCode();
	}

	public void visit(Graphics2D graphics2D) {
		graphics2D.setColor(this.color);
		graphics2D.drawLine(this.x1, this.y1, this.x2, this.y2);
		int dx = this.x2 - this.x1;
		int dy = this.y2 - this.y1;
		double ratio = dy / dx;
		double angle = Math.atan(ratio);
		angle = Math.toDegrees(angle);
		boolean lessThan45 = Math.abs(angle) < 45.0;
		for (int i = 1; i <= this.width; i++) {
			if (lessThan45) {
				graphics2D.drawLine(this.x1, this.y1 + LINEGAP * i, this.x2, this.y2 + LINEGAP * i);
				graphics2D.drawLine(this.x1, this.y1 - LINEGAP * i, this.x2, this.y2 - LINEGAP * i);
			} else {
				graphics2D.drawLine(this.x1 + LINEGAP * i, this.y1, this.x2 + LINEGAP * i, this.y2);
				graphics2D.drawLine(this.x1 - LINEGAP * i, this.y1, this.x2 - LINEGAP * i, this.y2);
			}
		}
	}
}
