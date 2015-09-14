package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Random;

import org.danh.project.particle.ColorParticle;
import org.danh.project.particle.motion.accel.GravityAccelerator;
import org.danh.project.particle.motion.ineract.ZLimiter;
import org.danh.project.particle.processors.RadiusRandomiser;
import org.danh.project.particle.sources.ParticleCreationPipe;
import org.danh.project.particle.sources.SingleDropSource;
import org.sh.gpf.motion.Animator;
import org.sh.gpf.particle.Particles;
import org.sh.gpf.particle.processors.Translation;

public class DropComponent implements IImageComponent {
	int x;
	int y;
	double dropHeight;
	Color color;
	long seed;
	transient Dimension dimension = new Dimension(1024, 1024);

	public DropComponent(int x, int y, double dropHeight, Color color, long seed) {
		this.x = x;
		this.y = y;
		this.dropHeight = dropHeight;
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
		final Random random = new Random(this.seed);
		BufferedImage image = new BufferedImage((int) this.dimension.getWidth(), (int) this.dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Animator<ColorParticle> animator = new Animator<ColorParticle>(0.1);
		
		SingleDropSource<ColorParticle> source = new SingleDropSource<ColorParticle>(this.dropHeight, 0.0, 1) {
			public Particles<ColorParticle> generateParticles(double number) {
				Particles<ColorParticle> particles = new Particles<ColorParticle>();
				for (int i = 0; i < number; i++) {
					particles.add(new ColorParticle(DropComponent.this.color));
				}
				return particles;
			}
		};
		
		ParticleCreationPipe<ColorParticle> creator = new ParticleCreationPipe<ColorParticle>(source);
		creator.getProcessorList().add(new RadiusRandomiser<ColorParticle>(20.0, this.seed));
		creator.getProcessorList().add(new Translation<ColorParticle>(this.x, this.y, 0.0));
		animator.addSource(creator);
		
		animator.addAccelerator(new GravityAccelerator<ColorParticle>());
		
		ZLimiter<ColorParticle> limiter = new ZLimiter<ColorParticle>(0.0, ZLimiter.LimitType.LESS_THAN) {
			public Particles<ColorParticle> doRemoval(Particles<ColorParticle> before, Particles<ColorParticle> toRemove) {
				for(ColorParticle particle : toRemove){
					g.setColor(new Color(particle.getColor().getRGB()));
					
					int force = (int) Math.pow(particle.getVZ(), 2.0);
					int radius = force / 8;
					g.fillOval((int) particle.getX() - radius, (int) particle.getY() - radius, radius * 2, radius * 2);
					
					for (int i = 0; i < 120; i++) {
						double angle = random.nextDouble() * Math.PI * 2.0;
						int r = (int) Math.round(radius * 1.2 + random.nextGaussian() * (radius * 0.1));
						
						AffineTransform trans = g.getTransform();
						g.translate(particle.getX(), particle.getY());
						g.rotate(angle);
						g.fillOval(0, 0, r, r / 6);
						g.setTransform(trans);
					}
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
