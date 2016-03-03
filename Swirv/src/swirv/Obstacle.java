package swirv;

import javafx.geometry.*;
import javafx.scene.canvas.*;

public interface Obstacle {

	public Rectangle2D getBoundary();
	public void render(GraphicsContext graphics);
	public boolean intersects(Handle h);
	
}
