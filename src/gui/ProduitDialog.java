package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import app.*;

@SuppressWarnings("serial")
public class ProduitDialog extends JDialog implements ActionListener, ItemListener {
    private JLabel lbl_title;
    private JLabel lbl_price;
    private JLabel lbl_quantity;
    private JLabel lbl_option1;
    private JTextField tf_title;
    private JTextField tf_price;
    private JTextField tf_quantity;
    private JTextField tf_option1;
    private JComboBox<String> cbx_type;
    private JButton btn_valider;
    private JButton btn_cancel;

    public ProduitDialog(Window owner) {
        super(owner, "Gestion vidéothèque - Nouveau produit");
        setLocation(300, 200);
        setSize(240, 210);

        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        var pnl_fields = new JPanel(new GridLayout(5, 1));

        cbx_type = new JComboBox<String>();
        for (var item : Utils.produits) {
            cbx_type.addItem(item[0]);
        }
        cbx_type.addItemListener(this);
        pnl_fields.add(cbx_type);


        var pnl_title = new JPanel(new FlowLayout());
        lbl_title = new JLabel("Titre :");
        tf_title = new JTextField(20);
        pnl_title.add(lbl_title);
        pnl_title.add(tf_title);
        pnl_fields.add(pnl_title);

        var pnl_price = new JPanel(new FlowLayout());
        lbl_price = new JLabel("Prix / jour :");
        tf_price = new JTextField(5);
        pnl_price.add(lbl_price);
        pnl_price.add(tf_price);
        pnl_fields.add(pnl_price);

        var pnl_quantity = new JPanel(new FlowLayout());
        lbl_quantity = new JLabel("Quantitée :");
        tf_quantity = new JTextField(3);
        pnl_quantity.add(lbl_quantity);
        pnl_quantity.add(tf_quantity);
        pnl_fields.add(pnl_quantity);

        var pnl_optionfields = new JPanel(new GridLayout(1, 1));
        var pnl_option1 = new JPanel(new FlowLayout());
        lbl_option1 = new JLabel(Utils.produits[cbx_type.getSelectedIndex()][1] + " :");
        tf_option1 = new JTextField(10);
        pnl_option1.add(lbl_option1);
        pnl_option1.add(tf_option1);
        pnl_optionfields.add(pnl_option1);
        pnl_fields.add(pnl_optionfields);

        var pnl_validate = new Panel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_fields, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                var owner = (MainWindow) getOwner();
                owner.dialogReturn();
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            if(tf_title.getText().trim().length() <= 0 ||
               tf_price.getText().trim().length() <= 0 ||
               tf_quantity.getText().trim().length() <= 0 ||
               tf_option1.getText().trim().length() <= 0)
            {
                JOptionPane.showMessageDialog(this, "L'un des champs est vide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Calendar releaseDate = Calendar.getInstance();
            releaseDate.set(Calendar.MILLISECOND, 0);
            releaseDate.set(Calendar.SECOND, 0);
            releaseDate.set(Calendar.MINUTE, 0);
            releaseDate.set(Calendar.HOUR_OF_DAY, 0);
    
            if (cbx_type.getSelectedIndex() == 4) { // It's a CD
                Pattern regex = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}");
                Matcher m = regex.matcher(tf_option1.getText());
                if (m.matches()) {
                    String dateValue = tf_option1.getText();
                    String[] dateValueTab = dateValue.split("/");

                    int releaseDateDay = Integer.parseInt(dateValueTab[0]);
                    int releaseDateMonth = Integer.parseInt(dateValueTab[1]) - 1;
                    int releaseDateYear = Integer.parseInt(dateValueTab[2]);

                    if (releaseDateYear >= 1970 && releaseDateMonth >= 0 && releaseDateMonth <= 11) {
                        releaseDate.set(Calendar.MONTH, releaseDateMonth);
                        releaseDate.set(Calendar.YEAR, releaseDateYear);
                        if (releaseDateDay >= releaseDate.getActualMinimum(Calendar.DAY_OF_MONTH)
                                && releaseDateDay <= releaseDate.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            releaseDate.set(Calendar.DAY_OF_MONTH, releaseDateDay);
                        } else {
                            JOptionPane.showMessageDialog(this, "La date n'est pas valide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "La date n'est pas valide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "La date n'est pas valide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            setVisible(false);

            Produit produit;
            try {
                if(cbx_type.getSelectedIndex() == 1) // It's a Roman
                    produit = new Roman(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
                else if (cbx_type.getSelectedIndex() == 2) // It's a Manuel Scolaire
                    produit = new ManuelScolaire(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
                else if (cbx_type.getSelectedIndex() == 3) // It's a Dictionnaire
                    produit = new Dictionnaire(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
                else if (cbx_type.getSelectedIndex() == 4) // It's a CD
                    produit = new CD(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), releaseDate);
                else if (cbx_type.getSelectedIndex() == 5) // It's a DVD
                    produit = new DVD(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
                else // It's a BD (the first one who is selected by default)
                    produit = new BD(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
            } catch (Exception error) {
                JOptionPane.showMessageDialog(this, "Une des entrées ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                setVisible(true);
                return;
            }
            var owner = (MainWindow) getOwner();
            owner.produitDialogReturn(produit);
            this.dispose();

        } else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.dialogReturn();
            this.dispose();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbx_type) {
            lbl_option1.setText(Utils.produits[cbx_type.getSelectedIndex()][1] + " :");
            if (cbx_type.getSelectedIndex() == 4) { // It's a CD
                var defCalendar = Calendar.getInstance();
                var defDate = new int[] { defCalendar.get(Calendar.DATE), (defCalendar.get(Calendar.MONTH) + 1), defCalendar.get(Calendar.YEAR) };
                tf_option1.setText(
                      (defDate[0] < 10 ? "0" + defDate[0] : defDate[0]) + "/"
                    + (defDate[1] < 10 ? "0" + defDate[1] : defDate[1]) + "/"
                    + defDate[2]);
            } else {
                tf_option1.setText("");
            }

        }
    }

}
