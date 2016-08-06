package com.pokedroid.editor.gui;

import com.pokedroid.editor.gui.dialog.EditMapDialog;
import com.pokedroid.editor.map.TileMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * <p>The MapList is used to display all the maps
 * within the project.</p>
 *
 * @author NinjaPhase
 * @version 05 August 2016
 */
public class MapList extends JPanel implements ActionListener, MouseListener {

    private MainGUI mainGUI;
    private JTree mapTree;
    private JPopupMenu tilemapPopMenu;
    private JMenuItem jmiMapProperties;
    private TileMap selectedTileMap;

    /**
     * <p>Constructs a new MapList.</p>
     *
     * @param mainGUI The maingui.
     */
    public MapList(MainGUI mainGUI) {
        this.setLayout(new BorderLayout());
        this.mainGUI = mainGUI;
        this.mapTree = new JTree();
        buildTree();
        this.add(mapTree);
        mapTree.addMouseListener(this);
        this.tilemapPopMenu = new JPopupMenu();
        {
            this.jmiMapProperties = new JMenuItem("Map Properties");
            this.jmiMapProperties.addActionListener(this);
            this.tilemapPopMenu.add(jmiMapProperties);
        }
    }

    /**
     * <p>Builds the map list tree.</p>
     */
    public void buildTree() {
        if(mainGUI.getStory() == null) {
            setEnabled(false);
            mapTree.setModel(null);
            return;
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(mainGUI.getStory().getName());
        for(TileMap m : mainGUI.getStory().getMaps().values()) {
            root.add(new DefaultMutableTreeNode(m));
        }
        DefaultTreeModel dtm = new DefaultTreeModel(root);
        mapTree.setModel(dtm);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
            TreePath p = mapTree.getSelectionPath();
            if(p != null) {
                Object o = ((DefaultMutableTreeNode) p.getLastPathComponent()).getUserObject();
                if(o instanceof TileMap) {
                    mainGUI.setTileMap((TileMap) o);
                }
            }
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            TreePath p = mapTree.getSelectionPath();
            if(p != null) {
                Object o = ((DefaultMutableTreeNode) p.getLastPathComponent()).getUserObject();
                if(o instanceof TileMap) {
                    this.selectedTileMap = (TileMap) o;
                    this.tilemapPopMenu.show(this.mapTree, e.getX(), e.getY());
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jmiMapProperties && selectedTileMap != null) {
            new EditMapDialog(mainGUI, selectedTileMap);
        }
    }

}
