package org.danh.project.particle.sources;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleSource;
import org.sh.gpf.particle.Particles;

public abstract class DropSource<T extends IParticle> implements IParticleSource<T> {
	protected double height;
	protected double dropV;
	protected int dropsPerDt;

	public DropSource(double height, double dropV, int dropsPerDt) {
		this.height = height;
		this.dropV = dropV;
		this.dropsPerDt = dropsPerDt;
	}

	public Particles<T> generate(double dt) {
		Particles<T> particles = generateParticles(this.dropsPerDt * dt);
		for(T particle : particles){
			particle.setZ(this.height);
			particle.setVZ(this.dropV);
		}
		return particles;
	}

	public abstract Particles<T> generateParticles(double number);
}