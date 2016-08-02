package com.pokedroid.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.pokedroid.map.TileMap;
import com.pokedroid.story.Story;

/**
 * <p>The {@code JMapTree} is a tree structure of different
 * maps, this is more for organisation than performance and
 * is only logged within the editor config.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class JMapTree extends JTree implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -8807921980870069694L;

	private JMappy mappy;
	private JPopupMenu storyMenu, mapMenu;
	private boolean dragging;
	private TreePath dragTree;
	private int mX, mY;

	/**
	 * <p>Constructs a new {@code JMapTree}.</p>
	 * 
	 * @param mappy The mappy parent.
	 */
	public JMapTree(JMappy mappy) {
		this.mappy = mappy;
		this.storyMenu = createStoryMenu();
		this.mapMenu = createMapMenu();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setCellRenderer(new JMapTreeCellRenderer());
		refresh();
	}

	/**
	 * <p>Creates the {@code JPopupMenu} to
	 * use when maps are selected.</p>
	 * 
	 * @return The JPopupMenu.
	 */
	protected JPopupMenu createStoryMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new JMenuItem("New Map"));
		menu.addSeparator();
		menu.add(new JMenuItem("Properties"));
		return menu;
	}

	/**
	 * <p>Creates the {@code JPopupMenu} to
	 * use when maps are selected.</p>
	 * 
	 * @return The JPopupMenu.
	 */
	protected JPopupMenu createMapMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new JMenuItem("New Map"));
		menu.add(new JMenuItem("Delete Map"));
		menu.addSeparator();
		menu.add(new JMenuItem("Properties"));
		return menu;
	}

	/**
	 * <p>Refreshes the JMapTree.</p>
	 */
	public void refresh() {
		if(mappy.getOpenStory() == null) {
			setBackground(Color.GRAY);
			setModel(null);
		} else {
			setBackground(Color.WHITE);
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(mappy.getOpenStory(), true);
			for(TileMap map : mappy.getOpenStory().getMapList()) {
				if(map.getParent() == null) {
					addNode(root, map);
				}
			}
			setModel(new DefaultTreeModel(root));
		}
		this.setCellRenderer(new JMapTreeCellRenderer());
		repaint();
	}
	
	/**
	 * <p>Adds a tilemap node.</p>
	 * 
	 * @param root The root.
	 * @param m The map node.
	 */
	protected void addNode(DefaultMutableTreeNode root, TileMap m) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(m);
		for(TileMap child : m.getChildren()) {
			addNode(newNode, child);
		}
		root.add(newNode);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dragging
		if(dragTree != null && dragging) {
			g.setFont(getFont());
			FontMetrics a = g.getFontMetrics();
			g.setColor(new Color(51, 153, 255));
			g.fillRect(mX, mY+4-a.getHeight(), a.stringWidth(dragTree.getLastPathComponent().toString())+4, a.getHeight());
			g.setColor(Color.WHITE);
			g.drawString(dragTree.getLastPathComponent().toString(), mX+2, mY+2);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(e.getClickCount() >= 2) {
				TreePath path = getPathForLocation(e.getX(), e.getY());
				if(path != null && path.getLastPathComponent() instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
					if(node.getUserObject() instanceof TileMap) {
						mappy.setOpenMap((TileMap)node.getUserObject());
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			dragTree = getPathForLocation(e.getX(), e.getY());
			if(dragTree != null && dragTree.getPathCount() == 1)
				dragTree = null;
		}
		if(e.getButton() == MouseEvent.BUTTON3) {
			TreePath path = getPathForLocation(e.getX(), e.getY());
			setSelectionPath(path);
			if(path != null) {
				if(path.getLastPathComponent() instanceof DefaultMutableTreeNode) {
					Object obj = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
					if(obj instanceof Story) {
						this.setComponentPopupMenu(storyMenu);
					} else if(obj instanceof TileMap) {
						this.setComponentPopupMenu(mapMenu);
					}
				}
			} else {
				this.setComponentPopupMenu(null);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragTree != null && dragging) {
			TreePath end = getPathForLocation(e.getX(), e.getY());
			if(end != null && end != dragTree) {
				if(dragTree.getLastPathComponent() instanceof DefaultMutableTreeNode
						&& end.getLastPathComponent() instanceof DefaultMutableTreeNode) {
					Object startOb = ((DefaultMutableTreeNode)dragTree.getLastPathComponent()).getUserObject();
					Object endOb = ((DefaultMutableTreeNode)end.getLastPathComponent()).getUserObject();
					if(startOb == endOb)
						return;
					if(startOb instanceof TileMap) {
						if(endOb instanceof Story) {
							((TileMap) startOb).setParent(null);
						} else if(endOb instanceof TileMap) {
							((TileMap) endOb).addChild((TileMap) startOb);
						}
					}
					refresh();
				}
			}
		}
		dragTree = null;
		dragging = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragTree != null) {
			mX = e.getX();
			mY = e.getY();
			dragging = true;
			TreePath path = getPathForLocation(e.getX(), e.getY());
			setSelectionPath(path);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * <p>Constructs a new {@code JMapTreeCellRenderer}.</p>
	 * 
	 * @author J. Kitchen
	 * @version 16 March 2016
	 *
	 */
	public static class JMapTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -4629564184384991052L;
		private static final ImageIcon PROJECT_IMAGE = new ImageIcon(JMapTreeCellRenderer.class.getResource("project_icon.png"));

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
					hasFocus);
			if(value instanceof DefaultMutableTreeNode) {
				Object obj = ((DefaultMutableTreeNode) value).getUserObject();
				if(obj instanceof Story) {
					setIcon(PROJECT_IMAGE);
				}
			}
			return this;
		}

	}

}
