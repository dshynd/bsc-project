package org.danh.project.particle.motion.accel;

import org.sh.gpf.motion.IAccelerator;
import org.sh.gpf.particle.IParticle;

public class GravityAccelerator<T extends IParticle> implements IAccelerator<T> {
	public void accelerate(T particle, double dt) {
		particle.setVZ(particle.getVZ() - 9.8 * dt);
	}
}