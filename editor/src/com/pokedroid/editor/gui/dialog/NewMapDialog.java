package com.pokedroid.editor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SpinnerNumberModel;

import com.pokedroid.editor.gui.MainGUI;
import com.pokedroid.editor.map.TileMap;
import com.pokedroid.editor.map.TileSet;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.text.WebTextField;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebInnerNotification;

/**
 * <p>
 * The {@code NewMapDialog} allows for editing properties of a new map, these
 * are initial properties and should not be loaded from anywhere.
 * </p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class NewMapDialog extends WebDialog implements ActionListener {
	private static final long serialVersionUID = 3280877356489894043L;

	private MainGUI mainGUI;
	private WebPanel pnlGeneral, pnlButtons;
	private WebTextField txtName;
	private WebSpinner spWidth, spHeight;
	private WebButton btnCreate, btnCancel;
	private WebComboBox cbTilesets;

	/**
	 * <p>
	 * Constructor for a new {@code NewMapDialog}.
	 * </p>
	 * 
	 * @param mainGUI
	 *            The main gui.
	 */
	public NewMapDialog(MainGUI mainGUI) {
		super(mainGUI, "New Map", true);
		this.mainGUI = mainGUI;
		getContentPane().setLayout(new BorderLayout());
		pnlGeneral = new WebPanel(new GridLayout(3, 2));
		{
			txtName = new WebTextField(15);
			txtName.setInputPrompt("Enter map name...");
			pnlGeneral.add(new WebLabel("Map Name: "), "0, 0");
			pnlGeneral.add(txtName, "1, 0");
			cbTilesets = new WebComboBox(mainGUI.getStory().getTileSets().values().toArray());

			pnlGeneral.add(new WebLabel("TileSet: "), "0,1");
			pnlGeneral.add(cbTilesets, "1,1");
			spWidth = new WebSpinner(new SpinnerNumberModel(20, 1, 200, 1));
			spHeight = new WebSpinner(new SpinnerNumberModel(15, 1, 200, 1));
			pnlGeneral.add(new WebLabel("Dimensions: "), "0,2");
			pnlGeneral.add(new WebPanel(new GridLayout(1, 2), spWidth, spHeight), "1,2");
		}
		getContentPane().add(new WebPanel(new FlowLayout(FlowLayout.LEFT), pnlGeneral));
		pnlButtons = new WebPanel();
		{
			btnCreate = new WebButton("Create");
			btnCreate.addActionListener(this);
			btnCancel = new WebButton("Cancel");
			btnCancel.addActionListener(this);
			pnlButtons.add(new GroupPanel(btnCreate, btnCancel));
		}
		getContentPane().add(new WebPanel(new FlowLayout(FlowLayout.RIGHT), pnlButtons), "South");
		pack();
		setLocationRelativeTo(mainGUI);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setResizable(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCreate) {
			if (txtName.getText().length() <= 0) {
				final WebInnerNotification mapNote = new WebInnerNotification();
				mapNote.setContent("Map name cannot be empty!");
				mapNote.setDisplayTime(1000);
				mapNote.setIcon(NotificationIcon.warning);
				NotificationManager.showInnerNotification(mainGUI, mapNote);
				return;
			} else if(mainGUI.getStory().getMaps().containsKey(txtName.getText())) {
				final WebInnerNotification mapNote = new WebInnerNotification();
				mapNote.setContent("A map with that name already exists.");
				mapNote.setDisplayTime(1000);
				mapNote.setIcon(NotificationIcon.warning);
				NotificationManager.showInnerNotification(mainGUI, mapNote);
				return;
			}
			TileMap map = new TileMap(txtName.getText(),
					(TileSet) cbTilesets.getSelectedItem(), (Integer) spWidth.getValue(),
					(Integer) spHeight.getValue());
			mainGUI.setTileMap(map);
			mainGUI.getStory().getMaps().put(map.getName(), map);
			final WebInnerNotification mapNote = new WebInnerNotification();
			mapNote.setContent("Successfully created " + txtName.getText() + "!");
			mapNote.setDisplayTime(3000);
			mapNote.setIcon(NotificationIcon.information);
			NotificationManager.showInnerNotification(mainGUI, mapNote);
			mainGUI.getMapList().buildTree();
			this.dispose();
		} else if (e.getSource() == btnCancel) {
			this.dispose();
		}
	}

}
