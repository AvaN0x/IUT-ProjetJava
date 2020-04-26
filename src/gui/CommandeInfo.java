package gui;

import java.awt.*;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import app.Commande;

public class CommandeInfo extends JDialog /*implements ActionListener*/ {
    private TableauEmprunts emprunts;
    private JTable t_emprunts;
    private TableRowSorter<TableModel> t_empruntsSorter;

    Commande commande;

    public CommandeInfo(Window owner, Commande commande) {
        super(owner, "Gestion vidéothèque - Information commande");
        this.commande = commande;
        
        setLocation(300, 200);
        setSize(400, 320);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new Panel(new GridLayout(6,1));

        var pnl_client = new Panel(new FlowLayout());
        var lbl_clientStatic = new JLabel("Client :");
        var lbl_client = new JLabel(commande.getClient().toString());
        pnl_client.add(lbl_clientStatic);
        pnl_client.add(lbl_client);
        
        var pnl_id = new Panel(new FlowLayout());
        var lbl_idStatic = new JLabel("ID :");
        var lbl_id = new JLabel(commande.getId());
        pnl_id.add(lbl_idStatic);
        pnl_id.add(lbl_id);

        var pnl_dateCreation = new Panel(new FlowLayout());
        var lbl_dateCreationStatic = new JLabel("Date de création :");
        var dateCreation = commande.getDateCreation();
        var lbl_dateCreation = new JLabel(((dateCreation.get(Calendar.DAY_OF_MONTH) > 9) ? dateCreation.get(Calendar.DAY_OF_MONTH) : ("0" + dateCreation.get(Calendar.DAY_OF_MONTH))) + 
            "/" + ((dateCreation.get(Calendar.MONTH) > 8) ? (dateCreation.get(Calendar.MONTH) + 1) : ("0" + (dateCreation.get(Calendar.MONTH) + 1))) + 
            "/" + dateCreation.get(Calendar.YEAR));
        pnl_dateCreation.add(lbl_dateCreationStatic);
        pnl_dateCreation.add(lbl_dateCreation);

        var pnl_reduc = new Panel(new FlowLayout());
        var lbl_reducStatic = new JLabel("Reduction :");
        var lbl_reduc = new JLabel(Double.toString(commande.getReduction() * 100) + " %");
        pnl_reduc.add(lbl_reducStatic);
        pnl_reduc.add(lbl_reduc);

        var pnl_cout = new Panel(new FlowLayout());
        var lbl_coutStatic = new JLabel("Cout total :");
        var lbl_cout = new JLabel(Double.toString(commande.getTotalCostNoReduc()) + " €");
        pnl_cout.add(lbl_coutStatic);
        pnl_cout.add(lbl_cout);

        pnl_fields.add(pnl_client);
        pnl_fields.add(pnl_id);
        pnl_fields.add(pnl_dateCreation);
        pnl_fields.add(pnl_reduc);
        pnl_fields.add(pnl_cout);

        if (commande.getReduction() != 0) {
            var pnl_coutReduc = new Panel(new FlowLayout());
            var lbl_coutReducStatic = new JLabel("Cout après reduction :");
            var lbl_coutReduc = new JLabel(Double.toString(commande.getTotalCost()) + " €");
            pnl_coutReduc.add(lbl_coutReducStatic);
            pnl_coutReduc.add(lbl_coutReduc);
    
            pnl_fields.add(pnl_coutReduc);    
        }

        emprunts = new TableauEmprunts(commande.getEmprunts());
        t_emprunts = new JTable(emprunts);
        t_empruntsSorter = new TableRowSorter<TableModel>(t_emprunts.getModel());
        t_empruntsSorter.setSortsOnUpdates(true);
        t_emprunts.setRowSorter(t_empruntsSorter);

        // TODO pouvoir supprimer ou ajouter des emprunts a la commande

        add(pnl_fields, BorderLayout.NORTH);
        add(new JScrollPane(t_emprunts), BorderLayout.CENTER);


        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                dispose();
            }
        });
    }
}