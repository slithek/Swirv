package swirv;

import javafx.scene.transform.Rotate;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;


public abstract class HandleImpl implements Handle {

	protected Image image;
	final double velocity = 2.5;
	protected int upDown;
	protected double positionX;
	protected double positionY;
	protected double boundaryX;
	protected double boundaryY;
	protected double rotationCounter;
	protected int width;
	protected int height;
	protected final double boundLineancy = 6;
	
	public HandleImpl(Image image, double initialPositionX, double initialPositionY, int upDown) {
		this.image = image;
		this.upDown = upDown;
		this.positionX = initialPositionX;
		this.positionY = initialPositionY;
		this.boundaryX = initialPositionX;
		this.boundaryY = initialPositionY;
		this.rotationCounter = 0;
		this.width = (int)image.getWidth();
		this.height = (int)image.getHeight();
	}
	
	@Override
	public Rectangle2D getBoundary() {
		return new Rectangle2D(boundaryX+this.boundLineancy/2.0, boundaryY+this.boundLineancy/2.0, width-this.boundLineancy, height-this.boundLineancy);
	}
	
	@Override
	public boolean intersects(Obstacle o) {
		return o.getBoundary().intersects(getBoundary());
	}
	
	@Override
	public abstract void render(GraphicsContext graphics);
	
	public abstract void renderStill(GraphicsContext graphics);
	
	protected void rotate(GraphicsContext graphics, double angle, double pX, double pY) {
        Rotate r = new Rotate(angle, pX, pY);
        graphics.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
	
}
