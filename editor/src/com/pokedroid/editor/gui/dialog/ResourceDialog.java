package com.pokedroid.editor.gui.dialog;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.pokedroid.editor.asset.Story;
import com.pokedroid.editor.gui.MainGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * <p>The {@code ResourceDialog} is used to manage the ingame
 * resources.</p>
 *
 * @author NinjaPhase
 * @version 06 August 2016
 */
public class ResourceDialog extends WebDialog implements ActionListener {

    private MainGUI mainGUI;
    private WebPanel pnlHeader, pnlGeneral, pnlButtons;
    private WebList lsResources;
    private WebButton btnAddResource, btnRemoveResource, btnOk;
    private WebFileChooser fileChooser;
    private DefaultListModel<String> lm;

    /**
     * <p>Constructs a new {@link ResourceDialog}</p>
     *
     * @param mainGUI The maingui.
     */
    public ResourceDialog(MainGUI mainGUI) {
        super(mainGUI, "Story Resources", true);
        this.mainGUI = mainGUI;
        this.fileChooser = new WebFileChooser();
        getContentPane().setLayout(new BorderLayout());
        pnlHeader = new WebPanel(new GridLayout(1, 2));
        {
            btnAddResource = new WebButton("Add Resource");
            btnAddResource.addActionListener(this);
            btnRemoveResource = new WebButton("Remove Resource");
            btnRemoveResource.addActionListener(this);
            pnlHeader.add(btnAddResource);
            pnlHeader.add(btnRemoveResource);
        }
        getContentPane().add(new WebPanel(new BorderLayout(), pnlHeader), "North");
        pnlGeneral = new WebPanel(new BorderLayout());
        {
            lsResources = new WebList();
            loadResources();
            pnlGeneral.add(lsResources);
        }
        getContentPane().add(pnlGeneral);
        pnlButtons = new WebPanel();
        {
            btnOk = new WebButton("OK");
            btnOk.addActionListener(this);
            pnlButtons.add(new GroupPanel(btnOk));
        }
        getContentPane().add(new WebPanel(new FlowLayout(FlowLayout.RIGHT), pnlButtons), "South");
        this.setSize(480, 640);
        this.setLocationRelativeTo(mainGUI);
        this.setVisible(true);
    }

    /**
     * <p>Loads the resources from the story.</p>
     */
    private void loadResources() {
        Story s = mainGUI.getStory();
        lm = new DefaultListModel<>();
        for(String key : s.getImages().keySet()) {
            lm.addElement("img: " + key);
        }
        lsResources.setModel(lm);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Story s = mainGUI.getStory();
        if(e.getSource() == btnAddResource) {
            this.fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if(f.isDirectory() || f.getName().endsWith(".png"))
                        return true;
                    return false;
                }

                @Override
                public String getDescription() {
                    return "Resources (*.png)";
                }
            });
            if(this.fileChooser.showOpenDialog(mainGUI) == WebFileChooser.APPROVE_OPTION) {
                File f = this.fileChooser.getSelectedFile();
                try {
                    s.getImages().put(f.getName(), ImageIO.read(f));
                    lm.addElement("img: " + f.getName());
                } catch (IOException exc) {

                }
            }
        } else if(e.getSource() == btnRemoveResource) {
            int selectedIndex = lsResources.getSelectedIndex();
            if(selectedIndex >= 0) {
                if(JOptionPane.showConfirmDialog(this,
                        "Are you sure you wish to remove this resource?",
                        "Are you sure?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    String selected = lsResources.getValueAt(selectedIndex);
                    if(selected.startsWith("img: ")) {
                        String image = selected.replace("img: ", "");
                        s.getImages().remove(image);
                        lm.removeElementAt(selectedIndex);
                    }
                }
            }
        } else if(e.getSource() == btnOk) {
            this.dispose();
        }
    }
}
