package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.Client;
import app.ClientFidele;
import app.ClientOccas;

public class UserDialog extends JDialog implements ActionListener {
    private JLabel lbl_nom;
    private JLabel lbl_prenom;
    private JTextField tf_nom;
    private JTextField tf_prenom;
    private JCheckBox cb_fidel;
    private JButton btn_valider;
    private JButton btn_cancel;

    public UserDialog(Window owner) {
        super(owner, "test - Nouveau client");
        setLocation(300, 200);
        setSize(600, 300);

        initComponents();
    }

    public void initComponents() {
        setLayout(new FlowLayout());

        lbl_nom = new JLabel("Nom :");
        tf_nom = new JTextField(10);
        lbl_prenom = new JLabel("Prenom :");
        tf_prenom = new JTextField(10);
        cb_fidel = new JCheckBox("Fidèle");
        cb_fidel.setToolTipText("Le client est-il fidèle ?");
        btn_valider = new JButton("Valider");
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.addActionListener(this);

        add(lbl_nom);
        add(tf_nom);
        add(lbl_prenom);
        add(tf_prenom);
        add(cb_fidel);
        add(btn_valider);
        add(btn_cancel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            Client client;
            if (cb_fidel.isSelected())
                client = new ClientFidele(tf_nom.getName(), tf_prenom.getName());
            else
                client = new ClientOccas(tf_nom.getName(), tf_prenom.getName());
            setVisible(false);
            var owner = (CommandeDialog) getOwner();
            owner.userDialogReturn(client);
        }
    }
}
