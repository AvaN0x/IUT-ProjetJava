package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import app.Client;
import app.Commande;

public class CommandeDialog extends JDialog implements ActionListener, ListSelectionListener {
    private boolean dialogShowing = false;

    private JList<Client> l_clients;
    private JButton btn_newUser;
    private JButton btn_delUser;
    private JButton btn_valider;
    private JButton btn_cancel;

    public CommandeDialog(Window owner) {
        super(owner, "test - Nouvelle commande");
        setLocation(300, 200);
        setSize(600, 300);

        initComponents();
    }

    private void initComponents() {
        var owner = (MainWindow) getOwner();
        setLayout(new BorderLayout());

        var pnl_clients = new JPanel(new FlowLayout());

        l_clients = new JList<Client>(owner.clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);
        var l_clientsScrollPane = new JScrollPane(l_clients);

        var pnl_clientsbtns = new JPanel(new GridLayout(2,1));
        btn_newUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\addUser.png")));
        btn_newUser.setToolTipText("Ajouter un client");
        btn_newUser.addActionListener(this);
        btn_delUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\deleteUser.png")));
        btn_delUser.setToolTipText("Supprimer un client sélectionné");
        btn_delUser.addActionListener(this);
        btn_delUser.setEnabled(false);
        pnl_clientsbtns.add(btn_newUser);
        pnl_clientsbtns.add(btn_delUser);

        pnl_clients.add(l_clientsScrollPane);
        pnl_clients.add(pnl_clientsbtns);

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.addActionListener(this);
        btn_valider.setEnabled(false);
        btn_cancel = new JButton("Annuler");
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_clients, BorderLayout.EAST);
        add(pnl_validate, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if(!dialogShowing)
        if (e.getSource() == btn_newUser) {
            var userDialog = new UserDialog(this);
            userDialog.setVisible(true);
            dialogShowing = true;
        } else if (e.getSource() == btn_delUser) {
            var owner = (MainWindow) getOwner();
            owner.clients.removeElement(l_clients.getSelectedValue());
        } else if (e.getSource() == btn_valider) {
            var commande = new Commande(l_clients.getSelectedValue());
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.commandeDialogReturn(commande);
            this.dispose();
        } else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.commandeDialogReturn();
            this.dispose();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);
                btn_valider.setEnabled(false);

            } else {
                btn_delUser.setEnabled(true);
                btn_valider.setEnabled(true);
            }
        }
    }

    public void userDialogReturn()
    {
        dialogShowing = false;
    }
    public void userDialogReturn(Client client) {
        var owner = (MainWindow) getOwner();
        owner.clients.addElement(client);
        userDialogReturn();
    }

}
