package com.pokedroid.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokedroid.map.TileMap;
import com.pokedroid.util.TextureUtils;

/**
 * <p>A {@code HumanEntity} is a humanoid {@link Entity} that uses a 4x4 spritesheet
 * to create a 4 directional person.</p>
 * 
 * @author PoketronHacker
 * @version 28 October 2015
 *
 */
public abstract class HumanEntity extends Entity {
	public static final float STANDARD_ANIM_SPEED = 25f, STANDARD_MOVE_SPEED = 128f;

	protected TileMap map;
	protected Animation[] moveAnims;
	protected float animSpeed;
	protected float animTime;
	protected float moveSpeed;
	protected int jumpDist;

	protected EntityDirection moveDirection, nextMoveDirection;
	protected int targetX, targetY;
	protected boolean step;

	/**
	 * <p>Constructor for a new {@code HumanEntity}.</p>
	 * 
	 * @param texture The texture of the spritesheet.
	 * @param map The {@link TileMap} this {@code HumanEntity} is on.
	 * @param x The x location of the entity.
	 * @param y The y location of the entity.
	 */
	public HumanEntity(Texture texture, TileMap map, int x, int y) {
		this(texture, map, x, y, STANDARD_MOVE_SPEED);
	}

	/**
	 * <p>Constructor for a new {@code HumanEntity}.</p>
	 * 
	 * @param texture The texture of the spritesheet.
	 * @param map The {@link TileMap} this {@code HumanEntity} is on.
	 * @param x The x location of the entity.
	 * @param y The y location of the entity.
	 * @param moveSpeed The move speed of this {@code HumanEntity}.
	 */
	public HumanEntity(Texture texture, TileMap map, int x, int y, float moveSpeed) {
		this(texture, STANDARD_ANIM_SPEED/moveSpeed, map, x, y, moveSpeed);
	}

	/**
	 * <p>Constructor for a new {@code HumanEntity}.</p>
	 * 
	 * @param texture The texture of the spritesheet.
	 * @param animSpeed The speed of the animations.
	 * @param map The {@link TileMap} this {@code HumanEntity} is on.
	 * @param x The x location of the entity.
	 * @param y The y location of the entity.
	 * @param moveSpeed The move speed of this {@code HumanEntity}.
	 */
	public HumanEntity(Texture texture, float animSpeed, TileMap map, int x, int y, float moveSpeed) {
		super();
		if(texture == null)
			throw new NullPointerException("texture cannot be null.");
		if(map == null)
			throw new NullPointerException("map cannot be null.");
		this.map = map;
		this.moveAnims = TextureUtils.createRMAnimation(texture, animSpeed);
		this.animSpeed = animSpeed;
		this.moveSpeed = moveSpeed;
		this.setMap(map, x, y);
	}

	@Override
	public void update(float timeDelta) {
		applyMovement(timeDelta);
	}

	@Override
	public void render(Batch batch) {
		TextureRegion reg = moveAnims[lookDirection.ordinal()].getKeyFrame(animTime);
		batch.draw(reg,
				position.x+(map.getTileset().getWidth()/2f)-(reg.getRegionWidth()/2f), position.y+position.z);
	}

	/**
	 * <p>Applies movement to the {@code HumanEntity}.</p>
	 * 
	 * @param timeDelta The time delta.
	 */
	protected void applyMovement(float timeDelta) {
		if(nextMoveDirection == EntityDirection.DIRECTION_UP
				&& moveDirection == null && canMove(tileX, tileY+1)) {
			boolean ledge = false;
			for(int i = 0; i < map.getLayerCount(); i++) {
				ledge = map.getTileset().isDownLedge(map.getTile(i, tileX, tileY+1));
				if(ledge) break;
			}
			if(ledge)
				return;
			moveDirection = lookDirection = EntityDirection.DIRECTION_UP;
			targetY = tileY+1;
			velocity.y = moveSpeed;
			animTime = (step) ? (moveAnims[lookDirection.ordinal()].getFrameDuration()*3) :
				(moveAnims[lookDirection.ordinal()].getFrameDuration());
			step = !step;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_DOWN
				&& moveDirection == null && canMove(tileX, tileY-1)) {
			moveDirection = lookDirection = EntityDirection.DIRECTION_DOWN;
			targetY = tileY-1;

			boolean ledge = false;
			for(int i = 0; i < map.getLayerCount(); i++) {
				ledge = map.getTileset().isDownLedge(map.getTile(i, tileX, targetY));
				if(ledge) break;
			}
			if(ledge) {
				targetY = tileY-2;
				velocity.y = -(moveSpeed*2);
				velocity.z = moveSpeed*2;
				jump(-2, moveDirection);
			} else velocity.y = -moveSpeed;

			animTime = (step) ? (moveAnims[lookDirection.ordinal()].getFrameDuration()*3) :
				(moveAnims[lookDirection.ordinal()].getFrameDuration());
			step = !step;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_LEFT
				&& moveDirection == null && canMove(tileX-1, tileY)) {
			moveDirection = lookDirection = EntityDirection.DIRECTION_LEFT;
			targetX = tileX-1;
			velocity.x = -moveSpeed;
			animTime = (step) ? (moveAnims[lookDirection.ordinal()].getFrameDuration()*3) :
				(moveAnims[lookDirection.ordinal()].getFrameDuration());
			step = !step;
		} else if(nextMoveDirection == EntityDirection.DIRECTION_RIGHT
				&& moveDirection == null && canMove(tileX+1, tileY)) {
			moveDirection = lookDirection = EntityDirection.DIRECTION_RIGHT;
			targetX = tileX+1;
			velocity.x = moveSpeed;
			animTime = (step) ? (moveAnims[lookDirection.ordinal()].getFrameDuration()*3) :
				(moveAnims[lookDirection.ordinal()].getFrameDuration());
			step = !step;
		} else {
			if(moveDirection != null) {
				animTime += timeDelta;
				if(moveDirection == EntityDirection.DIRECTION_UP &&
						position.y > targetY*map.getTileset().getHeight()) {
					tileY = targetY;
					if(tileY >= 0 && tileY < map.getHeight())
						System.out.println(map.getTile(0, tileX, tileY) + ":" + map.getTile(1, tileX, tileY)
						+ ":" + map.getTile(2, tileX, tileY));
					boolean ledge = false;
					if(tileY >= 0 && tileY < map.getHeight()-1)
						for(int i = 0; i < map.getLayerCount(); i++) {
							ledge = map.getTileset().isDownLedge(map.getTile(i, tileX, tileY+1));
							if(ledge) break;
						}
					if(nextMoveDirection == EntityDirection.DIRECTION_UP
							&& canMove(tileX, tileY+1) && !ledge) {
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
					boolean ledge = false;
					if(tileY >= 0 && tileY < map.getHeight()-1)
						for(int i = 0; i < map.getLayerCount(); i++) {
							ledge = map.getTileset().isDownLedge(map.getTile(i, tileX, tileY-1));
							if(ledge) break;
						}
					if(tileY >= 0 && tileY < map.getHeight())
						System.out.println(map.getTile(0, tileX, tileY) + ":" + map.getTile(1, tileX, tileY)
						+ ":" + map.getTile(2, tileX, tileY));
					if(nextMoveDirection == EntityDirection.DIRECTION_DOWN
							&& canMove(tileX, tileY-1)) {
						targetY = tileY-1;
						if(ledge) {
							targetY = tileY-2;
							velocity.y = -(moveSpeed*2);
						} else velocity.y = -(moveSpeed);
					} else {
						moveDirection = null;
						velocity.y = 0f;
						position.y = tileY*map.getTileset().getHeight();
						animTime = 0f;
					}

				} else if(moveDirection == EntityDirection.DIRECTION_LEFT &&
						position.x < targetX*map.getTileset().getWidth()) {
					tileX = targetX;
					System.out.println(map.getTile(0, tileX, tileY) + ":" + map.getTile(1, tileX, tileY)
					+ ":" + map.getTile(2, tileX, tileY));
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
					System.out.println(map.getTile(0, tileX, tileY) + ":" + map.getTile(1, tileX, tileY)
					+ ":" + map.getTile(2, tileX, tileY));
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
		velocity.z += ((-moveSpeed)/(((targetY == tileY) ? Math.abs(targetX-tileX) : Math.abs(targetY-tileY))*this.jumpDist));
		position.mulAdd(velocity, timeDelta);
		if(position.z < 0) {
			position.z = velocity.z = 0f;
		}
	}
	
	/**
	 * <p>Causes a jump to the point x and y.</p>
	 * 
	 * @param x The x position.
	 */
	public void jump(int amount, EntityDirection direction) {
		if(direction == EntityDirection.DIRECTION_DOWN || direction == EntityDirection.DIRECTION_UP) {
			int y = tileY+amount;
			this.jumpDist = tileY-y;
			this.targetY = y;
			this.velocity.y = (tileY > y) ? -(moveSpeed*jumpDist) : (tileY < y) ? (moveSpeed*jumpDist) : 0f;
			System.out.println(velocity.y);
		} else {
			int x = tileX+amount;
			this.jumpDist = tileX-x;
			this.targetX = x;
			this.velocity.x = (tileX > x) ? -(moveSpeed*jumpDist) : (tileX < x) ? (moveSpeed*jumpDist) : 0f;
		}
	}

	/**
	 * <p>Checks whether this {@code HumanEntity} can move to x and y.</p>
	 * 
	 * @param x The x location.
	 * @param y The y location.
	 * @return Whether the {@code HumanEntity} can move.
	 */
	protected boolean canMove(int x, int y) {
		if(map == null)
			return true;
		return map.canMove(x, y);
	}

	/**
	 * <p>Moves the {@code HumanEntity} in a new direction.</p>
	 * 
	 * @param direction The direction of the entity.
	 */
	public void move(EntityDirection direction) {
		if(direction != null && moveDirection == null)
			this.lookDirection = direction;
		this.nextMoveDirection = direction;
	}

	/**
	 * <p>Sets the spritesheet of this {@code HumanEntity}.</p>
	 * 
	 * @param texture The texture of the spritesheet.
	 */
	public void setSprite(Texture texture) {
		this.moveAnims = TextureUtils.createRMAnimation(texture, animSpeed);
	}

	/**
	 * <p>Sets the speed of each animation.</p>
	 * 
	 * @param animSpeed The speed of a single frame.
	 */
	public void setAnimationSpeed(float animSpeed) {
		this.animSpeed = animSpeed;
		for(int i = 0; i < moveAnims.length; i++) moveAnims[i].setFrameDuration(animSpeed);
	}

	/**
	 * <p>Sets the map this {@code HumanEntity} is on.</p>
	 * 
	 * @param map The new map.
	 * @param x The x tile position.
	 * @param y The y tile position.
	 */
	public void setMap(TileMap map, int x, int y) {
		setMap(map, x, y, true);
	}

	/**
	 * <p>Sets the map this {@code HumanEntity} is on.</p>
	 * 
	 * @param map The new map.
	 * @param x The x tile position.
	 * @param y The y tile position.
	 * @param stop Whether to stop on the tile position and "teleport".
	 */
	public void setMap(TileMap map, int x, int y, boolean stop) {
		if(map == null)
			throw new NullPointerException("map cannot be null");
		if(this.map != null) {
			this.map.getEntityList().remove(this);
			this.map.onExit();
		}
		this.map = map;
		setPosition(x, y, stop);
		this.map.getEntityList().add(this);
		this.map.onEnter();
	}

	/**
	 * <p>Sets the position of this {@code HumanEntity}.</p>
	 * 
	 * @param x The x tile position.
	 * @param y The y tile position.
	 */
	public void setPosition(int x, int y) {
		setPosition(x, y, true);
	}

	/**
	 * <p>Sets the position of this {@code HumanEntity}.</p>
	 * 
	 * @param x The x tile position.
	 * @param y The y tile position.
	 * @param stop Whether to stop on the tile and teleport.
	 */
	public void setPosition(int x, int y, boolean stop) {
		this.tileX = this.targetX = x;
		this.tileY = this.targetY = y;
		this.position.set(this.tileX * map.getTileset().getWidth(),
				this.tileY * map.getTileset().getHeight(), 0.0f);
		if(stop) {
			this.velocity.setZero();
			this.moveDirection = this.nextMoveDirection = null;
			this.animTime = 0f;
		} else {
			if(nextMoveDirection != null) {
				switch(nextMoveDirection) {
				case DIRECTION_DOWN:
					this.targetY = tileY-1;
					break;
				case DIRECTION_LEFT:
					this.targetX = tileX-1;
					break;
				case DIRECTION_RIGHT:
					this.targetX = tileX+1;
					break;
				case DIRECTION_UP:
					this.targetY = tileY+1;
					break;
				}
			}
		}
	}

	/**
	 * <p>Sets the speed of this {@code HumanEntity}.</p>
	 * 
	 * @param moveSpeed The movespeed.
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * <p>Gets the speed this {@code HumanEntity} moves at.</p>
	 * 
	 * @return The speed this {@code HumanEntity} moves at.
	 */
	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * <p>Gets The speed of a single animation frame.</p>
	 * 
	 * @return The speed of a single animation frame.
	 */
	public float getAnimationSpeed() {
		return this.animSpeed;
	}

	/**
	 * <p>Gets the {@link TileMap} this {@code HumanEntity} is currently on.</p>
	 * 
	 * @return The {@link TileMap} this {@code HumanEntity} is on.
	 */
	public TileMap getMap() {
		return this.map;
	}

}
