package org.game.math;

import java.awt.Graphics;

/**
 * <p>A collision box is used to test for collision with another collision
 * box.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class CollisionBox {
	
	private Vector2 position;
	private double width, height;
	private double left, right, top, bottom;
	
	/**
	 * <p>Creates a new collision box.</p>
	 * 
	 * @param position The position of this collision box.
	 * @param width The width of the collision box.
	 * @param height The height of the collision box.
	 */
	public CollisionBox(Vector2 position, double width, double height) {
		this.position = new Vector2(position);
		this.width = width;
		this.height = height;
		update();
	}
	
	public void setPosition(double x, double y) {
		this.position.set(x, y);
		update();
	}
	
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
		update();
	}
	
	/**
	 * <p>Checks for an intersection.</p>
	 * 
	 * @param other The other box to check against.
	 * @return Whether there was intersections.
	 */
	public boolean intersects(final CollisionBox other) {
		if(left > other.right ||
		   right < other.left ||
		   bottom < other.top ||
		   top > other.bottom)
			return false;
		return true;
	}
	
	/**
	 * <p>Updates this collision box.</p>
	 */
	public void update() {
		left = position.x;
		right = position.x+width;
		top = position.y;
		bottom = position.y+height;
	}
	
	public void render(Graphics g) {
		g.drawRect((int)position.x, (int)position.y, (int)width, (int)height);
	}

}
