package org.game.scene;

import java.awt.Graphics2D;
import java.util.Stack;

import org.game.Game;

/**
 * <p>The {@code SceneManager} is used to manage different parts of the game
 * by using a {@code Stack} that will store {@link Scene}s. These {@code Scene}s
 * will contain the functions that control the game.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @sicne v1.0
 *
 */
public class SceneManager {
	
	private Game game;
	private Stack<Scene> scenes;
	
	/**
	 * <p>Constructor for a new {@code SceneManager}.</p>
	 */
	public SceneManager(Game game) {
		this.scenes = new Stack<Scene>();
		this.game = game;
	}
	
	/**
	 * <p>Renders all the scenes within the stack.<p>
	 * 
	 * @param g The graphics to render onto.
	 */
	public void renderScenes(Graphics2D g) {
		for(Scene s : scenes)
			s.onRender(g);
	}
	
	/**
	 * <p>Pushes a game onto the scene stack.</p>
	 * @param s
	 */
	public void pushScene(Scene s) {
		s.game = game;
		s.init();
		s.focusGained();
		getStack().push(s);
	}
	
	/**
	 * <p>Pops the scene off the top of the stack.</p>
	 */
	public void pop() {
		if(getStack().isEmpty()) {
			getStack().pop();
		}
	}
	
	/**
	 * <p>Pops a scene within the stack so long as the 
	 * scene exists.</p>
	 * @param s The scene to pop off the stack.
	 */
	public void popScene(Scene s) {
		if(getStack().isEmpty() || !getStack().contains(s))
			return;
		Scene cur = getCurrentScene();
		if(cur == s) cur.focusLost();
		s.cleanup();
		getStack().remove(s);
	}
	
	/**
	 * @return A Thread-Safe method for the stack.
	 */
	protected synchronized Stack<Scene> getStack() {
		return this.scenes;
	}
	
	/**
	 * @return The Scene on top of the stack.
	 */
	public Scene getCurrentScene() {
		return (getStack().isEmpty() ? null : getStack().peek());
	}
	
}
