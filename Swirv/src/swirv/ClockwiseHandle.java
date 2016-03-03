package swirv;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ClockwiseHandle extends HandleImpl {

	public ClockwiseHandle(Image image, double initialPositionX, double initialPositionY, int upDown) {
		super(image, initialPositionX, initialPositionY, upDown);
	}
	
	@Override
	public void render(GraphicsContext graphics) {
		//SAVE CURRENT GRAPHICS STATE AND ROTATE CANVAS
			graphics.save();
			if (this.rotationCounter >= 360) {
				this.rotationCounter = 0;
			}
			rotate(graphics, this.velocity + this.rotationCounter, 250, 250);
			this.rotationCounter += this.velocity;
		//DRAW IMAGE AND ROTATE BACK
			graphics.drawImage(this.image, positionX, positionY);
		//RESTORE GRAPHICS TO ORIGINAL ROTATION
			graphics.restore();
		//BOUNDARY TEST
			//GET NEW BOUNDARY VALUES
				this.boundaryX = 215*Math.cos(Math.toRadians((this.rotationCounter+this.upDown)))+AppRunner.width/2.0-15;
				this.boundaryY = 215*Math.sin(Math.toRadians((this.rotationCounter+this.upDown)))+AppRunner.height/2.0-15;
//			Paint fillSave = graphics.getFill();
//			graphics.setFill(Color.BLUE);
//			graphics.fillRect(boundaryX+this.boundLineancy/2.0, boundaryY+this.boundLineancy/2.0, width-this.boundLineancy, height-this.boundLineancy);
//			graphics.setFill(fillSave);
	}
	
	@Override
	public void renderStill(GraphicsContext graphics) {
		//SAVE CURRENT GRAPHICS STATE AND ROTATE CANVAS
			graphics.save();
			rotate(graphics, this.rotationCounter, 250, 250);
		//DRAW IMAGE AND ROTATE BACK
			graphics.drawImage(this.image, positionX, positionY);
		//RESTORE GRAPHICS TO ORIGINAL ROTATION
			graphics.restore();
	}

}
