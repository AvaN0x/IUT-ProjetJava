package gui;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import app.Commande;

@SuppressWarnings("serial")
public class ExportDialog extends JDialog implements ActionListener {
    private Commande commande;
    
    private JButton btn_valider;
    private JButton btn_cancel;

    public ExportDialog(Window owner, Commande commande){
        super(owner, "Gestion vidéothèque - Exporter la commande");
        setLocation(300, 200);
        setSize(200, 150);

        this.commande = commande;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // TODO exporter dans d'autres formats

        var pnl_validate = new Panel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_validate, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            // TODO exporter la commande mieux, autres formats
            try {
                var file = new File("bin/commande_" + commande.getId() + ".txt");
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdir();
                if (file.exists())
                    file.createNewFile();
                var writer = new PrintWriter(file.getPath());

                writer.println("Commande " + commande.getId());
                writer.println("Client " + commande.getClient().getNom() + " " + commande.getClient().getPrenom() + "\n");
                
                writer.println("Nombre d'emprunts : " + commande.getEmprunts().size());
                for (var emprunt : commande.getEmprunts()) {
                    writer.print(emprunt.getProduit().getTitle() + " - " + emprunt.getProduit().getOption1() + " - " + emprunt.getProduit().getClass().getSimpleName() + "\t| ");
                    writer.print(emprunt.getProduit().getDailyPrice() + "\t| ");
                    writer.println(Utils.dateToString(commande.getDateCreation()) + " -> " + Utils.dateToString(emprunt.getDateFin()));
                }
                writer.println("\nTotal HR: " + commande.getTotalCostNoReduc() + "€");
                writer.println("Réduction : " + commande.getReduction()*100 + "%");
                writer.println("Total  : " + commande.getTotalCost() + "€");

                writer.flush();
                Utils.logStream.Log("Order " + commande.getId() + " exported");
            } catch (FileNotFoundException ex) {
                Utils.logStream.Error(ex);
            } catch (IOException ex) {
                Utils.logStream.Error(ex);
            }

            // TODO fermer fenêtre export
        } else if (e.getSource() == btn_cancel) {
            // TODO annuler export
        }
    }
}
