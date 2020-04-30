package gui;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.time.Duration;

import javax.swing.*;

import app.Commande;

@SuppressWarnings("serial")
public class ExportDialog extends JDialog implements ActionListener {
    private Commande commande;

    private JComboBox<String> cbx_type;

    private JButton btn_valider;
    private JButton btn_cancel;

    public ExportDialog(Window owner, Commande commande) {
        super(owner, "Gestion vidéothèque - Exporter la commande");
        setLocation(300, 200);
        setSize(200, 100);

        this.commande = commande;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        var pnl_content = new JPanel();
        pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.PAGE_AXIS));

        cbx_type = new JComboBox<String>();
        cbx_type.addItem("txt file");
        cbx_type.addItem("csv file");

        pnl_content.add(cbx_type);

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_content, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                quit();
            }
        });

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            if (cbx_type.getSelectedIndex() == 1) // csv file
                exportToCSV();
            else // txt file
                exportToTXT();
            Utils.logStream.Log("Order " + commande.getId() + " exported");
            quit();
        } else if (e.getSource() == btn_cancel) {
            quit();
        }
    }

    private void exportToCSV() {
        try {
            var file = new File("bin/commande_" + commande.getId() + ".csv");
            Utils.createFileIfNotExists(file);
            var writer = new PrintWriter(file.getPath(), "Windows-1252"); //* Only with Excel & Windows

            writer.println("Commande;" + commande.getId());
            writer.println("Client;" + commande.getClient().getId());
            writer.println(";" + commande.getClient().getNom() + " " + commande.getClient().getPrenom() + "\n");

            writer.println("Nombre d'emprunts : " + commande.getEmprunts().size());
            for (var emprunt : commande.getEmprunts()) {
                writer.println(emprunt.getProduit().getTitle());
                writer.println(";" + emprunt.getProduit().getOption1());
                writer.println(";Type :;" + emprunt.getProduit().getClass().getSimpleName());
                writer.println(";Prix journalier :;" + emprunt.getProduit().getDailyPrice() + "€");
                writer.println(";Coût :;" + emprunt.getCost() + "€");
                writer.println(";" + Utils.dateToString(commande.getDateCreation()) + " -> "
                        + Utils.dateToString(emprunt.getDateFin()) + " ("
                        + Duration.between(commande.getDateCreation().toInstant(), emprunt.getDateFin().toInstant())
                                .toDays()
                        + " jours)");
            }
            writer.println("\nTotal HR:;" + commande.getTotalCostNoReduc() + "€");
            writer.println("Réduction :;" + commande.getReduction() * 100 + "%");
            writer.println("Total  :;" + commande.getTotalCost() + "€");

            writer.flush();
        } catch (FileNotFoundException ex) {
            Utils.logStream.Error(ex);
        } catch (IOException ex) {
            Utils.logStream.Error(ex);
        }
    }

    private void exportToTXT() {
        try {
            var file = new File("bin/commande_" + commande.getId() + ".txt");
            Utils.createFileIfNotExists(file);
            var writer = new PrintWriter(file.getPath());

            writer.println("Commande " + commande.getId());
            writer.println("Client   " + commande.getClient().getId());
            writer.println("\t\t " + commande.getClient().getNom() + " " + commande.getClient().getPrenom() + "\n");

            writer.println("Nombre d'emprunts : " + commande.getEmprunts().size());
            for (var emprunt : commande.getEmprunts()) {
                writer.println(emprunt.getProduit().getTitle());
                writer.println("\t " + emprunt.getProduit().getOption1());
                writer.println("\t Type : " + emprunt.getProduit().getClass().getSimpleName());
                writer.println("\t Prix journalier : " + emprunt.getProduit().getDailyPrice() + "€");
                writer.println("\t Coût : " + emprunt.getCost() + "€");
                writer.println("\t " + Utils.dateToString(commande.getDateCreation()) + " -> "
                        + Utils.dateToString(emprunt.getDateFin()) + " ("
                        + Duration.between(commande.getDateCreation().toInstant(), emprunt.getDateFin().toInstant())
                                .toDays()
                        + " jours)"); //
            }
            writer.println("\nTotal HR: " + commande.getTotalCostNoReduc() + "€");
            writer.println("Réduction : " + commande.getReduction() * 100 + "%");
            writer.println("Total  : " + commande.getTotalCost() + "€");

            writer.flush();
        } catch (FileNotFoundException ex) {
            Utils.logStream.Error(ex);
        } catch (IOException ex) {
            Utils.logStream.Error(ex);
        }
    }

    private void quit() {
        setVisible(false);
        var owner = (IMyDialogOwner) getOwner();
        owner.dialogReturn();
        this.dispose();
    }
}
