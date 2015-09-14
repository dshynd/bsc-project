package org.danh.project.particle.sources;

import java.util.List;

import org.sh.gpf.particle.IParticle;
import org.sh.gpf.particle.IParticleProcessor;
import org.sh.gpf.particle.IParticleSource;
import org.sh.gpf.particle.Particles;
import org.sh.gpf.util.NonNullArrayList;

public class ParticleCreationPipe<T extends IParticle> implements IParticleSource<T> {
	IParticleSource<? extends T> source;
	NonNullArrayList<IParticleProcessor<? super T>> processors;

	public ParticleCreationPipe(IParticleSource<? extends T> source) {
		this.source = source;
		this.processors = new NonNullArrayList<IParticleProcessor<? super T>>();
	}

	public List<IParticleProcessor<? super T>> getProcessorList() {
		return this.processors;
	}

	public void setSource(IParticleSource<T> source) {
		this.source = source;
	}

	public IParticleSource<? extends T> getSource() {
		return this.source;
	}

	public Particles<T> generate(double number) {
		Particles<T> particles = new Particles<T>();
		particles.addAll(this.source.generate(number));
		
		for(IParticleProcessor<? super T> processor : this.processors){
			processor.process(particles);
		}
		
		return particles;
	}
}