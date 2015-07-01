package org.game.scene;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * <p>The loading scene of the game, this will be the first
 * scene that will be seen.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneLoad extends Scene {

	private Image loading;
	private boolean loadFinish;
	
	@Override
	public void init() {
		loadFinish = false;
	}

	@Override
	public void onUpdate(double timeDelta) {
		try {
			loading = ImageIO.read(getClass().getResourceAsStream("/assets/001.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(loading != null) {
			loadFinish = true;
		}
		if(loadFinish) {
			game.getSceneManager().popScene(this);
			game.getSceneManager().pushScene(new SceneMap());
		}
	}

	@Override
	public void onRender(Graphics2D g) {
		
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public void focusGained() {
		
	}

	@Override
	public void focusLost() {
		
	}

}
