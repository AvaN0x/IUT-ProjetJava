package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import app.*;

@SuppressWarnings("serial")
public class ProduitDialog extends MyJDialog implements ActionListener, ItemListener {
    private Produit produit;
    
    private JLabel lbl_option1;
    private JTextField tf_title;
    private JTextField tf_price;
    private JTextField tf_quantity;
    private JTextField tf_option1;
    private JComboBox<String> cbx_type;
    private JButton btn_valider;
    private JButton btn_cancel;

    public ProduitDialog(Window owner) {
        super(owner, "Nouveau produit", new Dimension(240, 210));
    }

    public ProduitDialog(Window owner, Produit produit) {
        super(owner, "Nouveau produit", new Dimension(240, 210));

        this.produit = produit;
        cbx_type.setEnabled(false);
        var types = Utils.getTypes();
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).getValue0() == produit.getClass())
                cbx_type.setSelectedIndex(i);
        }
        tf_title.setText(produit.getTitle());
        tf_price.setText(Double.toString(produit.getDailyPrice()));
        tf_quantity.setText(Integer.toString(produit.getQuantity()));
        tf_option1.setText((String) produit.getOption1());
    }


    public void initComponents() {
        var pnl_fields = new JPanel();
        pnl_fields.setLayout(new BoxLayout(pnl_fields, BoxLayout.PAGE_AXIS));
        
        cbx_type = new JComboBox<String>();
        for (var item : Utils.getTypes()) {
            cbx_type.addItem(item.getValue0().getSimpleName());
        }
        cbx_type.addItemListener(this);
        pnl_fields.add(cbx_type);
        
        
        var pnl_title = new JPanel(new FlowLayout());
        var lbl_title = new JLabel("Titre :");
        tf_title = new JTextField(20);
        pnl_title.add(lbl_title);
        pnl_title.add(tf_title);
        pnl_fields.add(pnl_title);
        
        var pnl_price = new JPanel(new FlowLayout());
        var lbl_price = new JLabel("Prix / jour :");
        tf_price = new JTextField(5);
        pnl_price.add(lbl_price);
        pnl_price.add(tf_price);
        pnl_fields.add(pnl_price);
        
        var pnl_quantity = new JPanel(new FlowLayout());
        var lbl_quantity = new JLabel("Quantité :");
        tf_quantity = new JTextField(3);
        pnl_quantity.add(lbl_quantity);
        pnl_quantity.add(tf_quantity);
        pnl_fields.add(pnl_quantity);
        
        var pnl_optionfields = new JPanel();
        pnl_optionfields.setLayout(new BoxLayout(pnl_optionfields, BoxLayout.PAGE_AXIS));
        var pnl_option1 = new JPanel(new FlowLayout());
        lbl_option1 = new JLabel();
        var types = Utils.getTypes();
        for (var pair : types) {
            if(pair.getValue0().getSimpleName() == cbx_type.getSelectedItem())
                // TODO: Majuscule
                lbl_option1 = new JLabel(pair.getValue1()[0].getName() + " :");
        }
        tf_option1 = new JTextField(10);
        pnl_option1.add(lbl_option1);
        pnl_option1.add(tf_option1);
        pnl_optionfields.add(pnl_option1);
        pnl_fields.add(pnl_optionfields);

        var pnl_validate = new JPanel(new FlowLayout());
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
            if (produit != null) {
                if (Double.parseDouble(tf_quantity.getText().trim()) < (produit.getQuantity() - Utils.produits.getProductStock(produit.getId())))
                {
                    JOptionPane.showMessageDialog(this, "La quantité ne peut pas être inférieur au nombre de produit loués actuellement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                } 
            }

            Calendar releaseDate = Calendar.getInstance();
            releaseDate.set(Calendar.MILLISECOND, 0);
            releaseDate.set(Calendar.SECOND, 0);
            releaseDate.set(Calendar.MINUTE, 0);
            releaseDate.set(Calendar.HOUR_OF_DAY, 0);
    
            if (cbx_type.getSelectedIndex() == 4) { // It's a CD // TODO faire la verif si c'est une date (au moins si c'est un CD avec reflection)
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
            if (produit == null) {
                try {
                    for (var pair : Utils.getTypes()){
                        if(pair.getValue0() != CD.class) // TODO better cd generation
                            //BUG génère toujours un roman
                            produit = pair.getValue0().getDeclaredConstructor(String.class, double.class, int.class, String.class).newInstance(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
                        else
                            produit = new CD(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), releaseDate);
                    }
                } catch (Exception error) {
                    Utils.logStream.Error(error);
                    JOptionPane.showMessageDialog(this, "Une des entrées ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    setVisible(true);
                    return;
                }
                var owner = (IMyProduitDialogOwner) getOwner();
                owner.produitDialogReturn(produit);
            } else {
                try{
                    var types = new ArrayList<String>();
                    for (var type : Utils.getTypes()) {
                        types.add(type.getValue0().getName().replaceAll("\\s+",""));
                    } 
                    Utils.SQLupdate(String.format("UPDATE `produits` SET `title` = \"%s\", `dailyPrice` = \""+produit.getDailyPrice()+"\", `quantity` = \"%d\", `option1` = \"%s\", `id-types` = \"%d\" WHERE `produits`.`id-prod` = \"%s\"", produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getName().substring(4)), produit.getId()));
                }
                catch (SQLException ex){
                    Utils.logStream.Error(ex);
                }
                produit.setTitle(tf_title.getText());
                produit.setDailyPrice(Double.parseDouble(tf_price.getText().trim()));
                produit.setQuantity(Integer.parseInt(tf_quantity.getText().trim()));
                if (cbx_type.getSelectedItem() == CD.class.getSimpleName())
                    produit.setOption1(releaseDate);
                else
                    produit.setOption1(tf_option1.getText());
                Utils.logStream.Log("Product " + produit.getId() + " edited");
                var owner = (IMyDialogOwner) getOwner();
                owner.dialogReturn();
            }
            this.dispose();

        } else if (e.getSource() == btn_cancel) {
            quit();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbx_type) {
            var types = Utils.getTypes();
            for (var pair : types) {
                if(pair.getValue0().getSimpleName() == cbx_type.getSelectedItem())
                    // TODO: Majuscule... et meilleur nom ^^
                    lbl_option1.setText(pair.getValue1()[0].getName() + " :");
            }
            if (cbx_type.getSelectedItem() == CD.class.getSimpleName()) {
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
