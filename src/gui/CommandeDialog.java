package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.Client;

public class CommandeDialog extends JDialog implements ActionListener {
    private boolean dialogShowing = false;

    private JButton btn_newUser;
    private JButton btn_valider;
    private JButton btn_cancel;

    public CommandeDialog(Window owner) {
        super(owner, "test - Nouvelle commande");
        setLocation(300, 200);
        setSize(600, 300);

        initComponents();
    }

    private void initComponents() {
        btn_newUser = new JButton(new ImageIcon(getClass().getResource("\\icons\\addUser.png")));
        btn_newUser.setToolTipText("Ajouter un client");
        btn_newUser.addActionListener(this);

        add(btn_newUser);
    }

    public void actionPerformed(ActionEvent e) {
        if(!dialogShowing)
        if (e.getSource() == btn_newUser) {
            var userDialog = new UserDialog(this);
            userDialog.setVisible(true);
            dialogShowing = true;
        } else if (e.getSource() == btn_valider) {
            var owner = (MainWindow) getOwner();
            owner.commandeDialogReturn();
        } else if (e.getSource() == btn_cancel) {
            var owner = (MainWindow) getOwner();
            owner.commandeDialogReturn();
        }
    }

    public void userDialogReturn()
    {
        dialogShowing = false;
    }
    public void userDialogReturn(Client client) {
        var owner = (MainWindow) getOwner();
        owner.clients.add(new JLabel(client.getNom() + " " + client.getPrenom()), client);
        userDialogReturn();
    }

}
