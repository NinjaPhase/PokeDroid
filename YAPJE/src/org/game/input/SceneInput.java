package org.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import org.game.scene.Scene;
import org.game.scene.SceneManager;

/**
 * <p>Processes input for a scene manager.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneInput implements KeyListener {
	
	private SceneManager manager;
	
	public SceneInput(SceneManager manager) {
		this.manager = manager;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Scene s = manager.getCurrentScene();
		if(s != null) {
			try {
				s.getClass().getMethod("keyPressed", KeyEvent.class).invoke(s, e);
			} catch (NoSuchMethodException | SecurityException |
					IllegalAccessException | IllegalArgumentException |
					InvocationTargetException e1) {}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Scene s = manager.getCurrentScene();
		if(s != null) {
			try {
				s.getClass().getMethod("keyReleased", KeyEvent.class).invoke(s, e);
			} catch (NoSuchMethodException | SecurityException |
					IllegalAccessException | IllegalArgumentException |
					InvocationTargetException e1) {}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Scene s = manager.getCurrentScene();
		if(s != null) {
			try {
				s.getClass().getMethod("keyTyped", KeyEvent.class).invoke(s, e);
			} catch (NoSuchMethodException | SecurityException |
					IllegalAccessException | IllegalArgumentException |
					InvocationTargetException e1) {}
		}
	}

}
