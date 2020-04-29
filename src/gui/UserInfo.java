package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import app.Client;
import app.ClientFidele;

@SuppressWarnings("serial")
public class UserInfo extends JDialog {
    private Client client;
    private TableauCommandes commandes;
    private JTable t_commandes;
    private TableRowSorter<TableModel> t_commandesSorter;


    public UserInfo(Window owner, Client client, TableauCommandes commandes) {
        super(owner, "Gestion vidéothèque - Information client");
        this.client = client;
        this.commandes = commandes;

        setLocation(300, 200);
        setSize(320, 180);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new Panel(new GridLayout(4,1));

        var pnl_nom = new Panel(new FlowLayout());
        var lbl_nomStatic = new JLabel("Nom :");
        var lbl_nom = new JLabel(client.getNom());
        pnl_nom.add(lbl_nomStatic);
        pnl_nom.add(lbl_nom);
        
        var pnl_prenom = new Panel(new FlowLayout());
        var lbl_prenomStatic = new JLabel("Prenom :");
        var lbl_prenom = new JLabel(client.getPrenom());
        pnl_prenom.add(lbl_prenomStatic);
        pnl_prenom.add(lbl_prenom);

        var pnl_id = new Panel(new FlowLayout());
        var lbl_idStatic = new JLabel("ID :");
        var lbl_id = new JLabel(client.getId());
        pnl_id.add(lbl_idStatic);
        pnl_id.add(lbl_id);

        var pnl_fidel = new Panel(new FlowLayout());
        var cb_fidel = new JCheckBox("Fidèle");
        cb_fidel.setEnabled(false);
        if (client instanceof ClientFidele)
            cb_fidel.setSelected(true);
        else
            cb_fidel.setSelected(false);
        pnl_fidel.add(cb_fidel);
        
        t_commandes = new JTable(commandes);
        t_commandesSorter = new TableRowSorter<TableModel>(t_commandes.getModel());
        t_commandesSorter.setSortsOnUpdates(true);
        t_commandes.setRowSorter(t_commandesSorter);
        for (var i = 0; i < commandes.getColumnCount(); i++)
            t_commandes.getColumnModel().getColumn(i).setPreferredWidth(
                    IMyTableModel.columnSizeModifier[i] * t_commandes.getColumnModel().getColumn(i).getWidth());
        var pnl_commandesTable = new JPanel(new BorderLayout());
        pnl_commandesTable.add(new JScrollPane(t_commandes));

        pnl_fields.add(pnl_nom);
        pnl_fields.add(pnl_prenom);
        pnl_fields.add(pnl_id);
        pnl_fields.add(pnl_fidel);
        pnl_fields.add(pnl_commandesTable);

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
