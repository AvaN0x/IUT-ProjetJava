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
    protected TableauCommandes commandes;

    private JTabbedPane tab;
    private JButton btn_toolbarNewCommande;
    private JButton btn_toolbarNewProd;
    private JButton btn_delUser;
    private JButton btn_infoUser;

    private JList<Client> l_clients;

    private JTable t_produits;
    private TableRowSorter<TableModel> t_produitsSorter;
    private JTable t_commandes;
    private TableRowSorter<TableModel> t_commandesSorter;
    private JButton btn_newProd;
    private JButton btn_remProd;
    private JButton btn_addQuantityProd;
    private JButton btn_infoProd;
    private JButton btn_newCommande;

    public MainWindow() {
        super("Gestion vidéothèque");
        setLocation(300, 200);
        setSize(1280, 720);

        clients = new DefaultListModel<Client>();
        produits = new TableauProduits();
        commandes = new TableauCommandes();

        produits.add(new DVD("DVD1", .8, 2, "Une personne connue1"));
        produits.add(new DVD("DVD2", 2, 1, "Une personne connue2"));
        produits.add(new DVD("DVD3", .2, 4, "Une personne connue3"));
        produits.add(new DVD("DVD4", 1.5, 1, "Une personne connue4"));
        produits.add(new Dictionnaire("dico1", .5, 1, "FR"));
        produits.add(new Roman("roman1", 5.5, 5, "Une personne connue5"));
        clients.addElement(new ClientFidele("nom", "prenom"));


        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        var toolbar = new JToolBar(null, JToolBar.VERTICAL);
        toolbar.setFloatable(false);

        btn_toolbarNewCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_toolbarNewCommande.setToolTipText("Ajouter une commande");
        btn_toolbarNewCommande.addActionListener(this);

        btn_toolbarNewProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\newProd.png")));
        btn_toolbarNewProd.setToolTipText("Ajouter un produit");
        btn_toolbarNewProd.addActionListener(this);

        toolbar.add(btn_toolbarNewCommande);
        toolbar.add(btn_toolbarNewProd);
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
        // TODO fix l'affichage

        var pnl_commandes = new JPanel(new FlowLayout());
        var pnl_filter = new JPanel(new GridLayout(0, 1));
        //TODO checkbox for filter

        t_commandes = new JTable(commandes);
        t_commandesSorter = new TableRowSorter<TableModel>(t_commandes.getModel());
        t_commandesSorter.setSortsOnUpdates(true);
        t_commandes.setRowSorter(t_commandesSorter);
        var pnl_produitTable = new JPanel();
        pnl_produitTable.add(new JScrollPane(t_commandes));

        pnl_commandes.add(pnl_filter);
        pnl_commandes.add(pnl_produitTable);

        btn_newCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_newCommande.setToolTipText("Ajouter une commande");
        btn_newCommande.addActionListener(this);
        //TODO boutton pour supprimer une commande

        pnl_commandesTab.add(lbl_commandesTab, BorderLayout.NORTH);
        pnl_commandesTab.add(pnl_commandes, BorderLayout.CENTER);
        pnl_commandesTab.add(btn_newCommande);

        return pnl_commandesTab;
    }

    private JPanel initComponentsProduits() {
        var pnl_produitsTab = new JPanel();

        var lbl_commandesTab = new JLabel("Liste des produits :");
        // TODO fix l'affichage

        var pnl_produits = new JPanel(new FlowLayout());
        var pnl_filter = new JPanel(new GridLayout(0, 1));
        //TODO checkbox for filter

        t_produits = new JTable(produits);
        t_produitsSorter = new TableRowSorter<TableModel>(t_produits.getModel());
        t_produitsSorter.setSortsOnUpdates(true);
        t_produits.setRowSorter(t_produitsSorter);
        var pnl_produitTable = new JPanel();
        pnl_produitTable.add(new JScrollPane(t_produits));

        pnl_produits.add(pnl_filter);
        pnl_produits.add(pnl_produitTable);

        var pnl_prodbtns = new JPanel(new GridLayout(4, 1));
        btn_newProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\newProd.png")));
        btn_newProd.setToolTipText("Ajouter un produit");
        btn_newProd.addActionListener(this);
        btn_remProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\remProd.png")));
        btn_remProd.setToolTipText("Supprimer un produit");
        btn_remProd.addActionListener(this);
        btn_addQuantityProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\addQuantityProd.png")));
        btn_addQuantityProd.setToolTipText("Ajouter du stock à un produit");
        btn_addQuantityProd.addActionListener(this);
        btn_infoProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\info.png")));
        btn_infoProd.setToolTipText("Plus d'informations à propos du produit");
        btn_infoProd.addActionListener(this);
        pnl_prodbtns.add(btn_newProd);
        pnl_prodbtns.add(btn_remProd);
        pnl_prodbtns.add(btn_addQuantityProd);
        pnl_prodbtns.add(btn_infoProd);

        pnl_produitsTab.add(lbl_commandesTab, BorderLayout.NORTH);
        pnl_produitsTab.add(pnl_produits, BorderLayout.CENTER);
        pnl_produitsTab.add(pnl_prodbtns);

        return pnl_produitsTab;
    }

    private JPanel initComponentsClients() {
        var pnl_clientsTab = new JPanel();

        var lbl_clientsTab = new JLabel("Liste des clients :");
        // TODO fix l'affichage

        var pnl_clients = new JPanel(new FlowLayout());
        l_clients = new JList<Client>(clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);

        var l_clientsScrollPane = new JScrollPane(l_clients);
        var pnl_clientsbtns = new JPanel(new GridLayout(2, 1));
        btn_delUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\deleteUser.png")));
        btn_delUser.setToolTipText("Supprimer un client sélectionné");
        btn_delUser.addActionListener(this);
        btn_delUser.setEnabled(false);
        btn_infoUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\info.png")));
        btn_infoUser.setToolTipText("Plus d'informations à propos du client");
        btn_infoUser.addActionListener(this);
        btn_infoUser.setEnabled(false);
        pnl_clientsbtns.add(btn_delUser);
        pnl_clientsbtns.add(btn_infoUser);
        
        pnl_clients.add(l_clientsScrollPane);
        pnl_clientsTab.add(lbl_clientsTab, BorderLayout.NORTH);
        pnl_clientsTab.add(pnl_clients, BorderLayout.EAST);
        pnl_clientsTab.add(pnl_clientsbtns);


        return pnl_clientsTab;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_toolbarNewCommande || e.getSource() == btn_newCommande) {
            var commandeDialog = new CommandeDialog(this);
            commandeDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_toolbarNewProd || e.getSource() == btn_newProd) {
            var ProduitDialog = new ProduitDialog(this);
            ProduitDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_delUser) {
            if (JOptionPane.showConfirmDialog(this,"Voulez vous vraiment supprimer le client ?", "Suppression client - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                clients.removeElement(l_clients.getSelectedValue());

        } else if (e.getSource() == btn_infoUser) {
            new UserInfo(this, l_clients.getSelectedValue()).setVisible(true);
        } else if (e.getSource() == btn_remProd) {
            if (t_produits.getSelectedRow() != -1)
                if (JOptionPane.showConfirmDialog(this,"Voulez vous vraiment supprimer le produit ?", "Suppression produit - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    produits.remove(t_produits.getSelectedRow());
        } else if (e.getSource() == btn_addQuantityProd) {
            if (t_produits.getSelectedRow() != -1) {
                try {
                    int quantity = Integer.parseInt(JOptionPane.showInputDialog("Combien voulez-vous rajouter au stock ?", 0));
                    if (quantity < 0)
                        JOptionPane.showMessageDialog(this, "L'entrée est négative ou nulle.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    produits.getProduit(t_produits.getSelectedRow()).addQuantity(quantity);
                    t_produits.repaint();
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(this, "L'entrée n'est pas un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btn_infoProd) {
            if (t_produits.getSelectedRow() != -1) {
                new ProduitInfo(this, produits.getProduit(t_produits.getSelectedRow())).setVisible(true);
            }
        }
    }

    
    public void dialogReturn() {
        this.setEnabled(true);
        this.toFront();
    }

    public void commandeDialogReturn(Commande commande) {
        commandes.add(commande);
        dialogReturn();
        tab.setSelectedIndex(0);
    }

    public void produitDialogReturn(Produit produit) {
        produits.add(produit);
        dialogReturn();
        tab.setSelectedIndex(1);
    }

    public void valueChanged(ListSelectionEvent e) {
        // TODO fix le cas ou il y a deja un client a l'ouverture du programme, il y a du coup aucuns boutons d'actifs
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);
                btn_infoUser.setEnabled(false);
            } else {
                btn_delUser.setEnabled(true);
                btn_infoUser.setEnabled(true);
            }
        }
    }
}
