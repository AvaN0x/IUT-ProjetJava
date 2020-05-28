package gui;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.time.Duration;

import javax.swing.*;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.PdfWriter;

import app.Commande;

@SuppressWarnings("serial")
public class ExportDialog extends MyJDialog implements ActionListener {
    private Commande commande;

    private JComboBox<String> cbx_type;

    private JButton btn_valider;
    private JButton btn_cancel;

    public ExportDialog(Window owner, Commande commande) {
        super(owner, "Exporter la commande", new Dimension(200,100));

        this.commande = commande;
    }

    public void initComponents() {
        var pnl_content = new JPanel();
        pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.PAGE_AXIS));

        cbx_type = new JComboBox<String>();
        cbx_type.addItem("txt file");
        cbx_type.addItem("csv file");
        cbx_type.addItem("pdf file");

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
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            if (cbx_type.getSelectedIndex() == 0) // txt file
                exportToTXT();
            else if (cbx_type.getSelectedIndex() == 1) // csv file
                exportToCSV();
            else // pdf file
                exportToPDF();
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
                        + " jours)");
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

    private void exportToPDF() {
		Document document = new Document();
		try
		{
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("bin/commande_" + commande.getId() + ".pdf"));
			document.open();

            document.add(new Paragraph("Commande " + commande.getId()));
            document.add(new Paragraph("Client   " + commande.getClient().getId()));
            document.add(new Paragraph("\t\t " + commande.getClient().getNom() + " " + commande.getClient().getPrenom() + "\n"));

            document.add(new Paragraph("Nombre d'emprunts : " + commande.getEmprunts().size()));
            for (var emprunt : commande.getEmprunts()) {
                document.add(new Paragraph(emprunt.getProduit().getTitle()));
                document.add(new Paragraph("\t " + emprunt.getProduit().getOption1()));
                document.add(new Paragraph("\t Type : " + emprunt.getProduit().getClass().getSimpleName()));
                document.add(new Paragraph("\t Prix journalier : " + emprunt.getProduit().getDailyPrice() + "€"));
                document.add(new Paragraph("\t Coût : " + emprunt.getCost() + "€"));
                document.add(new Paragraph("\t " + Utils.dateToString(commande.getDateCreation()) + " -> "
                        + Utils.dateToString(emprunt.getDateFin()) + " ("
                        + Duration.between(commande.getDateCreation().toInstant(), emprunt.getDateFin().toInstant())
                                .toDays()
                        + " jours)"));
            }
            document.add(new Paragraph("\nTotal HR: " + commande.getTotalCostNoReduc() + "€"));
            document.add(new Paragraph("Réduction : " + commande.getReduction() * 100 + "%"));
            document.add(new Paragraph("Total  : " + commande.getTotalCost() + "€"));

			document.close();
			writer.close();
        } catch (FileNotFoundException ex) {
            Utils.logStream.Error(ex);
        } catch (DocumentException ex) {
            Utils.logStream.Error(ex);
        }
    }
}
