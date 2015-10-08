package com.pokedroid.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.pokedroid.map.TileMap;

/**
 * <p>An {@code Entity} is a physical object on the .</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public abstract class Entity {
	
	protected Vector2 position, velocity;
	protected int tileX, tileY;
	protected EntityDirection direction;
	
	/**
	 * <p>Constructor for a {@code Entity}.</p>
	 */
	public Entity() {
		this.direction = EntityDirection.DIRECTION_DOWN;
		this.position = new Vector2();
		this.velocity = new Vector2();
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
	
}
