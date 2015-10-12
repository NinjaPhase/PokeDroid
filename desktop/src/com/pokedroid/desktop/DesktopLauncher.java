package com.pokedroid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pokedroid.PokeDroid;

/**
 * <p>This is the entry-point for a desktop application, this uses LibGDX's LWJGL wrapper.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class DesktopLauncher {
	private static final String DESKTOP_TITLE = "PokeDroid - A Java Pokemon Game";
	private static final float ASPECT_WIDESCREEN = 9f/16f;
	
	/**
	 * <p>The entry point.</p>
	 * 
	 * @param args The application arguments.
	 */
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = DESKTOP_TITLE;
		float f = LwjglApplicationConfiguration.getDesktopDisplayMode().width / PokeDroid.VIRTUAL_WIDTH;
		config.width = PokeDroid.VIRTUAL_WIDTH;
		config.height = (int)(PokeDroid.VIRTUAL_WIDTH * ASPECT_WIDESCREEN);
		new LwjglApplication(new PokeDroid(), config);
	}
}
