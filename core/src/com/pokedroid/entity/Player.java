package com.pokedroid.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.pokedroid.map.TileMap;

public class Player extends Entity {
	private static final float MOVE_SPEED = 64f;

	private EntityDirection moveDirection, nextMoveDirection;
	private int targetX, targetY;
	private TileMap map;

	private BitmapFont font;

	public Player(TileMap map) {
		font = new BitmapFont();
		this.map = map;
		this.map.getEntityList().add(this);
	}

	@Override
	public void update(float timeDelta) {
		if(nextMoveDirection == EntityDirection.DIRECTION_UP
				&& moveDirection == null) {
			moveDirection = EntityDirection.DIRECTION_UP;
			targetY = tileY+1;
			velocity.y = MOVE_SPEED;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_DOWN
				&& moveDirection == null) {
			moveDirection = EntityDirection.DIRECTION_DOWN;
			targetY = tileY-1;
			velocity.y = -MOVE_SPEED;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_LEFT
				&& moveDirection == null) {
			moveDirection = EntityDirection.DIRECTION_LEFT;
			targetX = tileX-1;
			velocity.x = -MOVE_SPEED;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_RIGHT
				&& moveDirection == null) {
			moveDirection = EntityDirection.DIRECTION_RIGHT;
			targetX = tileX+1;
			velocity.x = MOVE_SPEED;
		} else {
			if(moveDirection != null) {
				if(moveDirection == EntityDirection.DIRECTION_UP &&
						position.y > targetY*map.getTileset().getHeight()) {
					tileY = targetY;
					if(nextMoveDirection == EntityDirection.DIRECTION_UP) {
						targetY = tileY+1;
					} else {
						moveDirection = null;
						velocity.y = 0f;
						position.y = tileY*map.getTileset().getHeight();
					}
				} else if(moveDirection == EntityDirection.DIRECTION_DOWN &&
						position.y < targetY*map.getTileset().getHeight()) {
					tileY = targetY;
					if(nextMoveDirection == EntityDirection.DIRECTION_DOWN) {
						targetY = tileY-1;
					} else {
						moveDirection = null;
						velocity.y = 0f;
						position.y = tileY*map.getTileset().getHeight();
					}

				} else if(moveDirection == EntityDirection.DIRECTION_LEFT &&
						position.x < targetX*map.getTileset().getWidth()) {
					tileX = targetX;
					if(nextMoveDirection == EntityDirection.DIRECTION_LEFT) {
						targetX = tileX-1;
					} else {
						moveDirection = null;
						velocity.x = 0f;
						position.x = tileX*map.getTileset().getWidth();
					}
				} else if(moveDirection == EntityDirection.DIRECTION_RIGHT &&
						position.x > targetX*map.getTileset().getWidth()) {
					tileX = targetX;
					if(nextMoveDirection == EntityDirection.DIRECTION_RIGHT) {
						targetX = tileX+1;
					} else {
						moveDirection = null;
						velocity.x = 0f;
						position.x = tileX*map.getTileset().getWidth();
					}
				}
			}
		}
		position.mulAdd(velocity, timeDelta);
	}

	@Override
	public void render(Batch batch) {
		font.draw(batch, "!P!", position.x, position.y);
	}

	/**
	 * <p>Moves the {@code Player} in a certain direction, if {@code null} then the
	 * {@code Player} will stop on the next tile.</p>
	 * 
	 * @param direction The direction, {@code null} to stop.
	 */
	public void move(EntityDirection direction) {
		if(direction != null)
			this.direction = direction;
		this.nextMoveDirection = direction;
	}

	/**
	 * <p>Gets the position of the {@code Player}.</p>
	 * 
	 * @return The position of the {@code Player}.
	 */
	public Vector2 getPosition() {
		return this.position;
	}

}
