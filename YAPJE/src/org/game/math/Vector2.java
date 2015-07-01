package org.game.math;

/**
 * <p>A 2D vector on the screen.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class Vector2 implements Cloneable {
	
	public double x, y;
	
	public Vector2() {
		this(0, 0);
	}
	
	public Vector2(Vector2 vector) {
		this(vector.x, vector.y);
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2 set(Vector2 vector) {
		return set(vector.x, vector.y);
	}
	
	public Vector2 add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2 add(Vector2 vector) {
		return add(vector.x, vector.y);
	}
	
	public Vector2 mul(double m) {
		this.x *= m;
		this.y *= m;
		return this;
	}
	
	public Vector2 copy() {
		return (Vector2) this.clone();
	}
	
	public Object clone() {
		return new Vector2(this);
	}
	
}
