package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import org.danh.project.particle.ColorParticle;
import org.danh.project.particle.Sprayer;
import org.danh.project.particle.motion.accel.GravityAccelerator;
import org.danh.project.particle.motion.ineract.ZLimiter;
import org.danh.project.particle.sources.ParticleCreationPipe;
import org.danh.project.particle.sources.SingleThrowSource;
import org.sh.gpf.motion.Animator;
import org.sh.gpf.particle.Particles;
import org.sh.gpf.particle.processors.Translation;

public class ThrowComponent implements IImageComponent {
	int x;
	int y;
	double throwHeight;
	double throwYaw;
	double throwPitch;
	double velocity;
	Color color;
	long seed;
	transient Dimension dimension = new Dimension(1024, 1024);

	public ThrowComponent(int x, int y, double throwHeight, double throwYaw, double throwPitch, double velocity, Color color, long seed) {
		this.x = x;
		this.y = y;
		this.throwHeight = throwHeight;
		this.throwYaw = throwYaw;
		this.throwPitch = throwPitch;
		this.velocity = velocity;
		this.color = color;
		this.seed = seed;
	}

	public void setDimensions(int width, int height) {
		this.dimension = new Dimension(width, height);
	}

	public void setDimensions(Dimension dimension) {
		this.dimension = dimension;
	}

	public void visit(Graphics2D graphics2D) {
		BufferedImage image = new BufferedImage((int) this.dimension.getWidth(), (int) this.dimension.getHeight(), 2);
		final Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Animator<ColorParticle> animator = new Animator<ColorParticle>(0.1);
		
		SingleThrowSource<ColorParticle> source = new SingleThrowSource<ColorParticle>(this.throwHeight, this.throwYaw, this.throwPitch, this.velocity, 1) {
			public Particles<ColorParticle> generateParticles(double number) {
				Particles<ColorParticle> localParticles = new Particles<ColorParticle>();
				for (int i = 0; i < number; i++) {
					localParticles.add(new ColorParticle(ThrowComponent.this.color));
				}
				return localParticles;
			}
		};
		
		ParticleCreationPipe<ColorParticle> creator = new ParticleCreationPipe<ColorParticle>(source);
		creator.getProcessorList().add(new Translation<ColorParticle>(this.x, this.y, 0.0));
		animator.addSource(creator);
		
		animator.addAccelerator(new GravityAccelerator<ColorParticle>());
		
		ZLimiter<ColorParticle> limiter = new ZLimiter<ColorParticle>(0.0D, ZLimiter.LimitType.LESS_THAN) {
			public Particles<ColorParticle> doRemoval(Particles<ColorParticle> before, Particles<ColorParticle> toRemove) {
				for(ColorParticle particle : toRemove) {
					g.setColor(new Color(particle.getColor().getRGB()));
					new Sprayer(ThrowComponent.this.seed).drawSpray(g, particle, ThrowComponent.this.x, ThrowComponent.this.y);
				}
				
				before.removeAll(toRemove);
				
				Particles<ColorParticle> ret = new Particles<ColorParticle>();
				ret.addAll(before);
				return ret;
			}
		};
		animator.addInteractor(limiter);
		
		Particles<ColorParticle> pis = animator.animate();
		while(!pis.isEmpty()){
			pis = animator.animate();
		}
		
		float[] scales = { 1.0F, 1.0F, 1.0F, this.color.getAlpha() / 255.0F };
		float[] offsets = new float[4];
		RescaleOp rescale = new RescaleOp(scales, offsets, null);
		graphics2D.drawImage(image, rescale, 0, 0);
	}
}
