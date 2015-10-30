package com.pokedroid.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

/**
 * <p>An {@code Entity} is a physical object on the .</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public abstract class Entity {
	
	protected Vector3 position, velocity;
	protected int tileX, tileY;
	protected EntityDirection lookDirection;
	
	/**
	 * <p>Constructor for a {@code Entity}.</p>
	 */
	public Entity() {
		this.lookDirection = EntityDirection.DIRECTION_DOWN;
		this.position = new Vector3();
		this.velocity = new Vector3();
	}
	
	/**
	 * <p>Updates the {@code Entity}.</p>
	 * 
	 * @param timeDelta The delta time.
	 */
	public abstract void update(float timeDelta);
	
	/**
	 * <p>Renders the {@code Entity}.</p>
	 * 
	 * @param batch The batch to render onto.
	 */
	public abstract void render(Batch batch);
	
	/**
	 * <p>Sets the direction the {@code Entity} is looking, if
	 * {@code null} is passed then the look direction will not
	 * be changed as the {@code Entity} must look in a certain
	 * direction.</p>
	 * 
	 * @param lookDirection The new look direction.
	 */
	public void setLookDirection(EntityDirection lookDirection) {
		if(lookDirection == null)
			return;
		this.lookDirection = lookDirection;
	}
	
	/**
	 * <p>Gets the x location of the {@code Entity}.</p>
	 * 
	 * @return The x location of the {@code Entity}.
	 */
	public float getX() {
		return this.position.x;
	}
	
	/**
	 * <p>Gets the y location of the {@code Entity}.</p>
	 * 
	 * @return The y location of the {@code Entity}.
	 */
	public float getY() {
		return this.position.y;
	}
	
	/**
	 * <p>Gets the tile x.</p>
	 * 
	 * @return The x tile this {@code Entity} is on.
	 */
	public int getTileX() {
		return this.tileX;
	}
	
	/**
	 * <p>Gets the tile y.</p>
	 * 
	 * @return The y tile this {@code Entity} is on.
	 */
	public int getTileY() {
		return this.tileY;
	}
	
	/**
	 * <p>Gets the look direction of the {@code Entity}.</p>
	 * 
	 * @return The look direction of the {@code Entity}.
	 */
	public EntityDirection getLookDirection() {
		return this.lookDirection;
	}
	
}
