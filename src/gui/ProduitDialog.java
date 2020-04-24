package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.*;

public class ProduitDialog extends JDialog implements ActionListener, ItemListener {
    private JLabel lbl_title;
    private JLabel lbl_price;
    private JLabel lbl_quantity;
    private JLabel lbl_option1;
    private JTextField tf_title;
    private JTextField tf_price;
    private JTextField tf_quantity;
    private JTextField tf_option1;
    private JComboBox<String> cbx_type;
    private JButton btn_valider;
    private JButton btn_cancel;

    public ProduitDialog(Window owner) {
        super(owner, "Gestion vidéothèque - Nouveau produit");
        setLocation(300, 200);
        setSize(200, 200);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new JPanel(new GridLayout(5, 1));

        var pnl_title = new JPanel(new FlowLayout());
        lbl_title = new JLabel("Titre :");
        tf_title = new JTextField(10);
        pnl_title.add(lbl_title);
        pnl_title.add(tf_title);
        pnl_fields.add(pnl_title);

        var pnl_price = new JPanel(new FlowLayout());
        lbl_price = new JLabel("Prix :");
        tf_price = new JTextField(5);
        pnl_price.add(lbl_price);
        pnl_price.add(tf_price);
        pnl_fields.add(pnl_price);

        var pnl_quantity = new JPanel(new FlowLayout());
        lbl_quantity = new JLabel("Quantitée :");
        tf_quantity = new JTextField(3);
        pnl_quantity.add(lbl_quantity);
        pnl_quantity.add(tf_quantity);
        pnl_fields.add(pnl_quantity);

        cbx_type = new JComboBox<String>();
        for (var item : Utils.produits) {
            cbx_type.addItem(item[0]);
        }
        cbx_type.addItemListener(this);
        pnl_fields.add(cbx_type);

        var pnl_optionfields = new JPanel(new GridLayout(1, 1));
        var pnl_option1 = new JPanel(new FlowLayout());
        lbl_option1 = new JLabel(Utils.produits[cbx_type.getSelectedIndex()][1] + " :");
        tf_option1 = new JTextField(10);
        pnl_option1.add(lbl_option1);
        pnl_option1.add(tf_option1);
        pnl_optionfields.add(pnl_option1);
        pnl_fields.add(pnl_optionfields);

        var pnl_validate = new Panel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

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
        if (e.getSource() == btn_valider) {
            if(tf_title.getText().trim().length() <= 0 ||
               tf_price.getText().trim().length() <= 0 ||
               tf_quantity.getText().trim().length() <= 0 ||
               tf_option1.getText().trim().length() <= 0)
            {
                JOptionPane.showMessageDialog(this, "L'un des champs est vide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            setVisible(false);

            Produit produit;
            if(cbx_type.getSelectedIndex() == 1) // It's a Roman
                produit = new Roman(tf_title.getText(), Double.parseDouble(tf_price.getText()), Integer.parseInt(tf_quantity.getText()), tf_option1.getText());
            else if (cbx_type.getSelectedIndex() == 2) // It's a Manuel Scolaire
                produit = new ManuelScolaire(tf_title.getText(), Double.parseDouble(tf_price.getText()), Integer.parseInt(tf_quantity.getText()), tf_option1.getText());
            else if (cbx_type.getSelectedIndex() == 3) // It's a Dictionnaire
                produit = new Dictionnaire(tf_title.getText(), Double.parseDouble(tf_price.getText()), Integer.parseInt(tf_quantity.getText()), tf_option1.getText());
            //TODO créer un calendar à partir d'un string
            //else if (cbx_type.getSelectedIndex() == 4) // It's a CD
            //    produit = new CD(tf_title.getText(), Double.parseDouble(tf_price.getText()), Integer.parseInt(tf_quantity.getText()), tf_option1.getText());
            else if (cbx_type.getSelectedIndex() == 5) // It's a DVD
                produit = new DVD(tf_title.getText(), Double.parseDouble(tf_price.getText()), Integer.parseInt(tf_quantity.getText()), tf_option1.getText());
            else // It's a BD (the first one who selected by default)
                produit = new BD(tf_title.getText(), Double.parseDouble(tf_price.getText()), Integer.parseInt(tf_quantity.getText()), tf_option1.getText());

            var owner = (MainWindow) getOwner();
            owner.produitDialogReturn(produit);
            owner.dialogReturn();
            this.dispose();
        } else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.dialogReturn();
            this.dispose();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbx_type)
            lbl_option1.setText(Utils.produits[cbx_type.getSelectedIndex()][1] + " :");
    }

}
