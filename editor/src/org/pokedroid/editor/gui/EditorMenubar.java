package org.pokedroid.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.pokedroid.editor.gui.dialog.NewMapDialog;

import com.alee.laf.WebLookAndFeel;

/**
 * <p>
 * The {@code EditorMenubar} is the menu bar used for loading, saving and doing
 * other operations.
 * </p>
 * 
 * @author PoketronHacker
 * @version 17 October 2015
 *
 */
public class EditorMenubar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 5336209682415901622L;

	private MainGUI mainGUI;
	private JMenu jmFile, jmHelp;
	private JMenuItem jmiNew, jmiImport, jmiSave, jmiPreferences, jmiQuit, jmiAbout;

	/**
	 * <p>
	 * Constructor for a new {@code EditorMenubar}.
	 * </p>
	 * 
	 * @param mainGUI
	 *            The main gui.
	 */
	public EditorMenubar(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		this.jmFile = new JMenu("File");
		{
			this.jmiNew = createItem(new JMenuItem("New Map"));
			this.jmFile.add(jmiNew);
			this.jmiImport = createItem(new JMenuItem("Import Map"));
			this.jmFile.add(jmiImport);
			this.jmFile.addSeparator();
			this.jmiSave = createItem(new JMenuItem("Save Map"));
			this.jmFile.add(jmiSave);
			this.jmFile.addSeparator();
			this.jmiPreferences = createItem(new JMenuItem("Preferences"));
			this.jmFile.add(jmiPreferences);
			this.jmFile.addSeparator();
			this.jmiQuit = createItem(new JMenuItem("Quit"));
			this.jmFile.add(jmiQuit);
			this.add(jmFile);
		}
		this.jmHelp = new JMenu("Help");
		{
			this.jmiAbout = createItem(new JMenuItem("About"));
			this.jmHelp.add(jmiAbout);
			this.add(jmHelp);
		}
	}

	/**
	 * <p>
	 * Creates a {@code JMenuItem} and sets up the action listener.
	 * </p>
	 * 
	 * @param label
	 *            The label to give the item.
	 * @return
	 */
	private JMenuItem createItem(JMenuItem jmi) {
		jmi.addActionListener(this);
		return jmi;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jmiNew) {
			boolean b = WebLookAndFeel.isDecorateDialogs();
			WebLookAndFeel.setDecorateDialogs(true);
			new NewMapDialog(mainGUI);
			WebLookAndFeel.setDecorateDialogs(b);
		} else if (e.getSource() == jmiAbout) {
			JOptionPane.showConfirmDialog(mainGUI,
					"Mappy was created to by PoketronHacker to aid " + "the creation of maps for PokeDroid.",
					"About Us", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		} else if (e.getSource() == jmiQuit) {
			mainGUI.dispatchEvent(new WindowEvent(mainGUI, WindowEvent.WINDOW_CLOSING));
		}
	}

}
