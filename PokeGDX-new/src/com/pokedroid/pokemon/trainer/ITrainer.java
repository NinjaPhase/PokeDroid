package com.pokedroid.pokemon.trainer;

/**
 * <p>The {@code ITrainer} is the interface for a trainer, this is
 * what a trainer should include.</p>
 * 
 * @author PoketronHacker
 * @vresion 18 November 2015
 *
 */
public interface ITrainer {
	
	/**
	 * <p>Gets the name of this {@code ITrainer}.</p>
	 * 
	 * @return The name of this {@code ITrainer}.
	 */
	public String getName();
	
	/**
	 * <p>Gets the trainer id of the {@code ITrainer}.</p>
	 * 
	 * @return The trainer id of the {@code ITrainer}.
	 */
	public int getTrainerId();
	
	/**
	 * <p>Gets the secret id of the {@code ITrainer}.</p>
	 * 
	 * @return The secret id of the {@code ITrainer}.
	 */
	public int getSecretId();
	
	/**
	 * <p>Gets the party of the {@code ITrainer} for battling.</p>
	 * 
	 * @return The party of the {@code ITrainer} for battling.
	 */
	public IParty getParty();
	
}
