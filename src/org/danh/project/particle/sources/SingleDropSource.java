package org.danh.project.particle.sources;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.Particles;

public abstract class SingleDropSource<T extends IParticle> extends DropSource<T> {
	protected boolean dropped = false;

	public SingleDropSource(double height, double dropV, int dropsPerDt) {
		super(height, dropV, dropsPerDt);
	}

	public Particles<T> generate(double dt) {
		Particles<T> particles = new Particles<T>();
		if (!this.dropped) {
			particles.addAll(generateParticles(this.dropsPerDt * dt));
			for(T particle : particles){
				particle.setZ(this.height);
				particle.setVZ(this.dropV);
			}
			this.dropped = true;
		}
		return particles;
	}

	public void reset() {
		this.dropped = false;
	}

	public abstract Particles<T> generateParticles(double number);
}