package org.danh.project.particle.processors;

import java.util.Random;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleProcessor;
import org.sh.gpf.particle.Particles;

public class RadiusRandomiser<T extends IParticle> implements IParticleProcessor<T> {
	Random random;
	double maxR;

	public RadiusRandomiser(double maxR) {
		this.maxR = maxR;
		this.random = new Random();
	}

	public RadiusRandomiser(double maxR, long seed) {
		this.maxR = maxR;
		this.random = new Random(seed);
	}

	public void process(Particles<? extends T> particles) {
		for(T particle : particles){
			double angle = this.random.nextDouble() * Math.PI * 2.0;
			double r = Math.sqrt(this.random.nextDouble() * this.maxR);
			particle.setVX(r * Math.cos(angle));
			particle.setVY(r * Math.sin(angle));
		}
	}
}