package org.game.entity;

import java.awt.Graphics2D;

import org.game.GameConfig;
import org.game.math.Vector2;

public abstract class Entity {
	
	protected Vector2 position, velocity;
	
	/**
	 * <p>Creates a new entity.</p>
	 * 
	 * @param x The tile x.
	 * @param y The tile y.
	 */
	public Entity(int x, int y) {
		this.position = new Vector2(x*GameConfig.TILE_WIDTH, y*GameConfig.TILE_HEIGHT);
		this.velocity = new Vector2();
	}
	
	public void update(double timeDelta) {
		this.position.add(velocity.x * timeDelta, velocity.y * timeDelta);
	}
	
	public void render(Graphics2D g) {
		g.fillRect((int)position.x, (int)position.y, 32, 32);
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public Vector2 getVelocity() {
		return this.velocity;
	}
	
}
