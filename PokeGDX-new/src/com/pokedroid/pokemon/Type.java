package com.pokedroid.pokemon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;

/**
 * <p>A {@code Type} determines the strengths and weaknesses between two different
 * {@link Species}.
 * 
 * @author PoketronHacker
 * @version 13 November 2015
 *
 */
public class Type {
	
	private String name;
	private String[] strong, weak, immune;
	
	/**
	 * <p>Constructor for a new {@code Type}.</p>
	 * 
	 * @param v The json value.
	 */
	public Type(JsonValue v) {
		this(v.getString("name", v.name),
				v.get("strongAgainst").asStringArray(),
				v.get("weakAgainst").asStringArray(),
				v.get("immuneAgainst").asStringArray());
	}
	
	/**
	 * <p>Constructor for a new {@code Type}.</p>
	 * 
	 * @param name The name of the type.
	 * @param strong The strength of the type from the move perspective.
	 * @param weak The weak of the type from the move perspective.
	 * @param immune The immunity of the type from the move perspective.
	 */
	public Type(String name, String[] strong, String[] weak, String[] immune) {
		this.name = name;
		this.strong = strong;
		this.weak = weak;
		this.immune = immune;
	}
	
	/**
	 * <p>Gets the name of this {@code Type}.</p>
	 * 
	 * @return The name of this {@code Type}.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * <p>Gets the type modifier.</p>
	 * 
	 * @param The type of the {@link Species}.
	 * @return The type modifier effectiveness from the perspective
	 * of this type as a move.
	 */
	public float getEffectiveness(Type typeOne) {
		for(String s : strong) {
			if(s.equalsIgnoreCase(typeOne.getName())) return 2.0f;
		}
		for(String s : weak) {
			if(s.equalsIgnoreCase(typeOne.getName())) return 0.5f;
		}
		for(String s : immune) {
			if(s.equalsIgnoreCase(typeOne.getName())) return 0.0f;
		}
		return 1.0f;
	}
	
	/**
	 * <p>Gets the type modifier.</p>
	 * 
	 * @param typeOne The type of the {@link Species}.
	 * @param typeTwo The type of the {@link Species}.
	 * @return The type modifier effectiveness.
	 */
	public float getEffectiveness(Type typeOne, Type typeTwo) {
		if(typeOne == typeTwo)
			return getEffectiveness(typeOne);
		return getEffectiveness(typeOne) * getEffectiveness(typeTwo);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	private static final Map<String, Type> TYPES = Collections.synchronizedMap(new HashMap<String, Type>());
	
	/**
	 * <p>Registers the type.</p>
	 * 
	 * @param type The type to register.
	 */
	public static void registerType(Type type) {
		TYPES.put(type.getName().toUpperCase(), type);
	}
	
	/**
	 * <p>Unregisters the type.</p>
	 * 
	 * @param type The type of unregister.
	 */
	public static void unregisterType(Type type) {
		if(!TYPES.containsKey(type.getName()))
			return;
		TYPES.remove(type);
	}
	
	/**
	 * <p>Gets a type for the name.</p>
	 * 
	 * @param name The name of the type.
	 * @return The type if it exists, otherwise {@code null}.
	 */
	public static Type forName(String name) {
		return TYPES.get(name.toUpperCase());
	}
	
}
