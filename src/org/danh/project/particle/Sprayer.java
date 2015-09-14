package org.danh.project.particle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import org.danh.project.util.PolarCoords;
import org.sh.gpf.particle.IParticle;

public class Sprayer {
	Random random = new Random();

	public Sprayer() {
	}

	public Sprayer(long seed) {
		this.random = new Random(seed);
	}

	public void setRandomSeed(long seed) {
		this.random.setSeed(seed);
	}

	private float getOrientation(IParticle particle) {
		return (float) Math.atan2(particle.getVY(), particle.getVX());
	}

	private float getTilt(IParticle particle) {
		return (float) Math.atan(particle.getVZ() / getXYVelocity(particle));
	}

	private float getXYVelocity(IParticle particle) {
		return (float) Math.sqrt(Math.pow(particle.getVX(), 2.0) + Math.pow(particle.getVY(), 2.0));
	}

	private float getVelocity(IParticle particle) {
		return (float) Math.sqrt(Math.pow(particle.getVZ(), 2.0) + Math.pow(getXYVelocity(particle), 2.0));
	}

	public void drawSpray(Graphics2D g, IParticle particle, int x, int y) {
		float orientation = getOrientation(particle) + (float) Math.toRadians(90.0);
		float tilt = getTilt(particle);
		tilt = (float) (Math.toRadians(90.0D) - Math.abs(tilt));
		float absZ = (float) Math.abs(particle.getVZ());

		// Calculate centre of spray
		float nx = (float) (Math.sin(orientation) * Math.sin(tilt));
		float ny = (float) (-Math.cos(orientation) * Math.sin(tilt));
		float nz = (float) Math.cos(tilt);
		if (nz < 0.05D) {
			return;
		}
		float cd = absZ / nz;
		float cx = nx * cd;
		float cy = ny * cd;

		ArrayList<PolarCoords> points = new ArrayList<PolarCoords>();
		double maxR = 0.0;
		for (int i = 0; i < 2000; i++) {
			double direction = this.random.nextDouble() * Math.PI * 2.0;
			double dispersion = this.random.nextGaussian() * 0.2;

			double vx = Math.cos(direction) * dispersion;
			double vy = Math.sin(direction) * dispersion;
			double vz = 1.0;

			// Apply tilt
			double temp = vy;
			vy = temp * Math.cos(tilt) - vz * Math.sin(tilt);
			vz = temp * Math.sin(tilt) + vz * Math.cos(tilt);

			// Apply orientation
			temp = vx;
			vx = temp * Math.cos(orientation) - vy * Math.sin(orientation);
			vy = temp * Math.sin(orientation) + vy * Math.cos(orientation);

			// Calculate impact point
			if (vz >= 0.05) {
				float pd = (float) (absZ / vz);
				float px = (float) (vx * pd);
				float py = (float) (vy * pd);

				int ox = (int) (px - cx);
				int oy = (int) (py - cy);

				double angle = Math.atan2(oy, ox);
				double r = Math.sqrt(Math.pow(ox, 2.0) + Math.pow(oy, 2.0));

				if (r > maxR) {
					maxR = r;
				}

				points.add(new PolarCoords(r, angle));
			}
		}

		double conntectedLimit = Math.max(0.5 * maxR, 75.0);

		for (PolarCoords point : points) {
			AffineTransform trans = g.getTransform();
			g.translate(x, y);
			g.rotate(point.getAngle());

			int r = (int) Math.round(point.getRadius());
			int limit = (int) Math.round(conntectedLimit + this.random.nextGaussian() * 25.0);

			if (r < limit) {
				g.fillOval(0, -4, r, 8);
			} else {
				int size = 3 - this.random.nextInt(3);
				g.fillOval(r - 1, -1, size, size);
			}

			g.setTransform(trans);
		}
	}
}