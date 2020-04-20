package gui;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import app.*;

public class MainWindow extends JFrame implements ActionListener, ListSelectionListener {
    protected DefaultListModel<Client> clients;

    private boolean dialogShowing = false;

    private JPanel pnl_clients;
    private JButton btn_newCommande;
    private JButton btn_deluser;
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

        pnl_clients = new JPanel(new FlowLayout());
        l_clients = new JList<Client>(clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);
        var l_clientsScrollPane = new JScrollPane(l_clients);

        add(toolbar, BorderLayout.NORTH);
        add(l_clientsScrollPane, BorderLayout.EAST);
    }

    public void actionPerformed(ActionEvent e) {
        if (!dialogShowing)
            if (e.getSource() == btn_newCommande) {
                var commandeDialog = new CommandeDialog(this);
                commandeDialog.setVisible(true);
                dialogShowing = false;
            }
    }

    public void commandeDialogReturn() {
        dialogShowing = false;
    }

    public void valueChanged(ListSelectionEvent e) {

    }
}
