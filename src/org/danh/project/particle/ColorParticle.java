package org.danh.project.particle;

import java.awt.Color;
import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.SimpleParticle;

public class ColorParticle extends SimpleParticle implements IParticle {
	Color color;

	public ColorParticle(double x, double y, double z, double vx, double vy, double vz, Color color) {
		super(vx, vy, vz);
		setX(x);
		setY(y);
		setZ(z);
		this.color = color;
	}

	public ColorParticle(Color color) {
		super(0.0, 0.0, 0.0);
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public String toString() {
		return "(" + getX() + "," + getY() + "," + getZ() + ") - <" + getVX() + "," + getVY() + "," + getVZ() + "> - " + getColor();
	}
}