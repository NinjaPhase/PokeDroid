package pokejava.api;

import java.util.Map;

import pokejava.api.pokemon.ExperienceType;

/**
 * The Game API for accessing an interfaced instance of the Pokemon game.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
public interface Game {
	
	/**
	 * @return The different experience types to allow for new formulas.
	 */
	public Map<String, ExperienceType> getExpTypes();
	
}
