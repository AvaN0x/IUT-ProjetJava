package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.Client;
import app.ClientFidele;
import app.ClientOccas;

public class UserInfo extends JDialog implements ActionListener {
    Client client;

    private JButton btn_leave;

    public UserInfo(Window owner, Client client) {
        super(owner, "Gestion vidéothèque - Information client");
        this.client = client;

        setLocation(300, 200);
        setSize(320, 180);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new Panel(new GridLayout(4,1));

        var pnl_nom = new Panel(new FlowLayout());
        var lbl_nomStatic = new JLabel("Nom :");
        var lbl_nom = new JLabel(client.getNom());
        pnl_nom.add(lbl_nomStatic);
        pnl_nom.add(lbl_nom);
        
        var pnl_prenom = new Panel(new FlowLayout());
        var lbl_prenomStatic = new JLabel("Prenom :");
        var lbl_prenom = new JLabel(client.getPrenom());
        pnl_prenom.add(lbl_prenomStatic);
        pnl_prenom.add(lbl_prenom);

        var pnl_id = new Panel(new FlowLayout());
        var lbl_idStatic = new JLabel("ID :");
        var lbl_id = new JLabel(client.getId());
        pnl_id.add(lbl_idStatic);
        pnl_id.add(lbl_id);

        var pnl_fidel = new Panel(new FlowLayout());
        var cb_fidel = new JCheckBox("Fidèle");
        cb_fidel.setEnabled(false);
        if (client instanceof ClientFidele)
            cb_fidel.setSelected(true);
        else
            cb_fidel.setSelected(false);
        pnl_fidel.add(cb_fidel);
        
        // TODO ajouter les commandes de l'utilisateur ?

        pnl_fields.add(pnl_nom);
        pnl_fields.add(pnl_prenom);
        pnl_fields.add(pnl_id);
        pnl_fields.add(pnl_fidel);

        var pnl_validate = new Panel(new FlowLayout());
        btn_leave = new JButton("Quitter");
        btn_leave.addActionListener(this);
        pnl_validate.add(btn_leave);

        add(pnl_fields, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                var owner = (MainWindow) getOwner();
                owner.dialogReturn();
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btn_leave) {
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.dialogReturn();
            this.dispose();
        }
    }
}