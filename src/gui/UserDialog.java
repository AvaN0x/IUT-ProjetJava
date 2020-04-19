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
        setSize(200, 150);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new Panel();
        pnl_fields.setLayout(new GridLayout(3,1));

        var pnl_nom = new Panel();
        pnl_nom.setLayout(new FlowLayout());
        lbl_nom = new JLabel("Nom :");
        tf_nom = new JTextField(10);
        pnl_nom.add(lbl_nom);
        pnl_nom.add(tf_nom);
        
        var pnl_prenom = new Panel();
        pnl_prenom.setLayout(new FlowLayout());
        lbl_prenom = new JLabel("Prenom :");
        tf_prenom = new JTextField(10);
        pnl_prenom.add(lbl_prenom);
        pnl_prenom.add(tf_prenom);
        
        var pnl_fidel = new Panel();
        pnl_fidel.setLayout(new FlowLayout());
        pnl_fidel.add(pnl_nom);
        pnl_fidel.add(pnl_prenom);
        cb_fidel = new JCheckBox("Fidèle");
        cb_fidel.setToolTipText("Le client est-il fidèle ?");
        pnl_fidel.add(cb_fidel);
        
        pnl_fields.add(pnl_nom);
        pnl_fields.add(pnl_prenom);
        pnl_fields.add(pnl_fidel);

        var pnl_validate = new Panel();
        pnl_validate.setLayout(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_fields, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            Client client;
            if (cb_fidel.isSelected())
                client = new ClientFidele(tf_nom.getText(), tf_prenom.getText());
            else
                client = new ClientOccas(tf_nom.getText(), tf_prenom.getText());
            setVisible(false);
            var owner = (CommandeDialog) getOwner();
            owner.userDialogReturn(client);
        }
        else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (CommandeDialog) getOwner();
            owner.userDialogReturn();
        }
    }
}
