package swirv;

import javafx.scene.transform.Rotate;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;


public abstract class ObstacleImpl implements Obstacle {

	protected Image image;
	protected String imageLocation;
	protected double maxWidth;
	protected double maxHeight;
	public static double velocity = .8;
	public static final double initialVelocity = .8;
	protected double outVelocity = .8;
	protected double positionX;
	protected double positionY;
	protected double boundaryX;
	protected double boundaryY;
	protected double rotationCounter;
	protected int direction;
	protected double width;
	protected double height;
	protected double initialX;
	protected double initialY;
	protected double initialRotation;
	protected boolean exists;
	protected final double boundLineancy = 6;
	
	public ObstacleImpl(double initialPositionX, double initialPositionY) {
		this.positionX = initialPositionX;
		this.positionY = initialPositionY;
		this.initialX = initialPositionX;
		this.initialY = initialPositionY;
		this.boundaryX = initialPositionX;
		this.boundaryY = initialPositionY;
		this.exists = false;
		this.rotationCounter = Math.random()*360;
		this.initialRotation = rotationCounter;
		if (Math.random() < .5) {
			this.direction = -1;
		} else {
			this.direction = 1;
		}
	}
	
	@Override
	public Rectangle2D getBoundary() {
		if ( (width < boundLineancy) || (height < boundLineancy) ) {
			return new Rectangle2D(this.boundaryX, this.boundaryY, width, height);
		} else {
			return new Rectangle2D(this.boundaryX+this.boundLineancy/2.0, this.boundaryY+this.boundLineancy/2.0, width-this.boundLineancy, height-this.boundLineancy);
		}
	}
	
	@Override
	public boolean intersects(Handle h) {
		return h.getBoundary().intersects(getBoundary());
	}
	
	@Override
	public abstract void render(GraphicsContext graphics);
	
	public abstract void renderStill(GraphicsContext graphics);
	
	protected void rotate(GraphicsContext graphics, double angle, double pX, double pY) {
        Rotate r = new Rotate(angle, pX, pY);
        graphics.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
	
	public double getPositionY() {
		return this.positionY-this.width/2.0;
	}
	
	public void reset() {
		this.rotationCounter = Math.random()*360;
		if (Math.random() < .5) {
			this.direction = -1;
		} else {
			this.direction = 1;
		}
		this.positionX = this.initialX;
		this.positionY = this.initialY;
		this.exists = false;
	}
	
	public void setExists(boolean value) {
		this.exists = value;
	}
	
	public boolean exists() {
		return exists;
	}
	
}
