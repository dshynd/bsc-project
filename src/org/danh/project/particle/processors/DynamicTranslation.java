package org.danh.project.particle.processors;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleProcessor;
import org.sh.gpf.particle.Particles;
import org.sh.gpf.particle.processors.Translation;

public class DynamicTranslation<T extends IParticle> implements IParticleProcessor<T> {
	double x;
	double y;
	double z;
	double dX;
	double dY;
	double dZ;
	Translation<T> currentTranslation;

	public DynamicTranslation(double x, double y, double z, double dX, double dY, double dZ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dX = dX;
		this.dY = dY;
		this.dZ = dZ;
		this.currentTranslation = new Translation<T>(this.x, this.y, this.z);
	}

	public void process(Particles<? extends T> particles) {
		this.currentTranslation.process(particles);
		this.x += this.dX;
		this.y += this.dY;
		this.z += this.dZ;
		this.currentTranslation = new Translation<T>(this.x, this.y, this.z);
	}
}