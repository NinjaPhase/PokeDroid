package org.game.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import org.game.GameConfig;
import org.game.entity.Entity;
import org.game.math.Camera;
import org.game.math.CollisionBox;
import org.game.math.Vector2;

/**
 * <p>The map of this game.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneMap extends Scene implements KeyListener {
	
	private Camera camera;
	private Entity player;
	private CollisionBox box;

	@Override
	public void init() {
		camera = new Camera();
		player = new Entity(0,0) {
		};
		camera.position.x = player.getPosition().x+16f-((float)game.getWidth()/2f);
		camera.position.y = player.getPosition().y+16f-((float)game.getHeight()/2f);
		box = new CollisionBox(new Vector2(32.0, 0.0), 31.0, 31.0);
	}

	@Override
	public void onUpdate(double timeDelta) {
		player.update(timeDelta);
		camera.position.x = player.getPosition().x+16f-((float)game.getWidth()/2f);
		camera.position.y = player.getPosition().y+16f-((float)game.getHeight()/2f);
	}

	@Override
	public void onRender(Graphics2D g) {
		g.setTransform(camera.getTransform());
		g.setColor(Color.WHITE);
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				g.drawRect(x*GameConfig.TILE_WIDTH, y*GameConfig.TILE_HEIGHT, 32, 32);
			}
		}
		player.render(g);
		box.render(g);
		g.setTransform(new AffineTransform());
		g.drawString("hello", 32.0f, 32.0f);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.getVelocity().y = -2.0f;
			break;
		case KeyEvent.VK_S:
			player.getVelocity().y = 2.0f;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.getVelocity().y = 0.0f;
			break;
		case KeyEvent.VK_S:
			player.getVelocity().y = 0.0f;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
