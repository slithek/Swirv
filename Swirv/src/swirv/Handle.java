package swirv;

import javafx.geometry.*;
import javafx.scene.canvas.*;


public interface Handle {
	
	final int width = 42;
	final int height = 42;

	public Rectangle2D getBoundary();
	public boolean intersects(Obstacle o);
	public void render(GraphicsContext graphics);
	
}
