package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import org.danh.project.util.PolarCoords;

public class BrushedLineComponentV2 extends LineComponent {
	int width;
	long seed;
	double skipChance;
	static final transient double STROKE_WIDTH = 2.0;
	static final transient double STROKE_OFFSET = 1.0;
	static final transient double START_END_VARIANCE = 2.5;

	public BrushedLineComponentV2(int x1, int y1, int x2, int y2, int width, Color color, long seed, double skipChance) {
		super(x1, y1, x2, y2, color);
		this.width = width;
		this.skipChance = skipChance;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BrushedLineComponentV2)) {
			return false;
		}
		BrushedLineComponentV2 other = (BrushedLineComponentV2) obj;
		return (this.x1 == other.x1) && (this.y1 == other.y1) && (this.x2 == other.x2) && (this.y2 == other.y2) && (this.width == other.width) && (this.seed == other.seed) && (this.color.equals(other.color));
	}

	public int hashCode() {
		return this.x1 ^ this.y1 ^ this.x2 ^ this.y2 ^ this.width ^ (int) this.seed ^ this.color.hashCode();
	}

	public void visit(Graphics2D graphics2D) {
		Random random = new Random(this.seed);

		PolarCoords polarcoords = PolarCoords.fromXY(this.x2 - this.x1, this.y2 - this.y1);

		AffineTransform affinetransform = graphics2D.getTransform();
		graphics2D.translate(this.x1, this.y1);
		graphics2D.rotate(polarcoords.getAngle());
		graphics2D.setColor(this.color);

		boolean isEven = this.width % 2 == 0;
		
		for (int i = 0; i < this.width; i++) {
			
			//Calculate offset from centre of brush stroke
			double y;
			if (isEven) {
				y = (i/2) + 0.5; 
			} else {
				y = Math.ceil(i / 2.0);
			}
			
			//Alternate between sides of centre line
			if (i % 2 != 0){
				y *= -1.0;
			}
			
			double numSteps = Math.max(polarcoords.getRadius() / 20, 15);
			double stepSize = polarcoords.getRadius() / numSteps;

			boolean doPaint = false;
			double start = 0.0;
			
			//Move along line in steps.
			for (double currentPosition = random.nextGaussian() * START_END_VARIANCE; currentPosition <= polarcoords.getRadius(); currentPosition += stepSize) {
				if (random.nextDouble() <= this.skipChance) {
					if (doPaint) {
						//Draw stroke up to previous segment if needed
						graphics2D.fill(new Ellipse2D.Double(start, y - STROKE_OFFSET, currentPosition - start, STROKE_WIDTH));
						doPaint = false;
					}
				} else if (!doPaint) { 
					//Start new stroke if this is first segment not skipped since previous stroke
					start = currentPosition;
					doPaint = true;
				}
			}

			if (doPaint) {
				//Draw final stroke if needed
				double radius = polarcoords.getRadius() + random.nextGaussian() * START_END_VARIANCE;
				graphics2D.fill(new Ellipse2D.Double(start, y - STROKE_OFFSET, radius - start, STROKE_WIDTH));
			}
		}

		graphics2D.setTransform(affinetransform);
	}
}
