package org.pokedroid.editor.gui.main;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.pokedroid.editor.asset.AssetFolder;
import org.pokedroid.editor.gui.MainGUI;
import org.pokedroid.editor.map.TileMap;

/**
 * <p>The {@code MapTree} is used to navigate through the different maps available
 * in the editor.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class MapTree extends JPanel implements MouseListener {
	private static final long serialVersionUID = 5815365233527511995L;
	
	private MainGUI mainGUI;
	private AssetFolder assetFolder;
	private JList<TileMap> maps;
	private DefaultListModel<TileMap> listModel;
	
	/**
	 * <p>Constructor for a new {@code MapTree}.</p>
	 */
	public MapTree(MainGUI gui) {
		this.mainGUI = gui;
		this.assetFolder = this.mainGUI.getAssetFolder();
		this.listModel = new DefaultListModel<TileMap>();
		refreshList();
		this.setLayout(new BorderLayout());
		this.maps = new JList<TileMap>(listModel);
		this.add(maps);
		maps.addMouseListener(this);
	}
	
	/**
	 * <p>Refreshes the map list only.</p>
	 */
	public void refreshList() {
		listModel.clear();
		for(TileMap t : assetFolder.getMaps())
			listModel.addElement(t);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getClickCount() < 2)
			return;
		if(maps.getSelectedIndex() >= 0) {
			mainGUI.setMap(maps.getSelectedValue());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
