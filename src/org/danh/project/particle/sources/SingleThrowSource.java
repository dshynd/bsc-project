package org.danh.project.particle.sources;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.Particles;

public abstract class SingleThrowSource<T extends IParticle> extends ThrowSource<T> {
	boolean dropped = false;

	public SingleThrowSource(double height, double yaw, double pitch, double force, int toThrow) {
		super(height, yaw, pitch, force, toThrow);
	}

	public Particles<T> generate(double dt) {
		Particles<T> particles = new Particles<T>();
		if (!this.dropped) {
			particles.addAll(generateParticles(this.toThrow * dt));
			
			for(T particle : particles){
				particle.setZ(this.height);
				particle.setVX(this.vX);
				particle.setVY(this.vY);
				particle.setVZ(this.vZ);
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