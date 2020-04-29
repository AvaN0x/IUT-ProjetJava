package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import app.Client;
import app.Commande;
import app.Emprunt;

@SuppressWarnings("serial")
public class CommandeDialog extends JDialog implements ActionListener, ListSelectionListener, IMyUserDialogOwner {
    private boolean dateCreationValid;
    private Calendar dateCreation;
    private TableauEmprunts emprunts;
    private Commande commande;

    private JList<Client> l_clients;
    private JButton btn_newUser;
    private JButton btn_delUser;
    private JButton btn_infoUser;
    private JLabel lbl_dateCreation;
    private JLabel lbl_dateCreationWarn;
    private JTextField tf_dateCreation;
    private JTable t_produitsDispo;
    private TableRowSorter<TableModel> t_produitsDispoSorter;
    private JButton btn_prodDispo;
    private JButton btn_prodComm;
    private JTable t_emprunts;
    private TableRowSorter<TableModel> t_empruntsSorter;
    private JButton btn_valider;
    private JButton btn_cancel;
    private JButton btn_edit;

    public CommandeDialog(Window owner) {
        super(owner, "Gestion vidéothèque - Nouvelle commande");
        setLocation(300, 200);
        setSize(1100, 625);

        dateCreation = Calendar.getInstance();
        dateCreation.set(Calendar.MILLISECOND, 0);
        dateCreation.set(Calendar.SECOND, 0);
        dateCreation.set(Calendar.MINUTE, 0);
        dateCreation.set(Calendar.HOUR_OF_DAY, 0);

        initComponents();

        this.commande = null;
    }

    public CommandeDialog(Window owner, Commande commande) {
        super(owner, "Gestion vidéothèque - Modification commande");
        setLocation(300, 200);
        setSize(1100, 625);
        
        this.commande = commande;
        dateCreation = commande.getDateCreation();

        initComponents();

        for (int i = 0; i < l_clients.getModel().getSize(); i++) {
            if (commande.getClient().getId() == l_clients.getModel().getElementAt(i).getId()) {
                l_clients.setSelectedIndex(i);
                break;
            }
        }
        for (Emprunt emprunt : commande.getEmprunts()) {
            emprunts.add(new Emprunt(dateCreation, emprunt.getDateFin(), emprunt.getProduit()));
        }
        
        checkBtnValider();
    }


    private void initComponents() {
        var owner = (MainWindow) getOwner();
        setLayout(new BorderLayout());

        var pnl_dateclient = new JPanel(new FlowLayout());
        var pnl_clients = new JPanel(new FlowLayout());

        l_clients = new JList<Client>(owner.clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);
        var l_clientsScrollPane = new JScrollPane(l_clients);

        var pnl_clientsbtns = new JPanel(new GridLayout(3, 1));
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

        pnl_clients.add(l_clientsScrollPane);
        pnl_clients.add(pnl_clientsbtns);

        var pnl_dateCreation = new JPanel(new GridLayout(2, 1));
        var pnl_dateCreationSelect = new Panel(new FlowLayout());
        lbl_dateCreation = new JLabel("Date de création : ");
        tf_dateCreation = new JTextField(10);
        var defDate = new int[] { dateCreation.get(Calendar.DATE), (dateCreation.get(Calendar.MONTH) + 1), dateCreation.get(Calendar.YEAR) };
        tf_dateCreation.setText(
                  (defDate[0] < 10 ? "0" + defDate[0] : defDate[0]) + "/"
                + (defDate[1] < 10 ? "0" + defDate[1] : defDate[1]) + "/"
                + defDate[2]);
        dateCreationValid = true;
        lbl_dateCreationWarn = new JLabel("");
        lbl_dateCreationWarn.setForeground(Color.RED);
        tf_dateCreation.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void insertUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                Pattern regex = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}");
                Matcher m = regex.matcher(tf_dateCreation.getText());
                if (m.matches()) {
                    String dateValue = tf_dateCreation.getText();
                    String[] dateValueTab = dateValue.split("/");

                    int dateCreationDay = Integer.parseInt(dateValueTab[0]);
                    int dateCreationMonth = Integer.parseInt(dateValueTab[1]) - 1;
                    int dateCreationYear = Integer.parseInt(dateValueTab[2]);

                    if (dateCreationYear >= 1970 && dateCreationMonth >= 0 && dateCreationMonth <= 11) {
                        dateCreation.set(Calendar.MONTH, dateCreationMonth);
                        dateCreation.set(Calendar.YEAR, dateCreationYear);
                        if (dateCreationDay >= dateCreation.getActualMinimum(Calendar.DAY_OF_MONTH)
                                && dateCreationDay <= dateCreation.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            dateCreation.set(Calendar.DAY_OF_MONTH, dateCreationDay);
                            lbl_dateCreationWarn.setText("");
                            dateCreationValid = true;
                            t_emprunts.repaint();
                            checkBtnValider();
                        } else
                            notValid();
                    } else
                        notValid();
                } else
                    notValid();
            }

            private void notValid() {
                lbl_dateCreationWarn.setText("Date non valide (dd/mm/yyyy)");
                dateCreationValid = false;
            }
        });

        pnl_dateCreationSelect.add(lbl_dateCreation);
        pnl_dateCreationSelect.add(tf_dateCreation);

        pnl_dateCreation.add(pnl_dateCreationSelect);
        pnl_dateCreation.add(lbl_dateCreationWarn);

        pnl_dateclient.add(pnl_dateCreation);
        pnl_dateclient.add(pnl_clients);

        var pnl_produitTables = new JPanel(new FlowLayout());

        emprunts = new TableauEmprunts();
        t_emprunts = new JTable(emprunts);
        t_empruntsSorter = new TableRowSorter<TableModel>(t_emprunts.getModel());
        t_empruntsSorter.setSortsOnUpdates(true);
        t_emprunts.setRowSorter(t_empruntsSorter);

        var pnl_produitsBtns = new JPanel();
        pnl_produitsBtns.setLayout(new BoxLayout(pnl_produitsBtns, BoxLayout.PAGE_AXIS));

        btn_prodComm = new JButton(new ImageIcon(getClass().getResource(".\\icons\\left.png")));
        btn_prodComm.setToolTipText("Ajouter un produit à la commande");
        btn_prodComm.addActionListener(this);

        btn_prodDispo = new JButton(new ImageIcon(getClass().getResource(".\\icons\\right.png")));
        btn_prodDispo.setToolTipText("Retire un produit de la commande");
        btn_prodDispo.addActionListener(this);

        btn_edit = new JButton(new ImageIcon(getClass().getResource(".\\icons\\edit.png")));
        btn_edit.setToolTipText("Edite un produit de la commande");
        btn_edit.addActionListener(this);

        pnl_produitsBtns.add(btn_prodComm);
        pnl_produitsBtns.add(btn_prodDispo);
        pnl_produitsBtns.add(btn_edit);

        t_produitsDispo = new JTable(owner.produits);
        t_produitsDispoSorter = new TableRowSorter<TableModel>(t_produitsDispo.getModel());
        t_produitsDispoSorter.setSortsOnUpdates(true);
        t_produitsDispo.setRowSorter(t_produitsDispoSorter);

        pnl_produitTables.add(new JScrollPane(t_emprunts));
        pnl_produitTables.add(pnl_produitsBtns);
        pnl_produitTables.add(new JScrollPane(t_produitsDispo));

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_valider.setEnabled(false);
        btn_cancel = new JButton("Annuler");
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_dateclient, BorderLayout.NORTH);
        add(pnl_produitTables, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                quit();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_newUser) {
            var userDialog = new UserDialog(this);
            userDialog.setVisible(true);
            setEnabled(false);        
        } else if (e.getSource() == btn_delUser) {
            var owner = (MainWindow) getOwner();
            owner.clients.removeElement(l_clients.getSelectedValue());
        } else if (e.getSource() == btn_infoUser) {
            var owner = (MainWindow) getOwner();
            new UserInfo(this, l_clients.getSelectedValue(), owner.commandes).setVisible(true);
        } else if (e.getSource() == btn_prodComm) {
            if (t_produitsDispo.getSelectedRow() != -1) {
                var owner = (MainWindow) getOwner();
                // TODO gerer en fonction du stock dispo
                // TODO gérer localement l'ajout et suppression au stock, pour eviter des erreurs en cas de fermeture de fenetre (gestionnaire de taches > fin de tache)
                new EmpruntDialog(this, owner.produits.getItem(t_produitsDispo.getSelectedRow()), dateCreation).setVisible(true);;
            }    
        } else if (e.getSource() == btn_prodDispo) {
            if (t_emprunts.getSelectedRow() != -1) {
                emprunts.remove(t_emprunts.getSelectedRow());
                checkBtnValider();
            }
        } else if (e.getSource() == btn_edit) {
            // edit de produit
        } else if (e.getSource() == btn_valider) {
            if (commande == null) { // Nouvelle commande
                commande = new Commande(l_clients.getSelectedValue(), dateCreation);
                for (Emprunt emprunt : emprunts.getList())
                    commande.addEmprunt(emprunt.getDateFin(), emprunt.getProduit());  

                setVisible(false);
                var owner = (MainWindow) getOwner();
                owner.commandeDialogReturn(commande);
                dispose();
            } else { // Edit de commande
                commande.setClient(l_clients.getSelectedValue());
                commande.setDateCreation(dateCreation);
                commande.emptyEmprunts();
                for (Emprunt emprunt : emprunts.getList()) {
                    commande.addEmprunt(emprunt.getDateFin(), emprunt.getProduit());    
                }
    
                quit();
            }
        } else if (e.getSource() == btn_cancel) {
            quit();
        } 
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);
                btn_infoUser.setEnabled(false);
                checkBtnValider();
            } else {
                btn_delUser.setEnabled(true);
                btn_infoUser.setEnabled(true);
                checkBtnValider();
            }
        }
    }

    public void checkBtnValider() {
        if (l_clients.getSelectedIndex() != -1 && dateCreationValid && emprunts.getRowCount() > 0)
            btn_valider.setEnabled(true);
        else
            btn_valider.setEnabled(false);

    }

    private void quit() {
        setVisible(false);
        var owner = (MainWindow) getOwner();
        owner.dialogReturn();
        dispose();
    }

    public void dialogReturn() {
        this.setEnabled(true);
        this.toFront();
        checkBtnValider();
    }

    public void userDialogReturn(Client client) {
        var owner = (MainWindow) getOwner();
        owner.clients.addElement(client);
        dialogReturn();
    }

    public void empruntDialogReturn(Emprunt emprunt) {
        emprunts.add(emprunt);
        dialogReturn();
    }

}
