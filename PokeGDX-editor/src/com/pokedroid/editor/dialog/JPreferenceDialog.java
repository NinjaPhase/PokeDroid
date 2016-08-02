package com.pokedroid.editor.dialog;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * <p>The {@code JPreferences} is the preferences editor.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class JPreferenceDialog extends JDialog {
	private static final long serialVersionUID = -3466862491216018150L;
	
	/**
	 * <p>Constructs a new {@code JPreferences}.</p>
	 * 
	 * @param frame The frame.
	 */
	public JPreferenceDialog(Frame frame) {
		super(frame, true);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(createPanel(new JLabel("Event Editor: "), new JTextField(25)));
		getContentPane().add(new JLabel("Tests"));
		getContentPane().add(new JLabel("Test 2"));
		setTitle("Mappy - Preferences");
		setSize(480, 360);
		setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	protected JPanel createPanel(Component... component) {
		JPanel pnl = new JPanel();
		for(Component c : component)
			pnl.add(c);
		return pnl;
	}
	
}
