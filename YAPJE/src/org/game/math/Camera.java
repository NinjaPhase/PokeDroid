package org.game.math;

import java.awt.geom.AffineTransform;

/**
 * <p>A simple 2d camera that is able to translate the graphics it is
 * attached to.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class Camera {
	
	public Vector2 position;
	
	public Camera() {
		position = new Vector2();
	}
	
	public Camera translate(double x, double y) {
		position.add(x, y);
		return this;
	}
	
	public Camera translate(Vector2 v) {
		return translate(v.x, v.y);
	}
	
	public AffineTransform getTransform() {
		AffineTransform transform = new AffineTransform();
		transform.translate(-position.x, -position.y);
		return transform;
	}
	
}
