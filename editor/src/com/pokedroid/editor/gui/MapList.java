package com.pokedroid.editor.gui;

import com.pokedroid.editor.map.TileMap;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * <p>The MapList is used to display all the maps
 * within the project.</p>
 *
 * @author NinjaPhase
 * @version 05 August 2016
 */
public class MapList extends JPanel implements MouseListener {

    private MainGUI mainGUI;
    private JTree mapTree;

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
        if(e.getClickCount() >= 2) {
            TreePath p = mapTree.getClosestPathForLocation(e.getX(), e.getY());
            if(p != null) {
                Object o = ((DefaultMutableTreeNode) p.getLastPathComponent()).getUserObject();
                if(o instanceof TileMap) {
                    mainGUI.setTileMap((TileMap) o);
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
}
