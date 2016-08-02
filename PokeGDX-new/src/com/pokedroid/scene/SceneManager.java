package com.pokedroid.scene;

import java.util.Stack;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.pokedroid.PokeDroid;

/**
 * <p>The {@code SceneManager} will control a stack of {@link IScene}'s, this will be used
 * to determine which scenes will be updated and rendered.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneManager implements InputProcessor, Disposable {

	private PokeDroid game;
	private Stack<IScene> sceneStack;

	/**
	 * <p>Constructor for a new {@code SceneManager}.</p>
	 * 
	 * @param game The game.
	 */
	public SceneManager(PokeDroid game) {
		this.game = game;
		this.sceneStack = new Stack<IScene>();
	}

	/**
	 * <p>Updates the {@link IScene} on the top of the stack.</p>
	 * 
	 * @param deltaTime The delta time.
	 */
	public void update(float deltaTime) {
		synchronized(sceneStack) {
			IScene s = getCurrentScene();
			if(s != null) s.update(deltaTime);
		}
	}

	/**
	 * <p>Renders every {@link IScene} in the stack.</p>
	 * 
	 * @param batch The batch for {@link IScene}'s to render onto.
	 */
	public void render(Batch batch) {
		for(IScene s : sceneStack) {
			s.render(batch);
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
	 * <p>Resizes all the {@link IScene}'s.</p>
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public void resize(int width, int height) {
		synchronized(sceneStack) {
			for(IScene s : sceneStack) {
				s.resize(width, height);
			}
		}
	}

	/**
	 * <p>Pushes a {@link IScene} onto the top of the stack.</p>
	 * 
	 * @param s The scene to push onto the top of the stack.
	 */
	public void push(IScene s) {
		synchronized(sceneStack) {
			IScene cur = getCurrentScene();
			if(cur != null) {
				cur.focusLost();
			}
			s.create(game);
			s.focusGained();
			sceneStack.push(s);
		}
	}

	/**
	 * <p>Removes the {@link IScene} on the top of the stack.</p>
	 * 
	 * @return The {@link IScene} just removed, if empty then {@code null}.
	 */
	public IScene pop() {
		synchronized(sceneStack) {
			if(sceneStack.isEmpty())
				return null;
			IScene s = sceneStack.pop();
			s.focusLost();
			s.dispose();
			s = getCurrentScene();
			if(s != null) {
				s.focusGained();
			}
			return s;
		}
	}

	/**
	 * <p>Removes a {@link IScene}, regardless of stack position.</p>
	 * 
	 * @param s The {@link IScene} to remove.
	 * @return Whether the {@link IScene} was on the stack.
	 */
	public boolean remove(IScene s) {
		synchronized(sceneStack) {
			if(sceneStack.isEmpty() || !sceneStack.contains(s))
				return false;
			sceneStack.remove(s);
			s.focusLost();
			s.dispose();
			s = getCurrentScene();
			if(s != null) {
				s.focusGained();
			}
			return true;
		}
	}

	/**
	 * <p>Gets the {@link IScene} on the top of the stack, if the stack is
	 * empty then returns {@code null}.</p>
	 * 
	 * @return The {@link IScene} on the top of the stack, or {@code null} if empty.
	 */
	public IScene getCurrentScene() {
		synchronized(sceneStack) {
			return (sceneStack.isEmpty() ? null : sceneStack.peek());
		}
	}
	
	/**
	 * <p>Finds the first Scene of type.</p>
	 * 
	 * @param type The type.
	 * @return The {@link IScene}.
	 */
	@SuppressWarnings("unchecked")
	public <T extends IScene> T findScene(Class<T> type) {
		synchronized(sceneStack) {
			for(IScene s : sceneStack) {
				if(type.isInstance(s))
					return (T) s;
			}
		}
		return null;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(getCurrentScene() != null)
			return getCurrentScene().keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(getCurrentScene() != null)
			return getCurrentScene().keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
