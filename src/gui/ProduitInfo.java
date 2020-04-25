package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.Produit;

public class ProduitInfo extends JDialog {
    Produit produit;

    public ProduitInfo(Window owner, Produit produit) {
        super(owner, "Gestion vidéothèque - Information produit");
        this.produit = produit;

        setLocation(300, 200);
        setSize(320, 220);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new Panel(new GridLayout(6,1));

        var pnl_title = new Panel(new FlowLayout());
        var lbl_titleStatic = new JLabel("Titre :");
        var lbl_title = new JLabel(produit.getTitle());
        pnl_title.add(lbl_titleStatic);
        pnl_title.add(lbl_title);
        
        var pnl_id = new Panel(new FlowLayout());
        var lbl_idStatic = new JLabel("ID :");
        var lbl_id = new JLabel(produit.getId());
        pnl_id.add(lbl_idStatic);
        pnl_id.add(lbl_id);

        var pnl_dailyPrice = new Panel(new FlowLayout());
        var lbl_dailyPriceStatic = new JLabel("Prix / jour :");
        var lbl_dailyPrice = new JLabel(Double.toString(produit.getDailyPrice()));
        pnl_dailyPrice.add(lbl_dailyPriceStatic);
        pnl_dailyPrice.add(lbl_dailyPrice);

        var pnl_class = new Panel(new FlowLayout());
        var lbl_classStatic = new JLabel("Type :");
        var lbl_class = new JLabel(produit.getClass().getSimpleName());
        pnl_class.add(lbl_classStatic);
        pnl_class.add(lbl_class);

        // TODO ajouter l'option differente a chaque prod

        var pnl_dispoPrice = new Panel(new FlowLayout());
        var lbl_dispoPriceStatic = new JLabel("Disponible :");
        var lbl_dispoPrice = new JLabel(Integer.toString(produit.getDispo()));
        pnl_dispoPrice.add(lbl_dispoPriceStatic);
        pnl_dispoPrice.add(lbl_dispoPrice);

        var pnl_loue = new Panel(new FlowLayout());
        var lbl_loueStatic = new JLabel("Loué :");
        var lbl_loue = new JLabel(Integer.toString(produit.getQuantity() - produit.getDispo()));
        pnl_loue.add(lbl_loueStatic);
        pnl_loue.add(lbl_loue);

        pnl_fields.add(pnl_title);
        pnl_fields.add(pnl_id);
        pnl_fields.add(pnl_dailyPrice);
        pnl_fields.add(pnl_class);
        pnl_fields.add(pnl_dispoPrice);
        pnl_fields.add(pnl_loue);

        add(pnl_fields, BorderLayout.CENTER);

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
}