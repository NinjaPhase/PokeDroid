package com.pokedroid.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pokedroid.map.TileMap;
import com.pokedroid.util.TextureUtils;

/**
 * <p>The {@code Player} is the {@link Entity} that the player will control.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class Player extends Entity {
	private static final float MOVE_SPEED = 128f;

	private EntityDirection moveDirection, nextMoveDirection;
	private int targetX, targetY;
	private TileMap map;
	
	private Animation[] anims;
	private float animTime;
	private boolean step;

	/**
	 * <p>Constructor for a new {@code Player}.</p>
	 * 
	 * @param map The map the player will start on.
	 * @param walk The walking texture.
	 */
	public Player(TileMap map, Texture walk) {
		super();
		this.map = map;
		this.map.getEntityList().add(this);
		this.anims = TextureUtils.createRMAnimation(walk, 0.25f);
		this.position.set(map.getStartX()*map.getTileset().getWidth(), map.getStartY()*map.getTileset().getHeight());
		this.tileX = this.targetX = map.getStartX();
		this.tileY = this.targetY = map.getStartY();
	}

	@Override
	public void update(float timeDelta) {
		if(nextMoveDirection == EntityDirection.DIRECTION_UP
				&& moveDirection == null && canMove(tileX, tileY+1)) {
			moveDirection = direction = EntityDirection.DIRECTION_UP;
			targetY = tileY+1;
			velocity.y = MOVE_SPEED;
			animTime = (step) ? (anims[direction.ordinal()].getFrameDuration()*3) :
				(anims[direction.ordinal()].getFrameDuration());
			step = !step;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_DOWN
				&& moveDirection == null && canMove(tileX, tileY-1)) {
			moveDirection = direction = EntityDirection.DIRECTION_DOWN;
			targetY = tileY-1;
			velocity.y = -MOVE_SPEED;
			animTime = (step) ? (anims[direction.ordinal()].getFrameDuration()*3) :
				(anims[direction.ordinal()].getFrameDuration());
			step = !step;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_LEFT
				&& moveDirection == null && canMove(tileX-1, tileY)) {
			moveDirection = direction = EntityDirection.DIRECTION_LEFT;
			targetX = tileX-1;
			velocity.x = -MOVE_SPEED;
			animTime = (step) ? (anims[direction.ordinal()].getFrameDuration()*3) :
				(anims[direction.ordinal()].getFrameDuration());
			step = !step;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_RIGHT
				&& moveDirection == null && canMove(tileX+1, tileY)) {
			moveDirection = direction = EntityDirection.DIRECTION_RIGHT;
			targetX = tileX+1;
			velocity.x = MOVE_SPEED;
			animTime = (step) ? (anims[direction.ordinal()].getFrameDuration()*3) :
				(anims[direction.ordinal()].getFrameDuration());
			step = !step;
		} else {
			if(moveDirection != null) {
				animTime += timeDelta;
				if(moveDirection == EntityDirection.DIRECTION_UP &&
						position.y > targetY*map.getTileset().getHeight()) {
					tileY = targetY;
					if(nextMoveDirection == EntityDirection.DIRECTION_UP
							&& canMove(tileX, tileY+1)) {
						targetY = tileY+1;
					} else {
						moveDirection = null;
						velocity.y = 0f;
						position.y = tileY*map.getTileset().getHeight();
						animTime = 0f;
					}
				} else if(moveDirection == EntityDirection.DIRECTION_DOWN &&
						position.y < targetY*map.getTileset().getHeight()) {
					tileY = targetY;
					if(nextMoveDirection == EntityDirection.DIRECTION_DOWN
							 && canMove(tileX, tileY-1)) {
						targetY = tileY-1;
					} else {
						moveDirection = null;
						velocity.y = 0f;
						position.y = tileY*map.getTileset().getHeight();
						animTime = 0f;
					}

				} else if(moveDirection == EntityDirection.DIRECTION_LEFT &&
						position.x < targetX*map.getTileset().getWidth()) {
					tileX = targetX;
					if(nextMoveDirection == EntityDirection.DIRECTION_LEFT
							 && canMove(tileX-1, tileY)) {
						targetX = tileX-1;
					} else {
						moveDirection = null;
						velocity.x = 0f;
						position.x = tileX*map.getTileset().getWidth();
						animTime = 0f;
					}
				} else if(moveDirection == EntityDirection.DIRECTION_RIGHT &&
						position.x > targetX*map.getTileset().getWidth()) {
					tileX = targetX;
					if(nextMoveDirection == EntityDirection.DIRECTION_RIGHT
							 && canMove(tileX+1, tileY)) {
						targetX = tileX+1;
					} else {
						moveDirection = null;
						velocity.x = 0f;
						position.x = tileX*map.getTileset().getWidth();
						animTime = 0f;
					}
				}
			}
		}
		position.mulAdd(velocity, timeDelta);
	}

	@Override
	public void render(Batch batch) {
		TextureRegion reg = anims[direction.ordinal()].getKeyFrame(animTime);
		batch.draw(reg,
				position.x+(map.getTileset().getWidth()/2f)-(reg.getRegionWidth()/2f), position.y);
	}

	/**
	 * <p>Moves the {@code Player} in a certain direction, if {@code null} then the
	 * {@code Player} will stop on the next tile.</p>
	 * 
	 * @param direction The direction, {@code null} to stop.
	 */
	public void move(EntityDirection direction) {
		if(direction != null && moveDirection == null)
			this.direction = direction;
		this.nextMoveDirection = direction;
	}
	
	/**
	 * <p>Checks whether the player can move to this position.</p>
	 * 
	 * @param x The x position.
	 * @param y The y position.
	 * @return Whether the player can move.
	 */
	private boolean canMove(int x, int y) {
		return map.canMove(x, y);
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
