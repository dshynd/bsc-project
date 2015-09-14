package org.danh.project.particle.processors;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleProcessor;
import org.sh.gpf.particle.Particles;

public class PointToPointTranslation<T extends IParticle> implements IParticleProcessor<T> {
	DynamicTranslation<T> translator;
	int numSteps;
	int stepsTaken = 0;

	public PointToPointTranslation(double x1, double y1, double x2, double y2, int numSteps) {
		this.numSteps = numSteps;
		double dx = (x2 - x1) / numSteps;
		double dy = (y2 - y1) / numSteps;
		this.translator = new DynamicTranslation<T>(x1, y1, 0.0, dx, dy, 0.0);
	}

	public void process(Particles<? extends T> particles) {
		if (!isFinished()) {
			this.translator.process(particles);
			this.stepsTaken += 1;
		}
	}

	public boolean isFinished() {
		return this.stepsTaken == this.numSteps;
	}
}