package tests;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import gui.*;

public class testTabbedPane extends JFrame implements ActionListener {
    private JTabbedPane tab;
    private JButton btn_newCommande;
    private JButton btn_newProd;

    private testTabbedPane() {
        super();
        setLocation(300, 200);
        setSize(1280, 720);

        var toolbar = new JToolBar(null, JToolBar.VERTICAL);
        toolbar.setFloatable(false);

        btn_newCommande = new JButton(new ImageIcon(getClass().getResource("..\\gui\\icons\\add.png")));
        btn_newCommande.setToolTipText("Ajouter une commande");
        btn_newCommande.addActionListener(this);

        btn_newProd = new JButton(new ImageIcon(getClass().getResource("..\\gui\\icons\\newProd.png")));
        btn_newProd.setToolTipText("Ajouter un produit");
        btn_newProd.addActionListener(this);


        toolbar.add(btn_newCommande);
        toolbar.add(btn_newProd);
        toolbar.addSeparator();
        add(toolbar, BorderLayout.WEST);


        // set grid layout for the frame
        tab = new JTabbedPane();
 
        tab.addTab("Commandes", makePanel("This is tab 1"));
        tab.addTab("Produits", makePanel("This is tab 2"));
        tab.addTab("Clients", makePanel("This is tab 3"));


        // to get the current selected tab
        // System.out.println(tab.getSelectedIndex()); 

        add(tab);
    }
    
    // methode temporaire pour creer un panel
            private JPanel makePanel(String text) {
                JPanel p = new JPanel();
                p.add(new Label(text));
                p.setLayout(new GridLayout(1, 1));
                return p;
            }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_newCommande) {
            var commandeDialog = new CommandeDialog(this);
            commandeDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_newProd) {
            var ProduitDialog = new ProduitDialog(this);
            ProduitDialog.setVisible(true);
            this.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        new testTabbedPane().setVisible(true); 
    }
 
}