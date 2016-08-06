package com.pokedroid.editor.gui.dialog;

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
import com.pokedroid.editor.asset.Story;
import com.pokedroid.editor.gui.MainGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SpinnerNumberModel;

/**
 * <p>The {@code NewStoryDialog} is used to </p>
 */
public class NewStoryDialog extends WebDialog implements ActionListener {

    private MainGUI mainGUI;
    private WebPanel pnlGeneral, pnlButtons;
    private WebTextField txtName;
    private WebButton btnCreate, btnCancel;

    /**
     * <p>Constructs a new {@code NewStoryDialog}.</p>
     *
     * @param mainGUI THe mainGUI.
     */
    public NewStoryDialog(MainGUI mainGUI) {
        super(mainGUI, true);
        this.mainGUI = mainGUI;
        getContentPane().setLayout(new BorderLayout());
        pnlGeneral = new WebPanel(new GridLayout(1, 2));
        {
            txtName = new WebTextField(15);
            txtName.setInputPrompt("Enter a story name...");
            pnlGeneral.add(new WebLabel("Story Name: "), "0, 0");
            pnlGeneral.add(txtName, "1, 0");
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
        this.setLocationRelativeTo(mainGUI);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnCreate) {
            if (txtName.getText().length() <= 0) {
                final WebInnerNotification mapNote = new WebInnerNotification();
                mapNote.setContent("Story name cannot be empty!");
                mapNote.setDisplayTime(1000);
                mapNote.setIcon(NotificationIcon.warning);
                NotificationManager.showInnerNotification(mainGUI, mapNote);
                return;
            }
            Story s = new Story(txtName.getText());
            mainGUI.setStory(s);
            this.dispose();
        } else if(e.getSource() == btnCancel) {
            this.dispose();
        }
    }
}
