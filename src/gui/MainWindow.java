package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import app.*;

public class MainWindow extends JFrame implements ActionListener, ListSelectionListener {
    protected DefaultListModel<Client> clients;

    private boolean dialogShowing = false;

    private JButton btn_newCommande;
    private JButton btn_delUser;
    private JList<Client> l_clients;

    public MainWindow() {
        super("titre");
        setLocation(300, 200);
        setSize(1280, 720);

        clients = new DefaultListModel<Client>();

        initComponents();
    }

    private void initComponents() {
        btn_newCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_newCommande.setToolTipText("Ajouter une commande");
        btn_newCommande.addActionListener(this);

        setLayout(new BorderLayout());
        var toolbar = new JToolBar();

        toolbar.add(btn_newCommande);
        toolbar.addSeparator();

        var pnl_clients = new JPanel(new FlowLayout());
        l_clients = new JList<Client>(clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);
        var l_clientsScrollPane = new JScrollPane(l_clients);
        btn_delUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\deleteUser.png")));
        btn_delUser.setToolTipText("Supprimer un client sélectionné");
        btn_delUser.addActionListener(this);
        btn_delUser.setEnabled(false);
        pnl_clients.add(l_clientsScrollPane);
        pnl_clients.add(btn_delUser);

        add(toolbar, BorderLayout.NORTH);
        add(pnl_clients, BorderLayout.EAST);
    }

    public void actionPerformed(ActionEvent e) {
        if (!dialogShowing)
            if (e.getSource() == btn_newCommande) {
                var commandeDialog = new CommandeDialog(this);
                commandeDialog.setVisible(true);
                dialogShowing = true;
            }
            else if (e.getSource() == btn_delUser) {
                clients.removeElement(l_clients.getSelectedValue());
            }
    }

    public void commandeDialogReturn() {
        dialogShowing = false;
    }
    public void commandeDialogReturn(Commande commande) {
        dialogShowing = false;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);

            } else {
                btn_delUser.setEnabled(true);
            }
        }
    }
}
