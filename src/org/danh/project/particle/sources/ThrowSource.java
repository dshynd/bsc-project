package org.danh.project.particle.sources;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleSource;

abstract class ThrowSource<T extends IParticle> implements IParticleSource<T> {
	protected double height;
	protected double vX;
	protected double vY;
	protected double vZ;
	protected int toThrow;

	public ThrowSource(double height, double yaw, double pitch, double force, int toThrow) {
		this.height = height;
		this.vZ = (Math.sin(pitch) * force);
		double d = Math.cos(pitch) * force;
		this.vX = (d * Math.cos(yaw));
		this.vY = (d * Math.sin(yaw));
		this.toThrow = toThrow;
	}
}