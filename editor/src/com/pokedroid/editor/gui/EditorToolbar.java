package com.pokedroid.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.pokedroid.editor.gui.brush.Brush;
import com.pokedroid.editor.gui.brush.BrushRegistry;

import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.toolbar.WebToolBar;

/**
 * <p>
 * The {@code EditorToolbar} will contain the different tools used to edit the
 * map with, and togglable options to aid the designer.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public class EditorToolbar extends WebToolBar implements ActionListener {
	private static final long serialVersionUID = -6008037659416421958L;

	private MainGUI mainGUI;
	private WebComboBox cbBrushes;
	private JToggleButton btnToggleGrid, btnToggleDim;
	private JToggleButton btnLayerOne, btnLayerTwo, btnLayerThree, btnEntity;
	private ButtonGroup btnGroupLayers;

	/**
	 * <p>
	 * Creates a new {@code EditorToolbar}.
	 * </p>
	 * 
	 * @param mainGUI
	 *            The main gui.
	 */
	public EditorToolbar(MainGUI mainGUI) {
		super();
		this.mainGUI = mainGUI;
		this.cbBrushes = new WebComboBox(BrushRegistry.BRUSHES.toArray(new Brush[BrushRegistry.BRUSHES.size()]));
		this.cbBrushes.addActionListener(this);
		this.btnGroupLayers = new ButtonGroup();
		this.add(cbBrushes);
		this.addSeparator();
		this.add(createButton(btnToggleGrid = new JToggleButton("Show Grid"),
				true));
		this.add(createButton(btnToggleDim = new JToggleButton("Dim Layers"), true));
		this.addSeparator();
		this.add(createButton(btnLayerOne = new JToggleButton("1"), true, btnGroupLayers));
		this.add(createButton(btnLayerTwo = new JToggleButton("2"), false, btnGroupLayers));
		this.add(createButton(btnLayerThree = new JToggleButton("3"), false, btnGroupLayers));
		this.add(createButton(btnEntity = new JToggleButton("E"), false, btnGroupLayers));
		this.setFloatable(false);
	}

	/**
	 * <p>
	 * Creates a button and assigns the action listener.
	 * </p>
	 * 
	 * @param btn
	 *            The button.
	 * @return The button.
	 */
	protected <T extends AbstractButton> T createButton(T btn, boolean clicked) {
		return createButton(btn, clicked, null);
	}

	/**
	 * <p>
	 * Creates a button and assigns the action listener.
	 * </p>
	 *
	 * @param btn
	 *            The button.
	 * @return The button.
	 */
	protected <T extends AbstractButton> T createButton(T btn, boolean clicked, ButtonGroup group) {
		if(group != null) group.add(btn);
		btn.addActionListener(this);
		if (clicked)
			btn.doClick();
		return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnToggleGrid) {
			mainGUI.getMapDisplay().setShowGrid(btnToggleGrid.isSelected());
		} else if (e.getSource() == btnToggleDim) {
			mainGUI.getMapDisplay().setDimOtherLayers(btnToggleDim.isSelected());
		} else if (e.getSource() == btnLayerOne) {
			mainGUI.getMapDisplay().setCurrentLayer(0);
		} else if (e.getSource() == btnLayerTwo) {
			mainGUI.getMapDisplay().setCurrentLayer(1);
		} else if (e.getSource() == btnLayerThree) {
			mainGUI.getMapDisplay().setCurrentLayer(2);
		} else if (e.getSource() == cbBrushes) {
			Brush b = (Brush) cbBrushes.getSelectedItem();
			mainGUI.getMapDisplay().setCurrentBrush(b);
		}
	}

}
