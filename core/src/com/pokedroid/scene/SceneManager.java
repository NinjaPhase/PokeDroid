package com.pokedroid.scene;

import java.util.Stack;

import com.badlogic.gdx.utils.Disposable;
import com.pokedroid.PokeDroid;

/**
 * <p>The {@code SceneManager} will control a stack of {@link Scene}'s, this will be used
 * to determine which scenes will be updated and rendered.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneManager implements Disposable {

	private PokeDroid game;
	private Stack<Scene> sceneStack;

	/**
	 * <p>Constructor for a new {@code SceneManager}.</p>
	 * 
	 * @param game The game.
	 */
	public SceneManager(PokeDroid game) {
		this.game = game;
		this.sceneStack = new Stack<Scene>();
	}

	/**
	 * <p>Updates the {@link Scene} on the top of the stack.</p>
	 * 
	 * @param timeDelta The delta time.
	 */
	public void update(float timeDelta) {
		synchronized(sceneStack) {
			Scene s = getCurrentScene();
			if(s != null) s.update(timeDelta);
		}
	}

	/**
	 * <p>Renders every {@link Scene} in the stack.</p>
	 */
	public void render() {
		for(Scene s : sceneStack) {
			s.render();
		}
	}

	@Override
	public void dispose() {
		synchronized(sceneStack) {
			while(!sceneStack.isEmpty()) {
				pop();
			}
		}
	}

	/**
	 * <p>Pushes a {@link Scene} onto the top of the stack.</p>
	 * 
	 * @param s The scene to push onto the top of the stack.
	 */
	public void push(Scene s) {
		synchronized(sceneStack) {
			s.create(game);
			sceneStack.push(s);
		}
	}

	/**
	 * <p>Removes the {@link Scene} on the top of the stack.</p>
	 * 
	 * @return The {@link Scene} just removed, if empty then {@code null}.
	 */
	public Scene pop() {
		synchronized(sceneStack) {
			if(sceneStack.isEmpty())
				return null;
			Scene s = sceneStack.pop();
			s.dispose();
			return s;
		}
	}

	/**
	 * <p>Removes a {@link Scene}, regardless of stack position.</p>
	 * 
	 * @param s The {@link Scene} to remove.
	 * @return Whether the {@link Scene} was on the stack.
	 */
	public boolean remove(Scene s) {
		synchronized(sceneStack) {
			if(sceneStack.isEmpty() || !sceneStack.contains(s))
				return false;
			sceneStack.remove(s);
			s.dispose();
			return true;
		}
	}

	/**
	 * <p>Gets the {@link Scene} on the top of the stack, if the stack is
	 * empty then returns {@code null}.</p>
	 * 
	 * @return The {@link Scene} on the top of the stack, or {@code null} if empty.
	 */
	public Scene getCurrentScene() {
		synchronized(sceneStack) {
			return (sceneStack.isEmpty() ? null : sceneStack.peek());
		}
	}

}
