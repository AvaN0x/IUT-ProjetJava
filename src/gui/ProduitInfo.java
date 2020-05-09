package gui;

import java.awt.*;
import javax.swing.*;

import app.Produit;

@SuppressWarnings("serial")
public class ProduitInfo extends JDialog {
    private Produit produit;

    public ProduitInfo(Window owner, Produit produit) {
        super(owner, "Information produit");
        this.produit = produit;

        setSize(320, 200);
        setLocationRelativeTo(owner);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new JPanel(new GridLayout(6,1));

        var pnl_title = new JPanel(new FlowLayout());
        var lbl_titleStatic = new JLabel("Titre :");
        var lbl_title = new JLabel(produit.getTitle());
        pnl_title.add(lbl_titleStatic);
        pnl_title.add(lbl_title);
        
        var pnl_id = new JPanel(new FlowLayout());
        var lbl_idStatic = new JLabel("ID :");
        var lbl_id = new JLabel(produit.getId());
        pnl_id.add(lbl_idStatic);
        pnl_id.add(lbl_id);

        var pnl_dailyPrice = new JPanel(new FlowLayout());
        var lbl_dailyPriceStatic = new JLabel("Prix / jour :");
        var lbl_dailyPrice = new JLabel(Double.toString(produit.getDailyPrice()));
        pnl_dailyPrice.add(lbl_dailyPriceStatic);
        pnl_dailyPrice.add(lbl_dailyPrice);

        var pnl_class = new JPanel(new FlowLayout());
        var lbl_classStatic = new JLabel("Type :");
        var lbl_class = new JLabel(produit.getClass().getSimpleName());
        pnl_class.add(lbl_classStatic);
        pnl_class.add(lbl_class);

        var pnl_option1 = new JPanel(new FlowLayout());
        var lbl_option1Static = new JLabel();
        for (int i = 0; i < Utils.produitsTypes.length; i++) {
            if (Utils.produitsTypes[i][0].replaceAll(" ", "").contains(produit.getClass().getSimpleName()))
                lbl_option1Static.setText(Utils.produitsTypes[i][1] + " :");
        }
        
        var lbl_option1 = new JLabel((String) produit.getOption1());
        pnl_option1.add(lbl_option1Static);
        pnl_option1.add(lbl_option1);

        var pnl_quantity = new JPanel(new FlowLayout());
        var lbl_quantityStatic = new JLabel("Quantité :");
        var lbl_quantity = new JLabel(Integer.toString(produit.getQuantity()));
        pnl_quantity.add(lbl_quantityStatic);
        pnl_quantity.add(lbl_quantity);

        pnl_fields.add(pnl_title);
        pnl_fields.add(pnl_id);
        pnl_fields.add(pnl_dailyPrice);
        pnl_fields.add(pnl_class);
        pnl_fields.add(pnl_option1);
        pnl_fields.add(pnl_quantity);

        add(pnl_fields, BorderLayout.CENTER);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                dispose();
            }
        });
    }
}
