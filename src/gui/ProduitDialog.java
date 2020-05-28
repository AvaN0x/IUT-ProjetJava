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
        super(owner, Utils.lang.new_product, new Dimension(240, 210));
    }

    public ProduitDialog(Window owner, Produit produit) {
        super(owner, Utils.lang.new_product, new Dimension(240, 210));

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
            for (var field : Lang.class.getDeclaredFields()) {
                if (field.getName().equals("class_" + item.getValue0().getSimpleName()))
                    try {
                        cbx_type.addItem((String) field.get(Utils.lang));
                        break;
                    } catch (IllegalAccessException e) {
                        Utils.logStream.Error(e);
                    }
            }
        }
        cbx_type.addItemListener(this);
        pnl_fields.add(cbx_type);

        var pnl_title = new JPanel(new FlowLayout());
        var lbl_title = new JLabel(Utils.lang.field_title + " :");
        tf_title = new JTextField(20);
        pnl_title.add(lbl_title);
        pnl_title.add(tf_title);
        pnl_fields.add(pnl_title);

        var pnl_price = new JPanel(new FlowLayout());
        var lbl_price = new JLabel(Utils.lang.field_price + " :");
        tf_price = new JTextField(5);
        pnl_price.add(lbl_price);
        pnl_price.add(tf_price);
        pnl_fields.add(pnl_price);

        var pnl_quantity = new JPanel(new FlowLayout());
        var lbl_quantity = new JLabel(Utils.lang.field_quantity + " :");
        tf_quantity = new JTextField(3);
        pnl_quantity.add(lbl_quantity);
        pnl_quantity.add(tf_quantity);
        pnl_fields.add(pnl_quantity);

        var pnl_optionfields = new JPanel();
        pnl_optionfields.setLayout(new BoxLayout(pnl_optionfields, BoxLayout.PAGE_AXIS));
        var pnl_option1 = new JPanel(new FlowLayout());
        lbl_option1 = new JLabel();
        reloadOptionsLabel();
        tf_option1 = new JTextField(10);
        pnl_option1.add(lbl_option1);
        pnl_option1.add(tf_option1);
        pnl_optionfields.add(pnl_option1);
        pnl_fields.add(pnl_optionfields);

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton(Utils.lang.validate);
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton(Utils.lang.cancel);
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_fields, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);
    }

    private void reloadOptionsLabel() {
        for (var pair : Utils.getTypes()) {
            if (pair.getValue0().getSimpleName().equals(cbx_type.getSelectedItem()))
                for (var field : Lang.class.getDeclaredFields()) {
                    if (field.getName().equals("field_" + pair.getValue1()[0].getName()))
                        try {
                            lbl_option1.setText((String) field.get(Utils.lang) + " :");
                            break;
                        } catch (IllegalAccessException e) {
                            Utils.logStream.Error(e);
                        }
                }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            if (tf_title.getText().trim().length() <= 0 || tf_price.getText().trim().length() <= 0
                    || tf_quantity.getText().trim().length() <= 0 || tf_option1.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(this, Utils.lang.field_empty, Utils.lang.error, JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (produit != null) {
                if (Double.parseDouble(tf_quantity.getText().trim()) < (produit.getQuantity() - Utils.produits.getProductStock(produit.getId()))) {
                    JOptionPane.showMessageDialog(this, Utils.lang.product_stock_error, Utils.lang.error, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Calendar releaseDate = Calendar.getInstance();
            releaseDate.set(Calendar.MILLISECOND, 0);
            releaseDate.set(Calendar.SECOND, 0);
            releaseDate.set(Calendar.MINUTE, 0);
            releaseDate.set(Calendar.HOUR_OF_DAY, 0);

            if (getSelectedClass() == CD.class) {
                Pattern regex = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}");
                Matcher m = regex.matcher(tf_option1.getText());

                Runnable dateInvalid = () -> {
                    JOptionPane.showMessageDialog(this, Utils.lang.date_invalid, Utils.lang.error, JOptionPane.ERROR_MESSAGE);
                };

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
                            dateInvalid.run();
                            return;
                        }
                    } else {
                        dateInvalid.run();
                        return;
                    }
                } else {
                    dateInvalid.run();
                    return;
                }
            }

            setVisible(false);
            if (produit == null) {
                try {
                    var cls = getSelectedClass();
                    if (cls != CD.class)
                        produit = cls.getDeclaredConstructor(String.class, double.class, int.class, String.class).newInstance(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()),Integer.parseInt(tf_quantity.getText().trim()), tf_option1.getText());
                    else
                        produit = new CD(tf_title.getText(), Double.parseDouble(tf_price.getText().trim()), Integer.parseInt(tf_quantity.getText().trim()), releaseDate);
                } catch (Exception error) {
                    Utils.logStream.Error(error);
                    JOptionPane.showMessageDialog(this, Utils.lang.field_wrong, Utils.lang.error, JOptionPane.ERROR_MESSAGE);
                    setVisible(true);
                    return;
                }
                var owner = (IMyProduitDialogOwner) getOwner();
                owner.produitDialogReturn(produit);
            } else {
                try {
                    var types = new ArrayList<String>();
                    for (var type : Utils.getTypes()) {
                        types.add(type.getValue0().getName().replaceAll("\\s+", ""));
                    }
                    Utils.SQLupdate(String.format("UPDATE `produits` SET `title` = \"%s\", `dailyPrice` = \"" + produit.getDailyPrice() + "\", `quantity` = \"%d\", `option1` = \"%s\", `id-types` = \"%d\" WHERE `produits`.`id-prod` = \"%s\"", produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getName().substring(4)), produit.getId()));
                } catch (SQLException ex) {
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
            Utils.produits.setProdStock();
            this.dispose();

        } else if (e.getSource() == btn_cancel) {
            quit();
        }
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Produit> getSelectedClass() {
        try {
            for (var field : Lang.class.getDeclaredFields())
                if (((String) field.get(Utils.lang)).equals(cbx_type.getSelectedItem()))
                    return (Class<? extends Produit>) Class.forName("app." + (String) field.getName().substring(6));
        } catch (IllegalAccessException | ClassNotFoundException e) {
            Utils.logStream.Error(e);
        }
        return null;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbx_type) {
            reloadOptionsLabel();
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
