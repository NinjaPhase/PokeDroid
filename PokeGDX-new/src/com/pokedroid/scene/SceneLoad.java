package com.pokedroid.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.pokedroid.PokeDroid;
import com.pokedroid.pokemon.Move;
import com.pokedroid.pokemon.Species;
import com.pokedroid.pokemon.Type;

/**
 * <p>This is used as a placeholder when loading the assets.</p>
 * 
 * @author PoketronHacker
 * @version 14 November 2015
 *
 */
public class SceneLoad implements IScene, Runnable {

	private PokeDroid game;
	private JsonIterator speciesIterator;
	private boolean finished;

	@Override
	public void create(PokeDroid game) {
		this.game = game;
		this.speciesIterator = new JsonReader().parse(Gdx.files.internal("data/species.json")).iterator();
		(new Thread(this)).start();
	}

	@Override
	public void update(float timeDelta) {
		if(finished) {
			this.game.getSceneManager().push(new SceneTitleMenu());
		}
	}

	@Override
	public void render(Batch batch) {
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void focusGained() {

	}

	@Override
	public void focusLost() {

	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public void run() {
		for(JsonValue v : new JsonReader().parse(Gdx.files.internal("data/types.json"))) {
			Type.registerType(new Type(v));
		}
		for(JsonValue v : new JsonReader().parse(Gdx.files.internal("data/moves.json"))) {
			Move.registerMove(new Move(v));
		}
		while(speciesIterator.hasNext()) {
			JsonValue v = speciesIterator.next();
			Species s = new Species(v);
			Species.registerSpecies(s);
		}
		finished = true;
	}

}
