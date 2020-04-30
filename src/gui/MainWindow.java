package gui;

import java.util.List;
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

import app.*;

@SuppressWarnings("serial") //! https://stackoverflow.com/a/509230/13257820
public class MainWindow extends JFrame implements ActionListener, ListSelectionListener, IMyUserDialogOwner {
    protected DefaultListModel<Client> clients;
    protected TableauProduits produits;
    protected TableauCommandes commandes;

    private JMenuItem mnui_save;
    private JMenuItem mnui_newUser;
    private JMenuItem mnui_newCommande;
    private JMenuItem mnui_newProd;

    private JTabbedPane tab;
    private JButton btn_toolbarNewUser;
    private JButton btn_toolbarNewCommande;
    private JButton btn_toolbarNewProd;
    private JButton btn_toolbarSave;

    private JButton btn_newUser;
    private JButton btn_delUser;
    private JButton btn_infoUser;
    private JList<Client> l_clients;

    private JTable t_produits;
    private TableRowSorter<TableModel> t_produitsSorter;
    private JButton btn_newProd;
    private JButton btn_remProd;
    private JButton btn_editProd;
    private JButton btn_infoProd;

    private JTable t_commandes;
    private TableRowSorter<TableModel> t_commandesSorter;
    private JButton btn_newCommande;
    private JButton btn_remCommande;
    private JButton btn_editCommande;
    private JButton btn_infoCommande;

    @SuppressWarnings("unchecked")
    public MainWindow() {
        super("Gestion vidéothèque");
        setLookNFeel();
        setLocation(300, 200);
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clients = new DefaultListModel<Client>();
        produits = new TableauProduits();
        commandes = new TableauCommandes();

        if(new File(Utils.savingDir + "data.ser").exists()){
            try{
                InputStream fileStream = new FileInputStream(new File(Utils.savingDir + "data.ser"));
                var input = new ObjectInputStream(fileStream);

                commandes.setList((List<Commande>) input.readObject());

                produits.setList((List<Produit>) input.readObject());

                clients = (DefaultListModel<Client>) input.readObject();

                input.close();
                Utils.logStream.Log("Data loaded");
            } catch (IOException ex) {
                Utils.logStream.Error(ex);
            } catch (ClassNotFoundException ex) {
                Utils.logStream.Error(ex);
            }
        }
        else{/*
            clients.addElement(new ClientFidele("ricatte", "clément"));
            clients.addElement(new ClientFidele("sublet", "tom"));
            clients.addElement(new ClientOccas("hochet", "ric"));
            clients.addElement(new ClientFidele("térieur", "alex"));
            clients.addElement(new ClientOccas("térieur", "alain"));

            produits.add(new Roman("Harry Potter à l'école des sorciers", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et la chambre des secrets", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et le Prisonnier d'Azkaban", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et la Coupe de feu", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et l'Ordre du phénix", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et le Prince de Sang-Mêlé", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et les Reliques de la Mort", 0.99, 3, "J. K. Rowling"));
            produits.add(new Roman("Harry Potter et l'enfant maudit", 0.99, 4, "J. K. Rowling"));
            produits.add(new Dictionnaire("LAROUSSE", .65, 5, "FR"));
            produits.add(new ManuelScolaire("Objectif BAC Term S - BAC 2020", .99, 2, "hachette"));
            produits.add(new DVD("Le voyage de Chihiro", 1.99, 1, "Hayao Miyazaki"));
            produits.add(new BD("Les Simpson - Camping en délire", .75, 2, "Jungle!"));
            produits.add(new BD("Les Simpson - Un sacré foin !", .75, 1, "Jungle!"));
            var dateAdibou = Calendar.getInstance();
            dateAdibou.set(2003, 9, 24);
            produits.add(new CD("Adibou & le Secret de Paziral", .1, 1, dateAdibou));

            Calendar dateCreation = Calendar.getInstance();
            dateCreation.set(2020, Calendar.APRIL, 26);
            commandes.add(new Commande(clients.get(0), dateCreation));
            Calendar dateFin = Calendar.getInstance();
            dateFin.set(2020, Calendar.JUNE, 26);
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(0));
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(1));
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(2));
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(3));
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(4));
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(5));
            commandes.getItem(0).addEmprunt(dateFin, produits.getItem(6));*/
        }


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

        toolbar.add(btn_toolbarNewUser);
        toolbar.add(btn_toolbarNewCommande);
        toolbar.add(btn_toolbarNewProd);
        toolbar.addSeparator();
        toolbar.add(btn_toolbarSave);
        add(toolbar, BorderLayout.WEST);

        tab = new JTabbedPane();
        tab.addTab("Commandes", initComponentsCommandes());
        tab.addTab("Produits", initComponentsProduits());
        tab.addTab("Clients", initComponentsClients());

        add(tab, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                save();
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

        t_commandes = new JTable(commandes);
        t_commandesSorter = new TableRowSorter<TableModel>(t_commandes.getModel());
        t_commandesSorter.setSortsOnUpdates(true);
        t_commandes.setRowSorter(t_commandesSorter);
        t_commandes.getSelectionModel().addListSelectionListener(this);
        for (var i = 0; i < commandes.getColumnCount(); i++)
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

        pnl_commandesbtns.add(btn_newCommande);
        pnl_commandesbtns.add(btn_remCommande);
        pnl_commandesbtns.add(btn_editCommande);
        pnl_commandesbtns.add(btn_infoCommande);
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

        t_produits = new JTable(produits);
        t_produitsSorter = new TableRowSorter<TableModel>(t_produits.getModel());
        t_produitsSorter.setSortsOnUpdates(true);
        t_produits.setRowSorter(t_produitsSorter);
        t_produits.getSelectionModel().addListSelectionListener(this);
        for (var i = 0; i < produits.getColumnCount(); i++)
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
        l_clients = new JList<Client>(clients);
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
                new CommandeInfo(this, commandes.getItem(t_commandes.getSelectedRow())).setVisible(true);
            }
        } else if (e.getSource() == btn_remCommande) {
            if (t_commandes.getSelectedRow() != -1) {
                if (JOptionPane.showConfirmDialog(this, "Voulez vous vraiment supprimer la commande ?",
                        "Suppression commande - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    for (Emprunt emprunt : commandes.getItem(t_commandes.getSelectedRow()).getEmprunts()) {
                        emprunt.getProduit().rendre();
                    }
                    Utils.logStream.Log("Order "+ commandes.getItem(t_commandes.getSelectedRow()).getId() +" removed");
                    commandes.remove(t_commandes.getSelectedRow());
                }
            }
        } else if (e.getSource() == btn_editCommande) {
            if (t_commandes.getSelectedRow() != -1) {
                new CommandeDialog(this, commandes.getItem(t_commandes.getSelectedRow())).setVisible(true);
            }

        } else if (e.getSource() == btn_toolbarNewProd || e.getSource() == btn_newProd || e.getSource() == mnui_newProd) {
            var ProduitDialog = new ProduitDialog(this);
            ProduitDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_remProd) {
            for (int i = 0; i < commandes.getRowCount(); i++)
                for (var emprunt : commandes.getItem(i).getEmprunts())
                    if (produits.getItem(t_produits.getSelectedRow()) == emprunt.getProduit()) {
                        JOptionPane.showMessageDialog(this, "Le produit est dans une commande en cours !", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
            if (t_produits.getSelectedRow() != -1)
                if (JOptionPane.showConfirmDialog(this, "Voulez vous vraiment supprimer le produit ?",
                        "Suppression produit - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    Utils.logStream.Log("Product " + produits.getItem(t_produits.getSelectedRow()) + "removed");
                    produits.remove(t_produits.getSelectedRow());
                }
        } else if (e.getSource() == btn_editProd) {
            if (t_produits.getSelectedRow() != -1) {
                var ProduitDialog = new ProduitDialog(this, produits.getItem(t_produits.getSelectedRow()));
                ProduitDialog.setVisible(true);
                this.setEnabled(false);    
            }
        } else if (e.getSource() == btn_infoProd) {
            if (t_produits.getSelectedRow() != -1) {
                new ProduitInfo(this, produits.getItem(t_produits.getSelectedRow())).setVisible(true);
            }
        } else if (e.getSource() == btn_newUser || e.getSource() == btn_toolbarNewUser || e.getSource() == mnui_newUser) {
            var userDialog = new UserDialog(this);
            userDialog.setVisible(true);
            setEnabled(false);
        } else if (e.getSource() == btn_delUser) {
            for (int i = 0; i < commandes.getRowCount(); i++) {
                if (l_clients.getSelectedValue() == commandes.getValueAt(0, i)) {
                    JOptionPane.showMessageDialog(this, "L'utilisateur à une commande en cours !", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (JOptionPane.showConfirmDialog(this, "Voulez vous vraiment supprimer le client ?",
                    "Suppression client - Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                Utils.logStream.Log("Client " + l_clients.getSelectedValue().getId() + " removed");
                clients.removeElement(l_clients.getSelectedValue());
            }

        } else if (e.getSource() == btn_infoUser) {
            new UserInfo(this, l_clients.getSelectedValue(), commandes).setVisible(true);
        } else if (e.getSource() == btn_toolbarSave || e.getSource() == mnui_save) {
            save();
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
        commandes.add(commande);
        dialogReturn();
        tab.setSelectedIndex(0);
        Utils.logStream.Log("Order " + commande.getId() + " added");
    }
    
    public void produitDialogReturn(Produit produit) {
        produits.add(produit);
        dialogReturn();
        tab.setSelectedIndex(1);
        Utils.logStream.Log("Product " + produit.getId() + " added");
    }
    
    public void userDialogReturn(Client client) {
        clients.addElement(client);
        dialogReturn();
        Utils.logStream.Log("User " + client.getId() + "added");
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
            } else {
                btn_remCommande.setEnabled(true);
                btn_editCommande.setEnabled(true);
                btn_infoCommande.setEnabled(true);
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

    public void save(){
        try{
            var saveFile = new File(Utils.savingDir + "data.ser");
            if (!new File(Utils.savingDir).exists())
                new File(Utils.savingDir).mkdir();
            if (!saveFile.exists())
                saveFile.createNewFile();
            OutputStream fileStream = new FileOutputStream(saveFile);
            var output = new ObjectOutputStream(fileStream);

            output.writeObject(commandes.getList());
            output.writeObject(produits.getList());
            output.writeObject(clients);

            output.close();
            Utils.logStream.Log("Data saved");
        } catch (IOException ex) {
            Utils.logStream.Error(ex);
        }
    }
}
