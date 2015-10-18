package org.pokedroid.editor.gui.brush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * The {@code BrushRegistry} is a collection of brushes.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public class BrushRegistry {

	public static final List<Brush> BRUSHES = Collections.synchronizedList(new ArrayList<Brush>());

	private BrushRegistry() {
	}

}
