package org.pokedroid.editor.gui.main;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.pokedroid.editor.gui.MainGUI;
import org.pokedroid.editor.gui.brush.Brush;
import org.pokedroid.editor.gui.brush.FillBrush;
import org.pokedroid.editor.gui.brush.PencilBrush;

/**
 * <p>The {@code EditorToolbar} is the toolbar to display the different kind of brushes.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class EditorToolbar extends JToolBar implements ActionListener {
	private static final long serialVersionUID = -1091300499341937837L;
	private static final Brush[] BRUSH_LIST = new Brush[]{PencilBrush.PENCIL_BRUSH, FillBrush.FILL_BRUSH};
	
	private MainGUI mainGUI;
	private JComboBox<Brush> cbBrushList;
	private JButton btnChangeBrush;
	private JToggleButton btnLayerOne, btnLayerTwo, btnLayerThree;
	private ButtonGroup bgLayers;
	
	/**
	 * <p>Constructor for the {@code EditorToolbar}.</p>
	 * 
	 * @param mainGUI The {@link MainGUI}.
	 */
	public EditorToolbar(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		this.cbBrushList = new JComboBox<Brush>(BRUSH_LIST);
		this.bgLayers = new ButtonGroup();
		this.add(cbBrushList);
		this.add((btnChangeBrush = createButton("Change Brush")));
		this.add(new JSeparator());
		this.add((btnLayerOne = createGroupButton("Layer One", bgLayers, true)));
		this.add((btnLayerTwo = createGroupButton("Layer Two", bgLayers, false)));
		this.add((btnLayerThree = createGroupButton("Layer Three", bgLayers, false)));
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		this.setFloatable(false);
	}
	
	private JButton createButton(String label) {
		JButton btn = new JButton(label);
		btn.addActionListener(this);
		btn.addKeyListener(mainGUI);
		return btn;
	}
	
	private JToggleButton createGroupButton(String label, ButtonGroup group, boolean selected) {
		JToggleButton btn = new JToggleButton(label);
		btn.addActionListener(this);
		btn.addKeyListener(mainGUI);
		group.add(btn);
		if(selected)
			btn.doClick();
		return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnChangeBrush &&
				cbBrushList.getSelectedIndex() >= 0) {
			mainGUI.setBrush((Brush)cbBrushList.getSelectedItem());
		} else if(e.getSource() == btnLayerOne) {
			mainGUI.setLayer(0);
		} else if(e.getSource() == btnLayerTwo) {
			mainGUI.setLayer(1);
		} else if(e.getSource() == btnLayerThree) {
			mainGUI.setLayer(2);
		}
	}

}
