package gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.sql.*;

import app.*;

// TODO faire les documentation pour chaque methodes
// TODO remplacer tout les string UI par des valeurs dans Utils.lang
    //! TENIR A JOUR LES FICHIERS fr_FR.json et en_EN.json

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener, ListSelectionListener, IMyUserDialogOwner, IMyProduitDialogOwner, IMyCommandeDialogOwner {
    private JMenuItem mnui_save;
    private JMenuItem mnui_newUser;
    private JMenuItem mnui_newCommande;
    private JMenuItem mnui_newProd;
    private JMenuItem mnui_settings;

    private JTabbedPane tab;
    private JButton btn_toolbarNewUser;
    private JButton btn_toolbarNewCommande;
    private JButton btn_toolbarNewProd;
    private JButton btn_toolbarSave;
    private JButton btn_toolbarSettings;

    private JButton btn_newUser;
    private JButton btn_delUser;
    private JButton btn_infoUser;
    private JList<Client> l_clients;

    private JTable t_produits;
    private JButton btn_newProd;
    private JButton btn_remProd;
    private JButton btn_editProd;
    private JButton btn_infoProd;

    private JTable t_commandes;
    private JButton btn_newCommande;
    private JButton btn_remCommande;
    private JButton btn_editCommande;
    private JButton btn_infoCommande;
    private JButton btn_exportCommande;

    public MainWindow() {
        super("Videotek");
        setLookNFeel();
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(".\\icons\\logo.png")));

        Utils.settings = new Settings();

        try {
            InputStream fileStream = new FileInputStream(new File(Utils.savingDir + "data.ser"));
            var input = new ObjectInputStream(fileStream);

            Utils.settings = (Settings) input.readObject();

            Utils.lang = new Lang();
            setTitle(Utils.lang.title);

            input.close();
        } catch (IOException e) {
            Utils.logStream.Error(e);
        } catch (ClassNotFoundException e) {
            Utils.logStream.Error(e);
        }

        Utils.clients = new DefaultListModel<Client>();
        Utils.produits = new TableauProduits();
        Utils.commandes = new TableauCommandes();

        if (new File(Utils.savingDir + "data.ser").exists()) {
            if (!Utils.settings.isLocal)
                JOptionPane.showMessageDialog(this, Utils.lang.connect_try,
                        Utils.lang.connect_title, JOptionPane.INFORMATION_MESSAGE);
            if (!Utils.load())
                if (!Utils.settings.isLocal)
                    JOptionPane.showMessageDialog(this, Utils.lang.connect_error, Utils.lang.connect_title,
                            JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this, Utils.lang.loading_error, "Chargement",
                            JOptionPane.ERROR_MESSAGE);
            else if (!Utils.settings.isLocal)
                JOptionPane.showMessageDialog(this, Utils.lang.connect_sucess, Utils.lang.connect_title,
                        JOptionPane.INFORMATION_MESSAGE);
        } else {
            Utils.clients.addElement(new ClientFidele("ricatte", "clément"));
            Utils.clients.addElement(new ClientFidele("sublet", "tom"));
            Utils.clients.addElement(new ClientOccas("hochet", "ric"));
            Utils.clients.addElement(new ClientFidele("térieur", "alex"));
            Utils.clients.addElement(new ClientOccas("térieur", "alain"));

            Utils.produits.add(new Roman("Harry Potter à l'école des sorciers", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et la chambre des secrets", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et le Prisonnier d'Azkaban", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et la Coupe de feu", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et l'Ordre du phénix", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et le Prince de Sang-Mêlé", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et les Reliques de la Mort", 0.99, 3, "J. K. Rowling"));
            Utils.produits.add(new Roman("Harry Potter et l'enfant maudit", 0.99, 4, "J. K. Rowling"));
            Utils.produits.add(new Dictionnaire("LAROUSSE", .65, 5, "FR"));
            Utils.produits.add(new ManuelScolaire("Objectif BAC Term S - BAC 2020", .99, 2, "hachette"));
            Utils.produits.add(new DVD("Le voyage de Chihiro", 1.99, 1, "Hayao Miyazaki"));
            Utils.produits.add(new BD("Les Simpson - Camping en délire", .75, 2, "Jungle!"));
            Utils.produits.add(new BD("Les Simpson - Un sacré foin !", .75, 1, "Jungle!"));
            var dateAdibou = Calendar.getInstance();
            dateAdibou.set(2003, 9, 24);
            Utils.produits.add(new CD("Adibou & le Secret de Paziral", .1, 1, dateAdibou));

            Calendar dateCreation = Calendar.getInstance();
            dateCreation.set(Calendar.MILLISECOND, 0);
            dateCreation.set(Calendar.SECOND, 0);
            dateCreation.set(Calendar.MINUTE, 0);
            dateCreation.set(Calendar.HOUR_OF_DAY, 0);
            dateCreation.set(2020, Calendar.APRIL, 26);
            Utils.commandes.add(new Commande(Utils.clients.get(0), dateCreation));
            Calendar dateFin = Calendar.getInstance();
            dateFin.set(Calendar.MILLISECOND, 0);
            dateFin.set(Calendar.SECOND, 0);
            dateFin.set(Calendar.MINUTE, 0);
            dateFin.set(Calendar.HOUR_OF_DAY, 0);
            dateFin.set(2020, Calendar.JUNE, 26);
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(0));
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(1));
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(2));
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(3));
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(4));
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(5));
            Utils.commandes.getItem(0).addEmprunt(dateFin, Utils.produits.getItem(6));

            dateCreation.set(2020, Calendar.MAY, 20);
            Utils.commandes.add(new Commande(Utils.clients.get(2), dateCreation));
            dateFin.setTimeInMillis(dateCreation.getTimeInMillis());
            dateFin.add(Calendar.DAY_OF_YEAR, 50);
            Utils.commandes.getItem(1).addEmprunt(dateFin, Utils.produits.getItem(8));
            Utils.commandes.getItem(1).addEmprunt(dateFin, Utils.produits.getItem(8));
            Utils.commandes.getItem(1).addEmprunt(dateFin, Utils.produits.getItem(8));
            Utils.commandes.getItem(1).addEmprunt(dateFin, Utils.produits.getItem(8));
            Utils.commandes.getItem(1).addEmprunt(dateFin, Utils.produits.getItem(8));
        }

        Utils.produits.setProdStock();
        initComponents();
    }

    private void setLookNFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            Utils.logStream.Error(e);
        } catch (ClassNotFoundException e) {
            Utils.logStream.Error(e);
        } catch (InstantiationException e) {
            Utils.logStream.Error(e);
        } catch (IllegalAccessException e) {
            Utils.logStream.Error(e);
        }
        Utils.logStream.Log("Look and feel loaded : " + UIManager.getSystemLookAndFeelClassName());
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        var mnu = new JMenuBar();
        
        var mnu_file = new JMenu("Fichier");
        
        mnui_save = new JMenuItem("Sauvegarder");
        mnui_save.setIcon(new ImageIcon(getClass().getResource(".\\icons\\save.png")));
        mnui_save.addActionListener(this);
        mnu_file.add(mnui_save);

        mnu.add(mnu_file);

        var mnu_edit = new JMenu("Edition");

        mnui_newUser = new JMenuItem("Nouveau client");
        mnui_newUser.setIcon(new ImageIcon(getClass().getResource(".\\icons\\addUser.png")));
        mnui_newUser.addActionListener(this);
        mnu_edit.add(mnui_newUser);

        mnui_newCommande = new JMenuItem("Nouvelle commande");
        mnui_newCommande.setIcon(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        mnui_newCommande.addActionListener(this);
        mnu_edit.add(mnui_newCommande);

        mnui_newProd = new JMenuItem("Nouveau produit");
        mnui_newProd.setIcon(new ImageIcon(getClass().getResource(".\\icons\\newProd.png")));
        mnui_newProd.addActionListener(this);
        mnu_edit.add(mnui_newProd);

        mnu_edit.addSeparator();

        mnui_settings = new JMenuItem("Paramètres");
        mnui_settings.setIcon(new ImageIcon(getClass().getResource(".\\icons\\settings.png")));
        mnui_settings.addActionListener(this);
        mnu_edit.add(mnui_settings);

        mnu.add(mnu_edit);

        setJMenuBar(mnu);
        
        var toolbar = new JToolBar(null, JToolBar.VERTICAL);
        toolbar.setFloatable(false);
        
        btn_toolbarNewUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\addUser.png")));
        btn_toolbarNewUser.setToolTipText("Ajouter un client");
        btn_toolbarNewUser.addActionListener(this);
        
        btn_toolbarNewCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_toolbarNewCommande.setToolTipText("Ajouter une commande");
        btn_toolbarNewCommande.addActionListener(this);
        
        btn_toolbarNewProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\newProd.png")));
        btn_toolbarNewProd.setToolTipText("Ajouter un produit");
        btn_toolbarNewProd.addActionListener(this);
        
        btn_toolbarSave = new JButton(new ImageIcon(getClass().getResource(".\\icons\\save.png")));
        btn_toolbarSave.setToolTipText("Sauvegarder");
        btn_toolbarSave.addActionListener(this);
        
        btn_toolbarSettings = new JButton(new ImageIcon(getClass().getResource(".\\icons\\settings.png")));
        btn_toolbarSettings.setToolTipText("Paramètres");
        btn_toolbarSettings.addActionListener(this);

        toolbar.add(btn_toolbarNewUser);
        toolbar.add(btn_toolbarNewCommande);
        toolbar.add(btn_toolbarNewProd);
        toolbar.addSeparator();
        toolbar.add(btn_toolbarSave);
        toolbar.add(btn_toolbarSettings);
        add(toolbar, BorderLayout.WEST);

        tab = new JTabbedPane();
        tab.addTab("Commandes", initComponentsCommandes());
        tab.addTab("Produits", initComponentsProduits());
        tab.addTab("Clients", initComponentsClients());

        add(tab, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(!Utils.settings.isLocal)
                    JOptionPane.showMessageDialog(null, "Savegarde dans la base de données, cela peut prendre un peu de temps...", "Sauvegardes", JOptionPane.INFORMATION_MESSAGE);
                Utils.save();
            }
        });
    }

    private JPanel initComponentsCommandes() {
        var pnl_commandesTab = new JPanel(new BorderLayout());

        var lbl_commandesTab = new JLabel("Liste des commandes", SwingConstants.CENTER);
        var attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.FAMILY, Font.DIALOG);
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attributes.put(TextAttribute.SIZE, 16);
        lbl_commandesTab.setFont(Font.getFont(attributes));

        var pnl_commandes = new JPanel(new BorderLayout());

        t_commandes = new JTable(Utils.commandes);
        var t_commandesSorter = new TableRowSorter<TableModel>(t_commandes.getModel());
        t_commandes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_commandesSorter.setSortsOnUpdates(true);
        t_commandes.setRowSorter(t_commandesSorter);
        t_commandes.getSelectionModel().addListSelectionListener(this);
        for (var i = 0; i < Utils.commandes.getColumnCount(); i++)
            t_commandes.getColumnModel().getColumn(i).setPreferredWidth(
                    IMyTableModel.columnSizeModifier[i] * t_commandes.getColumnModel().getColumn(i).getWidth());
        var pnl_commandesTable = new JPanel(new BorderLayout());
        pnl_commandesTable.add(new JScrollPane(t_commandes));

        pnl_commandes.add(pnl_commandesTable);

        var pnl_commandesbtns = new JPanel();
        pnl_commandesbtns.setLayout(new BoxLayout(pnl_commandesbtns, BoxLayout.PAGE_AXIS));
        btn_newCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\add.png")));
        btn_newCommande.setToolTipText("Ajouter une commande");
        btn_newCommande.addActionListener(this);
        btn_remCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\remove.png")));
        btn_remCommande.setToolTipText("Supprimer une commande");
        btn_remCommande.addActionListener(this);
        btn_remCommande.setEnabled(false);
        btn_editCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\edit.png")));
        btn_editCommande.setToolTipText("Modifier la commande");
        btn_editCommande.addActionListener(this);
        btn_editCommande.setEnabled(false);
        btn_infoCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\info.png")));
        btn_infoCommande.setToolTipText("Information sur la commande");
        btn_infoCommande.addActionListener(this);
        btn_infoCommande.setEnabled(false);
        btn_exportCommande = new JButton(new ImageIcon(getClass().getResource(".\\icons\\export.png")));
        btn_exportCommande.setToolTipText("Exporter la commande");
        btn_exportCommande.addActionListener(this);
        btn_exportCommande.setEnabled(false);

        pnl_commandesbtns.add(btn_newCommande);
        pnl_commandesbtns.add(btn_remCommande);
        pnl_commandesbtns.add(btn_editCommande);
        pnl_commandesbtns.add(btn_infoCommande);
        pnl_commandesbtns.add(btn_exportCommande);
        pnl_commandes.add(pnl_commandesbtns, BorderLayout.EAST);

        pnl_commandesTab.add(lbl_commandesTab, BorderLayout.NORTH);
        pnl_commandesTab.add(pnl_commandes, BorderLayout.CENTER);

        return pnl_commandesTab;
    }

    private JPanel initComponentsProduits() {
        var pnl_produitsTab = new JPanel(new BorderLayout());

        var lbl_prduitsTab = new JLabel("Liste des produits", SwingConstants.CENTER);
        var attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.FAMILY, Font.DIALOG);
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attributes.put(TextAttribute.SIZE, 16);
        lbl_prduitsTab.setFont(Font.getFont(attributes));

        var pnl_produits = new JPanel(new BorderLayout());

        t_produits = new JTable(Utils.produits);
        t_produits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        var t_produitsSorter = new TableRowSorter<TableModel>(t_produits.getModel());
        t_produitsSorter.setSortsOnUpdates(true);
        t_produits.setRowSorter(t_produitsSorter);
        t_produits.getSelectionModel().addListSelectionListener(this);
        for (var i = 0; i < Utils.produits.getColumnCount(); i++)
            t_produits.getColumnModel().getColumn(i).setPreferredWidth(
                    IMyTableModel.columnSizeModifier[i] * t_produits.getColumnModel().getColumn(i).getWidth());
        var pnl_produitTable = new JPanel(new BorderLayout());
        pnl_produitTable.add(new JScrollPane(t_produits));

        pnl_produits.add(pnl_produitTable, BorderLayout.CENTER);

        var pnl_prodbtns = new JPanel();
        pnl_prodbtns.setLayout(new BoxLayout(pnl_prodbtns, BoxLayout.PAGE_AXIS));
        btn_newProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\newProd.png")));
        btn_newProd.setToolTipText("Ajouter un produit");
        btn_newProd.addActionListener(this);
        btn_remProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\remProd.png")));
        btn_remProd.setToolTipText("Supprimer le produit");
        btn_remProd.addActionListener(this);
        btn_remProd.setEnabled(false);
        btn_editProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\editProd.png")));
        btn_editProd.setToolTipText("Modifier le produit");
        btn_editProd.addActionListener(this);
        btn_editProd.setEnabled(false);
        btn_infoProd = new JButton(new ImageIcon(getClass().getResource(".\\icons\\info.png")));
        btn_infoProd.setToolTipText("Plus d'informations à propos du produit");
        btn_infoProd.addActionListener(this);
        btn_infoProd.setEnabled(false);
        pnl_prodbtns.add(btn_newProd);
        pnl_prodbtns.add(btn_remProd);
        pnl_prodbtns.add(btn_editProd);
        pnl_prodbtns.add(btn_infoProd);

        pnl_produits.add(pnl_prodbtns, BorderLayout.EAST);
        pnl_produitsTab.add(lbl_prduitsTab, BorderLayout.NORTH);
        pnl_produitsTab.add(pnl_produits, BorderLayout.CENTER);

        return pnl_produitsTab;
    }

    private JPanel initComponentsClients() {
        var pnl_clientsTab = new JPanel(new BorderLayout());

        var lbl_clientsTab = new JLabel("Liste des clients", SwingConstants.CENTER);
        var attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.FAMILY, Font.DIALOG);
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attributes.put(TextAttribute.SIZE, 16);
        lbl_clientsTab.setFont(Font.getFont(attributes));

        var pnl_clients = new JPanel(new BorderLayout());
        l_clients = new JList<Client>(Utils.clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);

        var l_clientsScrollPane = new JScrollPane(l_clients);
        var pnl_clientsbtns = new JPanel();
        pnl_clientsbtns.setLayout(new BoxLayout(pnl_clientsbtns, BoxLayout.PAGE_AXIS));
        btn_newUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\addUser.png")));
        btn_newUser.setToolTipText("Ajouter un client");
        btn_newUser.addActionListener(this);
        btn_delUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\deleteUser.png")));
        btn_delUser.setToolTipText("Supprimer un client sélectionné");
        btn_delUser.addActionListener(this);
        btn_infoUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\info.png")));
        btn_infoUser.setToolTipText("Plus d'informations à propos du client");
        btn_infoUser.addActionListener(this);
        if (l_clients.getModel().getSize() == 0) {
            btn_delUser.setEnabled(false);
            btn_infoUser.setEnabled(false);
        } else {
            btn_delUser.setEnabled(true);
            btn_infoUser.setEnabled(true);
        }
        pnl_clientsbtns.add(btn_newUser);
        pnl_clientsbtns.add(btn_delUser);
        pnl_clientsbtns.add(btn_infoUser);

        pnl_clients.add(l_clientsScrollPane, BorderLayout.CENTER);
        pnl_clients.add(pnl_clientsbtns, BorderLayout.EAST);
        pnl_clientsTab.add(lbl_clientsTab, BorderLayout.NORTH);
        pnl_clientsTab.add(pnl_clients, BorderLayout.CENTER);

        return pnl_clientsTab;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_toolbarNewCommande || e.getSource() == btn_newCommande || e.getSource() == mnui_newCommande) {
            var commandeDialog = new CommandeDialog(this);
            commandeDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_infoCommande) {
            if (t_commandes.getSelectedRow() != -1) {
                new CommandeInfo(this, Utils.commandes.getItem(t_commandes.convertRowIndexToModel(t_commandes.getSelectedRow()))).setVisible(true);
            }
        } else if (e.getSource() == btn_remCommande) {
            if (t_commandes.getSelectedRow() != -1) {
                if (JOptionPane.showConfirmDialog(this, "Voulez vous vraiment supprimer la commande ?",
                        "Suppression commande - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    var commande = Utils.commandes.getItem(t_commandes.convertRowIndexToModel(t_commandes.getSelectedRow()));
                    Utils.logStream.Log("Order "+ commande.getId() +" removed");
                    if(!Utils.settings.isLocal)
                        try{
                            for (var empr : commande.getEmprunts()){
                                Utils.SQLupdate("DELETE FROM `emprunts` WHERE `emprunts`.`id-empr` = \""+ empr.getId() +"\"");
                            }
                            Utils.SQLupdate("DELETE FROM `commandes` WHERE `commandes`.`id-com` = \""+ commande.getId()  +"\"");
                        }
                        catch (SQLException ex){
                            Utils.logStream.Error(ex);
                        }
                    Utils.commandes.remove(t_commandes.convertRowIndexToModel(t_commandes.getSelectedRow()));
                }
                Utils.produits.setProdStock();
                t_produits.repaint();
            }
        } else if (e.getSource() == btn_editCommande) {
            if (t_commandes.getSelectedRow() != -1) {
                new CommandeDialog(this, Utils.commandes.getItem(t_commandes.convertRowIndexToModel(t_commandes.getSelectedRow()))).setVisible(true);
            }
        } else if (e.getSource() == btn_exportCommande) {
            var exportDialog = new ExportDialog(this, Utils.commandes.getItem(t_commandes.convertRowIndexToModel(t_commandes.getSelectedRow())));
            exportDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_toolbarNewProd || e.getSource() == btn_newProd || e.getSource() == mnui_newProd) {
            var ProduitDialog = new ProduitDialog(this);
            ProduitDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_remProd) {
            for (int i = 0; i < Utils.commandes.getRowCount(); i++)
                for (var emprunt : Utils.commandes.getItem(i).getEmprunts())
                    if (Utils.produits.getItem(t_produits.convertRowIndexToModel(t_produits.getSelectedRow())) == emprunt.getProduit()) {
                        JOptionPane.showMessageDialog(this, "Le produit est dans une commande en cours !", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
            if (t_produits.getSelectedRow() != -1)
                if (JOptionPane.showConfirmDialog(this, "Voulez vous vraiment supprimer le produit ?",
                        "Suppression produit - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Utils.logStream.Log("Product " + Utils.produits.getItem(t_produits.convertRowIndexToModel(t_produits.getSelectedRow())).getId() + "removed");
                    if(!Utils.settings.isLocal)
                        try{
                            Utils.SQLupdate("DELETE FROM `produits` WHERE `produits`.`id-prod` = \""+ Utils.produits.getItem(t_produits.convertRowIndexToModel(t_produits.getSelectedRow())).getId() +"\"");
                        }
                        catch (SQLException ex){
                            Utils.logStream.Error(ex);
                        }
                    Utils.produits.remove(t_produits.convertRowIndexToModel(t_produits.getSelectedRow()));
                }
        } else if (e.getSource() == btn_editProd) {
            if (t_produits.getSelectedRow() != -1) {
                var ProduitDialog = new ProduitDialog(this, Utils.produits.getItem(t_produits.convertRowIndexToModel(t_produits.getSelectedRow())));
                ProduitDialog.setVisible(true);
                this.setEnabled(false);    
            }
        } else if (e.getSource() == btn_infoProd) {
            if (t_produits.getSelectedRow() != -1) {
                new ProduitInfo(this, Utils.produits.getItem(t_produits.convertRowIndexToModel(t_produits.getSelectedRow()))).setVisible(true);
            }
        } else if (e.getSource() == btn_newUser || e.getSource() == btn_toolbarNewUser || e.getSource() == mnui_newUser) {
            var userDialog = new UserDialog(this);
            userDialog.setVisible(true);
            setEnabled(false);
        } else if (e.getSource() == btn_delUser) {
            for (int i = 0; i < Utils.commandes.getRowCount(); i++) {
                if (l_clients.getSelectedValue() == Utils.commandes.getValueAt(0, i)) {
                    JOptionPane.showMessageDialog(this, "L'utilisateur à une commande en cours !", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (JOptionPane.showConfirmDialog(this, "Voulez vous vraiment supprimer le client ?",
                    "Suppression client - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                Utils.logStream.Log("Client " + l_clients.getSelectedValue().getId() + " removed");
                if(!Utils.settings.isLocal)
                    try{
                        Utils.SQLupdate("DELETE FROM `clients` WHERE `clients`.`id-cli` = \""+ l_clients.getSelectedValue().getId() +"\"");
                    }
                    catch (SQLException ex){
                        Utils.logStream.Error(ex);
                    }
                Utils.clients.removeElement(l_clients.getSelectedValue());
            }

        } else if (e.getSource() == btn_infoUser) {
            new UserInfo(this, l_clients.getSelectedValue(), Utils.commandes).setVisible(true);
        } else if (e.getSource() == btn_toolbarSave || e.getSource() == mnui_save) {
            Utils.save();
        } else if (e.getSource() == btn_toolbarSettings || e.getSource() == mnui_settings) {
            var settingsDialog = new SettingsDialog(this);
            settingsDialog.setVisible(true);
            setEnabled(false);
        }
    }

    public void dialogReturn() {
        this.setEnabled(true);
        this.toFront();
        if (tab.getSelectedIndex() == 0)
            t_commandes.repaint();
        else if (tab.getSelectedIndex() == 1)
            t_produits.repaint();
    }

    public void commandeDialogReturn(Commande commande) {
        Utils.commandes.add(commande);
        if(!Utils.settings.isLocal)
            try{
                Utils.SQLupdate(String.format("INSERT INTO `commandes` (`id-com`, `id-cli`, `dateCreation`) VALUES (\"%s\",\"%s\",\"%s\")", commande.getId(), commande.getClient().getId(), commande.getDateCreation().get(Calendar.YEAR) + "/" + (commande.getDateCreation().get(Calendar.MONTH) + 1) + "/" +commande.getDateCreation().get(Calendar.DAY_OF_MONTH)));
                for (var emprunt : commande.getEmprunts()){
                    Utils.SQLupdate(String.format("INSERT INTO `emprunts` (`id-com`, `id-empr`, `dateFin`, `id-prod`) VALUE (\"%s\",\"%s\",\"%s\",\"%s\")", commande.getId(), emprunt.getId(), emprunt.getDateFin().get(Calendar.YEAR) + "/" + (emprunt.getDateFin().get(Calendar.MONTH) + 1) + "/" +emprunt.getDateFin().get(Calendar.DAY_OF_MONTH) , emprunt.getProduit().getId()));
                }
            }
            catch (SQLException e){
                Utils.logStream.Error(e);
            }
        dialogReturn();
        tab.setSelectedIndex(0);
        Utils.logStream.Log("Order " + commande.getId() + " added");
    }
    
    public void produitDialogReturn(Produit produit) {
        Utils.produits.add(produit);
        if(!Utils.settings.isLocal) {
            var types = new ArrayList<String>();
            try {
                for (var type : Utils.getTypes()) {
                    types.add(type.getValue0().getSimpleName().replaceAll("\\s+",""));
                } 
                Utils.SQLupdate(String.format("INSERT INTO `produits` (`id-prod`, `title`, `dailyPrice`, `quantity`, `option1`, `id-types`) VALUES (\"%s\", \"%s\", \""+produit.getDailyPrice()+"\", \"%d\", \"%s\", \"%d\")", produit.getId(), produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getSimpleName())));
            } catch (SQLIntegrityConstraintViolationException e) {
                try {
                    Utils.SQLupdate(String.format("INSERT INTO `types` (`id-types`, `categ`) VALUES (\"%d\",\"%s\")", types.indexOf(produit.getClass().getSimpleName()), produit.getClass().getSimpleName()));
                    Utils.SQLupdate(String.format("INSERT INTO `produits` (`id-prod`, `title`, `dailyPrice`, `quantity`, `option1`, `id-types`) VALUES (\"%s\", \"%s\", \""+produit.getDailyPrice()+"\", \"%d\", \"%s\", \"%d\")", produit.getId(), produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getSimpleName())));
                } catch (SQLException ex) {
                    Utils.logStream.Error(ex);
                }
            } catch (SQLException e) {
                Utils.logStream.Error(e);
            }
        }
        dialogReturn();
        tab.setSelectedIndex(1);
        Utils.logStream.Log("Product " + produit.getId() + " added");
    }
    
    public void userDialogReturn(Client client) {
        Utils.clients.addElement(client);
        if(!Utils.settings.isLocal)
            try {
                Utils.SQLupdate(String.format("INSERT INTO `clients` (`id-cli`, `nom`, `prenom`, `isFidel`) VALUES (\"%s\", \"%s\", \"%s\", \"%d\")", client.getId(), client.getNom(), client.getPrenom(), (client instanceof ClientFidele ? 1 : 0)));    
            } catch (SQLException e) {
                Utils.logStream.Error(e);
            }
        dialogReturn();
        Utils.logStream.Log("User " + client.getId() + " added");
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);
                btn_infoUser.setEnabled(false);
            } else {
                btn_delUser.setEnabled(true);
                btn_infoUser.setEnabled(true);
            }

            if (t_commandes.getSelectedRow() == -1) {
                btn_remCommande.setEnabled(false);
                btn_editCommande.setEnabled(false);
                btn_infoCommande.setEnabled(false);
                btn_exportCommande.setEnabled(false);
            } else {
                btn_remCommande.setEnabled(true);
                if (Utils.commandes.getItem(t_commandes.convertRowIndexToModel(t_commandes.getSelectedRow())).editable()) {
                    btn_editCommande.setEnabled(true);
                    btn_editCommande.setToolTipText("Modifier la commande");
                }
                else {
                    btn_editCommande.setEnabled(false);
                    btn_editCommande.setToolTipText("La commande n'est plus modifiable");
                }
                btn_infoCommande.setEnabled(true);
                btn_exportCommande.setEnabled(true);
            }
            
            if (t_produits.getSelectedRow() == -1) {
                btn_remProd.setEnabled(false);
                btn_editProd.setEnabled(false);
                btn_infoProd.setEnabled(false);
            } else {
                btn_remProd.setEnabled(true);
                btn_editProd.setEnabled(true);
                btn_infoProd.setEnabled(true);
            }
        }
    }
}
