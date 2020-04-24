package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.Produit;

public class ProduitDialog extends JDialog implements ActionListener {
    private JLabel lbl_title;
    private JLabel lbl_price;
    private JLabel lbl_quantity;
    private JTextField tf_title;
    private JTextField tf_price;
    private JTextField tf_quantity;
    private JComboBox<Produit> cbx_type;
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

        var pnl_fields = new JPanel(new GridLayout(4,1));

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
        
        cbx_type = new JComboBox<Produit>();
        pnl_fields.add(cbx_type);

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
            setVisible(false);
            var owner = (MainWindow) getOwner();
            // owner.produitDialogReturn(produit);
            owner.dialogReturn();
            this.dispose();
        }
        else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.dialogReturn();
            this.dispose();
        }
    }

}
