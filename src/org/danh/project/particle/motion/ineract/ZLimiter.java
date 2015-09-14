package org.danh.project.particle.motion.ineract;

import org.sh.gpf.motion.IInteractor;
import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.Particles;

public abstract class ZLimiter<T extends IParticle> implements IInteractor<T> {
	public static enum LimitType {
		LESS_THAN, GREATER_THAN;
	}

	double limit;
	LimitType type;

	public ZLimiter(double limit, LimitType type) {
		this.limit = limit;
		this.type = type;
	}

	public void applyInteractions(Particles<T> particles, double dt) {
		Particles<T> toRemove = new Particles<T>();

		for (T particle : particles) {
			switch (this.type) {
			case LESS_THAN:
				if (particle.getZ() < this.limit) {
					toRemove.add(particle);
				}
				break;
			case GREATER_THAN:
				if (particle.getZ() > this.limit) {
					toRemove.add(particle);
				}
				break;
			}
		}

		doRemoval(particles, toRemove);
	}

	public abstract Particles<T> doRemoval(Particles<T> before, Particles<T> toRemove);
}