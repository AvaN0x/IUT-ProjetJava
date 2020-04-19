package gui;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.*;

public class MainWindow extends JFrame implements ActionListener {
    protected JList<Client> clients;

    private boolean dialogShowing = false;

    private JPanel pnl_clients;
    private JButton btn_newCommande;
    private JButton btn_deluser;

    public MainWindow() {
        super("titre");
        setLocation(300, 200);
        setSize(1280, 720);

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
        clients = new JList<Client>();
        add(clients, BorderLayout.EAST);

        add(toolbar, BorderLayout.NORTH);
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
}
