package com.pokedroid.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.pokedroid.editor.dialog.JPreferenceDialog;
import com.pokedroid.editor.dialog.JStoryDialog;
import com.pokedroid.story.Story;

/**
 * <p>The {@code MenuBar} is the main menu bar.</p>
 * 
 * @author J. Kitchen
 *
 */
public class JMappyMenu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 769250075449089840L;

	private JMappy mappy;
	private JMenu jmFile, jmProject, jmEditors, jmPlugins, jmHelp;
	private JMenuItem jmiNew, jmiOpen, jmiClose, jmiSave, jmiSaveAs, jmiExit;
	private JMenuItem jmiNewMap, jmiSettings;
	private JMenuItem jmiGuiBuilder;
	private JMenuItem jmiPluginManager;
	private JMenuItem jmiPreferences, jmiAbout;

	/**
	 * <p>Constructs a {@code Mappy}.</p>
	 * 
	 * @param mappy The mappy.
	 */
	public JMappyMenu(JMappy mappy) {
		this.mappy = mappy;

		jmFile = new JMenu("File");
		{
			jmiNew = new JMenuItem("New Story");
			jmiNew.addActionListener(this);
			jmiOpen = new JMenuItem("Open File...");
			jmiOpen.addActionListener(this);
			jmiClose = new JMenuItem("Close");
			jmiClose.addActionListener(this);
			jmiClose.setEnabled(false);
			jmiSave = new JMenuItem("Save");
			jmiSave.addActionListener(this);
			jmiSave.setEnabled(false);
			jmiSaveAs = new JMenuItem("Save As...");
			jmiSaveAs.addActionListener(this);
			jmiSaveAs.setEnabled(false);
			jmiExit = new JMenuItem("Exit");
			jmiExit.addActionListener(this);
			jmFile.add(jmiNew);
			jmFile.add(jmiOpen);
			jmFile.addSeparator();
			jmFile.add(jmiClose);
			jmFile.addSeparator();
			jmFile.add(jmiSave);
			jmFile.add(jmiSaveAs);
			jmFile.addSeparator();
			jmFile.add(jmiExit);
			add(jmFile);
		}
		jmProject = new JMenu("Project");
		{
			jmiNewMap = new JMenuItem("New Map");
			jmiNewMap.setEnabled(false);
			jmiSettings = new JMenuItem("Project Settings");
			jmiSettings.setEnabled(false);
			jmProject.add(jmiNewMap);
			jmProject.addSeparator();
			jmProject.add(jmiSettings);
			add(jmProject);
		}
		jmEditors = new JMenu("Editors");
		{
			jmiGuiBuilder = new JMenuItem("GUI Builder");
			jmEditors.add(jmiGuiBuilder);
			add(jmEditors);
		}
		jmPlugins = new JMenu("Plugins");
		{
			jmiPluginManager = new JMenuItem("Plugin Manager");
			jmPlugins.add(jmiPluginManager);
			jmPlugins.addSeparator();
			add(jmPlugins);
		}
		jmHelp = new JMenu("Help");
		{
			jmiPreferences = new JMenuItem("Preferences");
			jmiPreferences.addActionListener(this);
			jmiAbout = new JMenuItem("About Mappy");
			jmiAbout.addActionListener(this);
			jmHelp.add(jmiPreferences);
			jmHelp.add(jmiAbout);
			add(jmHelp);
		}
	}
	
	/**
	 * <p>Refreshes the enabled and disabled status's.</p>
	 */
	public void refresh() {
		this.jmiClose.setEnabled((mappy.getOpenStory() != null));
		this.jmiSave.setEnabled((mappy.getOpenStory() != null));
		this.jmiSaveAs.setEnabled((mappy.getOpenStory() != null));
		this.jmiNewMap.setEnabled((mappy.getOpenStory() != null));
		this.jmiSettings.setEnabled((mappy.getOpenStory() != null));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jmiNew) {
			JStoryDialog storyDiag = new JStoryDialog(mappy);
		} else if(e.getSource() == jmiOpen) {
			JFileChooser fileChooser = new JFileChooser("./");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(fileChooser.showOpenDialog(mappy) == JFileChooser.APPROVE_OPTION) {
				
			}
		} else if(e.getSource() == jmiClose) {
			mappy.setOpenStory(null);
		} else if(e.getSource() == jmiExit) {
			mappy.dispatchEvent(new WindowEvent(mappy, WindowEvent.WINDOW_CLOSING));
		} else if(e.getSource() == jmiPreferences) {
			new JPreferenceDialog(mappy);
		} else if(e.getSource() == jmiAbout) {
			JOptionPane.showMessageDialog(mappy,
					"Mappy is the Pokémon story editor.\n\n" +
					"It is a tool to be used alongside Pokédroid to create unique Pokémon stories.",
					"About Mappy", JOptionPane.PLAIN_MESSAGE);
		}
	}

}
