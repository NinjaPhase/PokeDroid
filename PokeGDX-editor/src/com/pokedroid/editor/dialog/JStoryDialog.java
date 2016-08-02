package com.pokedroid.editor.dialog;

import javax.swing.JDialog;

import com.pokedroid.editor.JMappy;
import com.pokedroid.story.Story;

/**
 * <p>The {@code JStoryEditor} appears when the player creates a
 * new story.</p>
 * 
 * @author J. Kitchen
 * @version 11 June 2016
 *
 */
public class JStoryDialog extends JDialog {
	private static final long serialVersionUID = -8620610183742011121L;
	
	private Story story;
	
	/**
	 * <p>Constructs a new {@code JStoryDialog}.</p>
	 * 
	 * @param m The mappy.
	 */
	public JStoryDialog(JMappy m) {
		super(m, true);
		this.setTitle("Mappy - New Story");
		this.setSize(640, 480);
		this.setLocationRelativeTo(m);
		this.setVisible(true);
	}
	
	/**
	 * @return The story if constructed.
	 */
	public Story getStory() {
		return this.story;
	}
	
}
