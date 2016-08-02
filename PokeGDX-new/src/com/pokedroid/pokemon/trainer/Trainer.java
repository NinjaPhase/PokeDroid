package com.pokedroid.pokemon.trainer;

import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.pokemon.Pokemon;

/**
 * <p>A {@code Trainer} is a class for the {@link ITrainer}, it can
 * be used to generate a random trainer and secret id.</p>
 * 
 * @author PoketronHacker
 * @version 18 November 2015
 *
 */
public class Trainer implements ITrainer {
	private static final int MAX_ID = 65535;
	
	private Party party;
	private String name;
	private int trainerId;
	private int secretId;
	
	/**
	 * <p>Constructs a {@code Trainer} with a random trainer id and secret id.</p>
	 */
	public Trainer(String name) {
		this(name, (int)(Math.random()*MAX_ID), (int)(Math.random()*MAX_ID));
	}
	
	/**
	 * <p>Constructs a {@code Trainer} with a trainer id and secret id.</p>
	 * 
	 * @param trainerId The id of the trainer.
	 * @param secretId The secret id of the trainer.
	 */
	public Trainer(String name, int trainerId, int secretId) {
		this.name = name;
		this.party = new Party(6);
		this.trainerId = trainerId;
		this.secretId = secretId;
	}
	
	/**
	 * <p>Constructs a {@code Trainer} from a json file.</p>
	 * 
	 * @param v The trainer data.
	 */
	public Trainer(JsonValue v) {
		this(v.getString("name"),
				v.getInt("trainer_id", (int)(Math.random()*MAX_ID)),
				v.getInt("secret_id", (int)(Math.random()*MAX_ID)));
		for(JsonValue pokemon : v.get("party").iterator()) {
			party.addPokemon(new Pokemon(pokemon));
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getTrainerId() {
		return this.trainerId;
	}

	@Override
	public int getSecretId() {
		return this.secretId;
	}

	@Override
	public IParty getParty() {
		return party;
	}
	
}
