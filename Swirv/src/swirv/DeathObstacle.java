package swirv;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

public class DeathObstacle extends ObstacleImpl {
	
	public DeathObstacle(double initialPositionX, double initialPositionY) {
		super(initialPositionX, initialPositionY);
		this.imageLocation = "/images/circleObstSmall.png";
		this.image = new Image(this.imageLocation, 0.0, 0.0, false, false);
		this.width = 0;
		this.height = 0;
		this.maxWidth = 60;
		this.maxHeight = 60;
	}
	
	Paint fillSave;
	
	@Override
	public void render(GraphicsContext graphics) {
		//ROTATION RESET IF > 360
			if (this.rotationCounter >= 360) {
				this.rotationCounter = 0;
			}
			//GET NEW BOUNDARY VALUES
				this.boundaryX = (AppRunner.height/2.0-(this.positionY))*Math.cos(Math.toRadians(this.direction*(this.rotationCounter+270)))+AppRunner.width/2.0-15;
				this.boundaryY = (AppRunner.height/2.0-(this.positionY))*Math.sin(Math.toRadians(this.direction*(this.rotationCounter+270)))+AppRunner.height/2.0-15;
		//INCREMENT ROTATIONCOUNTER
			this.rotationCounter += ObstacleImpl.velocity;
		//DRAW IMAGE, INCREASE OUTVELOCITY, AND INCREASE SIZE
			graphics.drawImage(this.image, boundaryX, boundaryY);
			positionY -= outVelocity;
			this.image = new Image(imageLocation, ((225-positionY)/ (500-((int)((50.0/1080.0)*500))) )*maxWidth, ((225-positionY)/ (500-((int)((50.0/1080.0)*500))) )*maxHeight, false, false);
			this.width = image.getWidth();
			this.height = image.getHeight();
		//TEST BOUNDARY
//			fillSave = graphics.getFill();
//			graphics.setFill(Color.RED);
//			graphics.fillRect(boundaryX+this.boundLineancy/2.0, boundaryY+this.boundLineancy/2.0, width-this.boundLineancy, height-this.boundLineancy);
//			graphics.setFill(fillSave);
	}
	
	@Override
	public void renderStill(GraphicsContext graphics) {
		//SAVE CURRENT GRAPHICS STATE AND ROTATE CANVAS
			graphics.save();
			rotate(graphics, -1.0*this.rotationCounter, 250, 250);
		//DRAW IMAGE AND ROTATE BACK
			graphics.drawImage(this.image, positionX, positionY);
		//RESTORE GRAPHICS TO ORIGINAL ROTATION
			graphics.restore();
	}
	
}
