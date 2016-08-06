package com.pokedroid.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;
import com.pokedroid.editor.asset.Story;
import com.pokedroid.editor.gui.dialog.NewMapDialog;
import com.pokedroid.editor.gui.dialog.NewStoryDialog;

/**
 * <p>
 * The {@code EditorMenuBar} is the menu bar used for loading, saving and doing
 * other operations.
 * </p>
 * 
 * @author PoketronHacker
 * @version 17 October 2015
 *
 */
public class EditorMenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 5336209682415901622L;

	private MainGUI mainGUI;
	private JMenu jmFile, jmHelp;
	private JMenuItem jmiNewStory, jmiLoadStory;
	private JMenuItem jmiNewMap, jmiSaveStory, jmiPreferences, jmiQuit, jmiAbout;
	private WebFileChooser fileChooser;

	/**
	 * <p>
	 * Constructor for a new {@code EditorMenuBar}.
	 * </p>
	 * 
	 * @param mainGUI
	 *            The main gui.
	 */
	public EditorMenuBar(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		this.jmFile = new JMenu("File");
		{
			this.jmiNewStory = createItem(new JMenuItem("New Story"));
			this.jmFile.add(jmiNewStory);
			this.jmiLoadStory = createItem(new JMenuItem("Load Story"));
			this.jmFile.add(jmiLoadStory);
			this.jmFile.addSeparator();
			this.jmiNewMap = createItem(new JMenuItem("New Map"));
			this.jmFile.add(jmiNewMap);
			this.jmFile.addSeparator();
			this.jmiSaveStory = createItem(new JMenuItem("Save Story"));
			this.jmFile.add(jmiSaveStory);
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
	 * @param jmi
	 *            The JMenuItem.
	 * @return
	 */
	private JMenuItem createItem(JMenuItem jmi) {
		jmi.addActionListener(this);
		return jmi;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jmiNewStory) {
			new NewStoryDialog(mainGUI);
		} else if(e.getSource() == jmiLoadStory) {
			fileChooser = new WebFileChooser("./");
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return (f.isDirectory() || f.getName().endsWith(".zip"));
				}

				@Override
				public String getDescription() {
					return "Compressed ZIP File (*.zip)";
				}
			});
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (fileChooser.showOpenDialog(mainGUI) == WebFileChooser.APPROVE_OPTION) {
				File sf = fileChooser.getSelectedFile();
				Story s = new Story(sf);
				mainGUI.setStory(s);
			}
		} else if(e.getSource() == jmiSaveStory) {
			if(mainGUI.getStory() == null)
				return;
			mainGUI.getStory().save();
		} else if (e.getSource() == jmiNewMap) {
			new NewMapDialog(mainGUI);
		} else if (e.getSource() == jmiAbout) {
			JOptionPane.showConfirmDialog(mainGUI,
					"Mappy was created to by PoketronHacker to aid " + "the creation of maps for PokeDroid.",
					"About Us", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		} else if (e.getSource() == jmiQuit) {
			mainGUI.dispatchEvent(new WindowEvent(mainGUI, WindowEvent.WINDOW_CLOSING));
		}
	}

}
