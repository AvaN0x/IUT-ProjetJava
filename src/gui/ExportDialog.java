package gui;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.time.Duration;

import javax.swing.*;

import app.Commande;

@SuppressWarnings("serial")
public class ExportDialog extends JDialog implements ActionListener, ItemListener {
    private Commande commande;
    
    private JComboBox<String> cbx_type;

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

        var pnl_content = new JPanel();
        pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.PAGE_AXIS));
        
        cbx_type = new JComboBox<String>();
        cbx_type.addItem("txt file");
        cbx_type.addItem("cxv file");
        cbx_type.addItem("pdf file");
        cbx_type.addItemListener(this);

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
                writer.println("Client   " + commande.getClient().getId());
                writer.println("\t\t " + commande.getClient().getNom() + " " + commande.getClient().getPrenom() + "\n");
                
                writer.println("Nombre d'emprunts : " + commande.getEmprunts().size());
                for (var emprunt : commande.getEmprunts()) {
                    writer.println(emprunt.getProduit().getTitle());
                    writer.println("\t " + emprunt.getProduit().getOption1());
                    writer.println("\t Type : " + emprunt.getProduit().getClass().getSimpleName());
                    writer.println("\t Prix journalier : " + emprunt.getProduit().getDailyPrice() + "€");
                    writer.println("\t Coût : " + emprunt.getCost() + "€");
                    writer.println("\t " + Utils.dateToString(commande.getDateCreation()) + " -> " + Utils.dateToString(emprunt.getDateFin()) + " (" + Duration.between(commande.getDateCreation().toInstant(), emprunt.getDateFin().toInstant()).toDays() + " jours)"); // TODO afficher nombre de jours
                }
                writer.println("\nTotal HR: " + commande.getTotalCostNoReduc() + "€");
                writer.println("Réduction : " + commande.getReduction() * 100 + "%");
                writer.println("Total  : " + commande.getTotalCost() + "€");

                writer.flush();
                Utils.logStream.Log("Order " + commande.getId() + " exported");
            } catch (FileNotFoundException ex) {
                Utils.logStream.Error(ex);
            } catch (IOException ex) {
                Utils.logStream.Error(ex);
            }

            quit();
        } else if (e.getSource() == btn_cancel) {
            quit();
        }
    }

    public void itemStateChanged(ItemEvent e){

    }

    private void quit(){
        setVisible(false);
        var owner = (IMyDialogOwner) getOwner();
        owner.dialogReturn();
        this.dispose();
    }
}
