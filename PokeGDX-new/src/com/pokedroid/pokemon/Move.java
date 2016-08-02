package com.pokedroid.pokemon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;

/**
 * <p>A {@code Move} is an in-game battle technique that can be
 * used by a pokémon.</p>
 * 
 * <p>This class also contains a move registry.</p>
 * 
 * @author J. Kitchen
 * @version 08 May 2016
 *
 */
public class Move {

	private int id;
	private String name, description, category;
	private Type type;
	private int baseDamage, accuracy, totalPP,
		target, priority, functionCode;
	private float effectChance;
	private String flags, contestType;
	
	/**
	 * <p>Constructs a new {@code Move}.</p>
	 * 
	 * @param name The name of the move.
	 * @param functionCode The function code.
	 * @param baseDamage The base damage.
	 * @param type The type.
	 * @param category The category.
	 * @param accuracy The accuracy.
	 * @param totalPP The total PP.
	 * @param additionalChance The additional chance.
	 * @param target The target.
	 * @param priority The priority.
	 * @param flags The flags.
	 * @param contestType The contest type.
	 * @param description The description.
	 */
	public Move(String name, int functionCode, int baseDamage, Type type,
			String category, int accuracy, int totalPP, float additionalChance, int target,
			int priority, String flags, String contestType, String description) {
		this.name = name;
		this.type = type;
		this.functionCode = functionCode;
		this.baseDamage = baseDamage;
		this.category = category;
		this.accuracy = accuracy;
		this.totalPP = totalPP;
		this.effectChance = additionalChance;
		this.target = target;
		this.priority = priority;
		this.target = target;
		this.contestType = contestType;
		this.description = description;
	}
	
	/**
	 * <p>Constructs a move from the json.</p>
	 * 
	 * @param v The json value.
	 */
	public Move(JsonValue v) {
		this(v.getString("name"), v.getInt("function"), v.getInt("base_damage"),
				Type.forName(v.getString("type")), v.getString("category"), v.getInt("accuracy"),
				v.getInt("pp"), ((float)v.getInt("effect_chance")/100.0f), v.getInt("target"),
				v.getInt("priority"), v.getString("flags"), v.getString("contest_type"),
				v.getString("description"));
	}
	
	/**
	 * @return The id of the move.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The name of the move.
	 */
	public String getName() {
		return name.toUpperCase();
	}
	
	/**
	 * @return The move description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return Gets the moves category.
	 */
	public String getCategory() {
		return this.category;
	}
	
	/**
	 * <p>Gets the type.</p>
	 * 
	 * @return The type.
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * @return The base damage.
	 */
	public int getBasePower() {
		return this.baseDamage;
	}
	
	/**
	 * @return The moves accuracy.
	 */
	public int getAccuracy() {
		return this.accuracy;
	}

	/**
	 * @return The moves total pp.
	 */
	public int getTotalPP() {
		return totalPP;
	}
	
	/**
	 * @return Who this move targets.
	 */
	public int getTarget() {
		return this.target;
	}
	
	/**
	 * @return The moves priority for order.
	 */
	public int getPriority() {
		return this.priority;
	}
	
	/**
	 * @return The function code.
	 */
	public int getFunctionCode() {
		return functionCode;
	}
	
	/**
	 * @return The percentage between 0-1 an effect will
	 * 			occur.
	 */
	public float getEffectChance() {
		return this.effectChance;
	}
	
	/**
	 * <p>Gets a set of flags.</p>
	 * 
	 * @return A set of flags.
	 */
	public String getFlags() {
		return this.flags;
	}
	
	/**
	 * @return The moves contest type.
	 */
	public String getContestType() {
		return this.contestType;
	}

	/**
	 * <p>Gets the name of the move.</p>
	 */
	public String toString() {
		return getName();
	}
	
	private static final Map<String, Move> MOVES = Collections.synchronizedMap(new HashMap<String, Move>());
	
	/**
	 * <p>Registers the move.</p>
	 * 
	 * @param type The move to register.
	 */
	public static void registerMove(Move type) {
		MOVES.put(type.getName().toUpperCase(), type);
	}
	
	/**
	 * <p>Unregisters the move.</p>
	 * 
	 * @param type The move of unregister.
	 */
	public static void unregisterMove(Move type) {
		if(!MOVES.containsKey(type.getName()))
			return;
		MOVES.remove(type);
	}
	
	/**
	 * <p>Searches for the move with a given id.</p>
	 * 
	 * @param id The move id.
	 * @return The move at that id.
	 */
	public static Move forId(int id) {
		for(Move m : MOVES.values()) {
			if(m.getId() == id)
				return m;
		}
		return null;
	}
	
	/**
	 * <p>Gets a move for the name.</p>
	 * 
	 * @param name The name of the move.
	 * @return The move if it exists, otherwise {@code null}.
	 */
	public static Move forName(String name) {
		return MOVES.get(name.toUpperCase());
	}
	
}