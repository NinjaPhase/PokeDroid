package org.pokedroid.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.pokedroid.editor.gui.brush.Brush;
import org.pokedroid.editor.gui.brush.BrushRegistry;

import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.toolbar.ToolbarStyle;
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
		this.add(cbBrushes);
		this.addSeparator();
		this.add(createButton(btnToggleGrid = new JToggleButton(new ImageIcon(getClass().getResource("grid_icon.png"))),
				true));
		this.add(createButton(btnToggleDim = new JToggleButton("Dim Layers"), true));
		this.setToolbarStyle(ToolbarStyle.attached);
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
		}
	}

}
