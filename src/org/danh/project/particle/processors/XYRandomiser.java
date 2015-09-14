package org.danh.project.particle.processors;

import java.util.Random;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleProcessor;
import org.sh.gpf.particle.Particles;

class XYRandomiser implements IParticleProcessor<IParticle> {
	Random random = new Random();
	double maxV;

	public XYRandomiser(double maxV) {
		this.maxV = maxV;
	}

	double getAmount() {
		double direction = this.random.nextBoolean() ? -1.0 : 1.0;
		return this.random.nextDouble() * this.maxV * direction;
	}

	public void process(Particles<? extends IParticle> particles) {
		for(IParticle particle : particles){
			particle.setVX(getAmount());
			particle.setVY(getAmount());
		}
	}
}