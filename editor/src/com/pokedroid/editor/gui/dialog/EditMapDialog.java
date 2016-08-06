package com.pokedroid.editor.gui.dialog;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.text.WebTextField;
import com.pokedroid.editor.gui.MainGUI;
import com.pokedroid.editor.map.TileMap;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SpinnerNumberModel;

/**
 * <p>The {@code EditMapDialog} is used to edit a map.</p>
 *
 * @author NinjaPhase
 * @version v1.0
 */
public class EditMapDialog extends WebDialog implements ActionListener {

    private MainGUI mainGUI;
    private WebPanel pnlGeneral, pnlButtons;
    private WebTextField txtName;
    private WebSpinner spWidth, spHeight;
    private WebButton btnOk, btnCancel;
    private WebComboBox cbTilesets;

    /**
     * <p>
     * Constructor for a new {@code EditMapDialog}.
     * </p>
     *
     * @param mainGUI
     *            The main gui.
     */
    public EditMapDialog(MainGUI mainGUI, TileMap map) {
        super(mainGUI, "Edit Map Properties", true);
        this.mainGUI = mainGUI;
        getContentPane().setLayout(new BorderLayout());
        pnlGeneral = new WebPanel(new GridLayout(3, 2));
        {
            txtName = new WebTextField(15);
            txtName.setInputPrompt("Enter map name...");
            txtName.setText(map.getName());
            pnlGeneral.add(new WebLabel("Map Name: "), "0, 0");
            pnlGeneral.add(txtName, "1, 0");
            cbTilesets = new WebComboBox(mainGUI.getStory().getTileSets().values().toArray());

            pnlGeneral.add(new WebLabel("TileSet: "), "0,1");
            pnlGeneral.add(cbTilesets, "1,1");
            spWidth = new WebSpinner(new SpinnerNumberModel(map.getWidth(), 1, 200, 1));
            spHeight = new WebSpinner(new SpinnerNumberModel(map.getHeight(), 1, 200, 1));
            pnlGeneral.add(new WebLabel("Dimensions: "), "0,2");
            pnlGeneral.add(new WebPanel(new GridLayout(1, 2), spWidth, spHeight), "1,2");
        }
        getContentPane().add(new WebPanel(new FlowLayout(FlowLayout.LEFT), pnlGeneral));
        pnlButtons = new WebPanel();
        {
            btnOk = new WebButton("OK");
            btnOk.addActionListener(this);
            btnCancel = new WebButton("Cancel");
            btnCancel.addActionListener(this);
            pnlButtons.add(new GroupPanel(btnOk, btnCancel));
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
        if(e.getSource() == btnOk) {
            mainGUI.getMapList().repaint();
            this.dispose();
        } else if(e.getSource() == btnCancel) {
            this.dispose();
        }
    }
}
