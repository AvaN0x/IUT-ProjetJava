package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import app.*;

public class MainWindow extends JFrame implements ActionListener, ListSelectionListener {
    protected DefaultListModel<Client> clients;
    protected TableauProduits produits;
    protected DefaultListModel<Commande> commandes;

    private JTabbedPane tab;
    private JButton btn_newCommande;
    private JButton btn_newProd;
    private JButton btn_delUser;
    private JList<Client> l_clients;
    private JTable t_produits;
    private TableRowSorter<TableModel> t_produitsSorter;

    public MainWindow() {
        super("Gestion vidéothèque");
        setLocation(300, 200);
        setSize(1280, 720);

        clients = new DefaultListModel<Client>();
        produits = new TableauProduits();
        commandes = new DefaultListModel<Commande>();

        produits.add(new DVD("DVD", .8, 2, "Une personne connue"));

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        var toolbar = new JToolBar(null, JToolBar.VERTICAL);
        toolbar.setFloatable(false);

        btn_newCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_newCommande.setToolTipText("Ajouter une commande");
        btn_newCommande.addActionListener(this);

        btn_newProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\newProd.png")));
        btn_newProd.setToolTipText("Ajouter un produit");
        btn_newProd.addActionListener(this);

        toolbar.add(btn_newCommande);
        toolbar.add(btn_newProd);
        toolbar.addSeparator();
        add(toolbar, BorderLayout.WEST);

        tab = new JTabbedPane();
        tab.addTab("Commandes", initComponentsCommandes());
        tab.addTab("Produits", initComponentsProduits());
        tab.addTab("Clients", initComponentsClients());

        add(tab);
    }


    private JPanel initComponentsCommandes() {
        var pnl_commandesTab = new JPanel();

        var lbl_commandesTab = new JLabel("Liste des commandes :"); 
        pnl_commandesTab.add(lbl_commandesTab);

        return pnl_commandesTab;
    }

    private JPanel initComponentsProduits() {
        var pnl_produitsTab = new JPanel();

        var lbl_commandesTab = new JLabel("Liste des produits :"); // TODO fix l'affichage
        pnl_produitsTab.add(lbl_commandesTab);

        var pnl_produits = new JPanel(new FlowLayout());
        var pnl_filter = new JPanel(new GridLayout(0, 1));
        // checkbox for filter

        t_produits = new JTable(produits);
        t_produitsSorter = new TableRowSorter<TableModel>(t_produits.getModel());
        t_produitsSorter.setSortsOnUpdates(true);
        t_produits.setRowSorter(t_produitsSorter);
        var pnl_produitTable = new JPanel();
        pnl_produitTable.add(new JScrollPane(t_produits));

        pnl_produits.add(pnl_filter);
        pnl_produits.add(pnl_produitTable);

        pnl_produitsTab.add(pnl_produits, BorderLayout.CENTER);

        return pnl_produitsTab;
    }

    private JPanel initComponentsClients() {
        var pnl_clientsTab = new JPanel();

        var lbl_clientsTab = new JLabel("Liste des clients :"); // TODO fix l'affichage
        pnl_clientsTab.add(lbl_clientsTab);

        var pnl_clients = new JPanel(new FlowLayout());
        l_clients = new JList<Client>(clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);

        var l_clientsScrollPane = new JScrollPane(l_clients);
        btn_delUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\deleteUser.png")));
        btn_delUser.setToolTipText("Supprimer un client sélectionné");
        btn_delUser.addActionListener(this);
        btn_delUser.setEnabled(false);
        pnl_clients.add(l_clientsScrollPane);
        pnl_clients.add(btn_delUser);

        pnl_clientsTab.add(pnl_clients, BorderLayout.EAST);

        return pnl_clientsTab;
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
        } else if (e.getSource() == btn_delUser)
        {
            clients.removeElement(l_clients.getSelectedValue());
        }
    }

    
    public void dialogReturn() {
        this.setEnabled(true);
        this.toFront();
    }

    public void commandeDialogReturn(Commande commande) {
        commandes.addElement(commande);
        dialogReturn();
    }

    public void produitDialogReturn(Produit produit) {
        produits.add(produit);
        dialogReturn();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);

            } else {
                btn_delUser.setEnabled(true);
            }
        }
    }
}
