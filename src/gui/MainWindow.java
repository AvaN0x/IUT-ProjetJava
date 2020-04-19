package gui;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.*;

public class MainWindow extends JFrame implements ActionListener {
    protected ArrayList<Client> clients;

    private JButton btn_newCommande;

    public MainWindow() {
        super("titre");
        setLocation(300, 200);
        setSize(1280, 720);

        initComponents();

        clients = new ArrayList<Client>();
    }

    private void initComponents() {
        btn_newCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_newCommande.setToolTipText("Ajouter une commande");
        btn_newCommande.addActionListener(this);

        setLayout(new BorderLayout());
        var toolbar = new JToolBar();

        toolbar.add(btn_newCommande);
        toolbar.addSeparator();

        add(toolbar, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_newCommande) {
            var commandeDialog = new CommandeDialog(this);
            commandeDialog.setVisible(true);
        }
    }

    public void commandeDialogReturn()
    {

    }
}
