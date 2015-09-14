package org.danh.project.util;

import java.awt.geom.Point2D;

public class PolarCoords {
	double radius;
	double angle;

	public PolarCoords(double radius, double angle) {
		this.radius = radius;
		this.angle = angle;
	}

	public static PolarCoords fromXY(double x, double y) {
		return new PolarCoords(Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0)), Math.atan2(y, x));
	}

	public static Point2D fromPolar(double radius, double angle) {
		return new Point2D.Double(radius * Math.cos(angle), radius * Math.sin(angle));
	}

	public double getRadius() {
		return this.radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getAngle() {
		return this.angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public String toString() {
		return "(" + this.radius + ", " + this.angle + ")";
	}
}