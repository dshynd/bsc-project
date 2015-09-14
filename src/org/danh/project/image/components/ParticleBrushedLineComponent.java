package org.danh.project.image.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import org.danh.project.particle.ColorParticle;
import org.danh.project.particle.motion.accel.GravityAccelerator;
import org.danh.project.particle.motion.ineract.ZLimiter;
import org.danh.project.particle.processors.PointToPointTranslation;
import org.danh.project.particle.sources.DropSource;
import org.danh.project.particle.sources.ParticleCreationPipe;
import org.danh.project.util.PolarCoords;
import org.sh.gpf.motion.Animator;
import org.sh.gpf.particle.Particles;

public class ParticleBrushedLineComponent extends LineComponent {
	int width;
	long seed;
	transient Dimension dimension = new Dimension(1024, 1024);
	transient Animator<ColorParticle> animator;

	public ParticleBrushedLineComponent(int x1, int y1, int x2, int y2, int width, Color color, long seed) {
		super(x1, y1, x2, y2, color);
		this.width = width;
		this.seed = seed;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParticleBrushedLineComponent)) {
			return false;
		}
		ParticleBrushedLineComponent other = (ParticleBrushedLineComponent) obj;
		return (this.x1 == other.x1) && (this.y1 == other.y1) && (this.x2 == other.x2) && (this.y2 == other.y2)
				&& (this.width == other.width) && (this.seed == other.seed) && (this.color.equals(other.color));
	}

	public int hashCode() {
		return this.x1 ^ this.y1 ^ this.x2 ^ this.y2 ^ this.width ^ (int) this.seed ^ this.color.hashCode();
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
		
		this.animator = new Animator<ColorParticle>(0.1);
		
		addStroke(this.x1, this.y1, this.x2, this.y2);
		
		this.animator.addAccelerator(new GravityAccelerator<ColorParticle>());
		
		ZLimiter<ColorParticle> limiter = new ZLimiter<ColorParticle>(0.0D, ZLimiter.LimitType.LESS_THAN) {
			public Particles<ColorParticle> doRemoval(Particles<ColorParticle> before, Particles<ColorParticle> toRemove) {
				for(ColorParticle particle : toRemove){
					g.setColor(new Color(particle.getColor().getRGB()));
					AffineTransform trans = g.getTransform();
					
					PolarCoords coords;
					if ((particle.getX() == ParticleBrushedLineComponent.this.x1) && (particle.getY() == ParticleBrushedLineComponent.this.y1)) {
						coords = PolarCoords.fromXY(ParticleBrushedLineComponent.this.x2 - ParticleBrushedLineComponent.this.x1, ParticleBrushedLineComponent.this.y2 - ParticleBrushedLineComponent.this.y1);
						coords.setRadius(0.0);
					} else {
						coords = PolarCoords.fromXY(particle.getX() - ParticleBrushedLineComponent.this.x1, particle.getY() - ParticleBrushedLineComponent.this.y1);
					}
					
					g.translate(ParticleBrushedLineComponent.this.x1, ParticleBrushedLineComponent.this.y1);
					g.rotate(coords.getAngle());
					g.draw(new Ellipse2D.Double(coords.getRadius(), 0.0, 1.0, 1.0));
					
					double d1 = ParticleBrushedLineComponent.this.width % 2 == 0 ? 0.5D : 1.0D;
					int i = (Math.abs(coords.getAngle()) < Math.toRadians(45.0D)) || (Math.abs(coords.getAngle()) > Math.toRadians(135.0D)) ? 1 : 0;
					int j = ParticleBrushedLineComponent.this.width % 2 == 0 ? ParticleBrushedLineComponent.this.width : ParticleBrushedLineComponent.this.width - 1;
					for (int k = 0; k < j; k++) {
						int m = (int) Math.ceil(k / 2.0D);
						double d2 = k % 2 == 0 ? m * d1 : -1.0D * d1 * m;
						double d3;
						double d4;
						if (i != 0) {
							d3 = coords.getRadius();
							d4 = d2;
						} else {
							d3 = coords.getRadius() + d2;
							d4 = 0.0D;
						}
						g.draw(new Ellipse2D.Double(d3, d4, 1.0D, 1.0D));
					}
					g.setTransform(trans);
				}
				before.removeAll(toRemove);
				
				Particles<ColorParticle> ret = new Particles<ColorParticle>();
				ret.addAll(before);
				return ret;
			}
		};
		this.animator.addInteractor(limiter);

		Particles<ColorParticle> pis = animator.animate();
		while(!pis.isEmpty()){
			pis = animator.animate();
		}
		
		float[] scales = { 1.0F, 1.0F, 1.0F, this.color.getAlpha() / 255.0F };
		float[] offsets = new float[4];
		RescaleOp rescale = new RescaleOp(scales, offsets, null);
		graphics2D.drawImage(image, rescale, 0, 0);
	}

	void addStroke(double x1, double y1, double x2, double y2) {
		final PointToPointTranslation<ColorParticle> translate = new PointToPointTranslation<ColorParticle>(x1, y1, x2, y2, 2000);
		DropSource<ColorParticle> source = new DropSource<ColorParticle>(0.1, 0.0, 1) {
			public Particles<ColorParticle> generateParticles(double number) {
				Particles<ColorParticle> localParticles = new Particles<ColorParticle>();
				if (!translate.isFinished()) {
					for (int i = 0; i < number; i++) {
						localParticles.add(new ColorParticle(ParticleBrushedLineComponent.this.color));
					}
				}
				return localParticles;
			}
		};
		ParticleCreationPipe<ColorParticle> creator = new ParticleCreationPipe<ColorParticle>(source);
		creator.getProcessorList().add(translate);
		this.animator.addSource(creator);
	}
}
