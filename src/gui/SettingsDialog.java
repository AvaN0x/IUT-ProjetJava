package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class SettingsDialog extends MyJDialog implements ActionListener {
    private JComboBox<String> cbx_language;
    private JRadioButton rb_saveLocal;
    private JRadioButton rb_saveDB;
    private JTextField tf_dbUrl;
    private JTextField tf_dbUser;
    private JTextField tf_dbPassword;
    private JComboBox<String> cbx_db;
    private JButton btn_valider;
    private JButton btn_cancel;

    public SettingsDialog(Window owner) {
        super(owner, "Paramètres", new Dimension(250, 290));
    }

    public void initComponents() {
        var pnl_settings = new JPanel();
        pnl_settings.setLayout(new BoxLayout(pnl_settings, BoxLayout.PAGE_AXIS));

        var pnl_language = new JPanel(new FlowLayout());
        var lbl_language = new JLabel("Langue :");
        pnl_language.add(lbl_language);
        cbx_language = new JComboBox<String>();
        // cbx_language.addItem("Default");
        cbx_language.addItem("Français");
        // cbx_language.addItem("English");
        pnl_language.add(cbx_language);
        pnl_settings.add(pnl_language);

        var pnl_saveMethod = new JPanel();
        pnl_saveMethod.setLayout(new BoxLayout(pnl_saveMethod, BoxLayout.PAGE_AXIS));

        var lbl_saveMethod = new JLabel("Méthode de sauvegarde :");
        pnl_saveMethod.add(lbl_saveMethod);

        var grp_save = new ButtonGroup();
        rb_saveLocal = new JRadioButton("Locale");
        if (Utils.settings.isLocal)
            rb_saveLocal.setSelected(true);
        rb_saveLocal.addActionListener(this);
        grp_save.add(rb_saveLocal);
        pnl_saveMethod.add(rb_saveLocal);

        rb_saveDB = new JRadioButton("BdD");
        if (!Utils.settings.isLocal)
            rb_saveDB.setSelected(true);
        rb_saveDB.addActionListener(this);
        grp_save.add(rb_saveDB);
        pnl_saveMethod.add(rb_saveDB);

        pnl_settings.add(pnl_saveMethod);

        var pnl_bdd = new JPanel();
        pnl_bdd.setLayout(new BoxLayout(pnl_bdd, BoxLayout.PAGE_AXIS));

        var pnl_url = new JPanel(new FlowLayout());
        var lbl_url = new JLabel("Host :");
        pnl_url.add(lbl_url);
        tf_dbUrl = new JTextField(10);
        tf_dbUrl.setText(Utils.settings.dbUrl.getHostAddress());
        if (Utils.settings.isLocal)
            tf_dbUrl.setEnabled(false);
        pnl_url.add(tf_dbUrl);
        pnl_bdd.add(pnl_url);

        var pnl_user = new JPanel(new FlowLayout());
        var lbl_user = new JLabel("Username :");
        pnl_user.add(lbl_user);
        tf_dbUser = new JTextField(10);
        tf_dbUser.setText(Utils.settings.dbUser);
        if (Utils.settings.isLocal)
            tf_dbUser.setEnabled(false);
        pnl_user.add(tf_dbUser);
        pnl_bdd.add(pnl_user);

        var pnl_pass = new JPanel(new FlowLayout());
        var lbl_pass = new JLabel("Password :");
        pnl_pass.add(lbl_pass);
        tf_dbPassword = new JTextField(10);
        tf_dbPassword.setText(Utils.settings.dbPass);
        if (Utils.settings.isLocal)
            tf_dbPassword.setEnabled(false);
        pnl_pass.add(tf_dbPassword);
        pnl_bdd.add(pnl_pass);

        var pnl_db = new JPanel(new FlowLayout());
        var lbl_db = new JLabel("Base de Données :");
        pnl_db.add(lbl_db);
        cbx_db = new JComboBox<String>();
        cbx_db.addActionListener(this);
        if (Utils.settings.isLocal)
            cbx_db.setEnabled(false);
        else
            reloadDB();
        pnl_db.add(cbx_db);
        pnl_bdd.add(pnl_db);

        pnl_settings.add(pnl_bdd);

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        pnl_settings.add(pnl_validate);

        add(pnl_settings, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rb_saveLocal) {
            tf_dbUrl.setEnabled(false);
            tf_dbUser.setEnabled(false);
            tf_dbPassword.setEnabled(false);
            cbx_db.setEnabled(false);

            cbx_db.removeAllItems();
        } else if (e.getSource() == rb_saveDB) {
            tf_dbUrl.setEnabled(true);
            tf_dbUser.setEnabled(true);
            tf_dbPassword.setEnabled(true);
            cbx_db.setEnabled(true);

            changeDBSettings();
            reloadDB();
        } else if (e.getSource() == btn_valider) {
            Utils.settings.isLocal = rb_saveLocal.isSelected();

            changeDBSettings();

            Utils.settings.language = Locale.getDefault(); // TODO faire en sorte que çe soit bien choisit et pas juste le défaut

            Utils.save();
            if (Utils.settings.isLocal) {
                Utils.settings.resetDB();
            } else {
                // TODO: prévenir qu'on ping l'host
                // BUG : hôte injoignable alors que si
                try {
                    if (!Utils.settings.dbUrl.isReachable(5000))
                        JOptionPane.showMessageDialog(null, "L'hôte est injoignable", "Test de connexion",
                                JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    Utils.logStream.Error(ex);
                }
            }

            Utils.commandes.clear();
            Utils.clients.clear();
            Utils.produits.clear();
            if (!Utils.load())
                if (!Utils.settings.isLocal)
                    JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Connexion",
                            JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this, "Erreur de lecture des données", "Chargement",
                            JOptionPane.ERROR_MESSAGE);

            quit();
        } else if (e.getSource() == btn_cancel) {
            quit();
        } else if (e.getSource() == cbx_db && cbx_db.getSelectedItem() == "<rafraichir>") {
            changeDBSettings();
            reloadDB();
        }
    }

    private void changeDBSettings() {
        try {
            Utils.settings.dbUrl = InetAddress.getByName(tf_dbUrl.getText());
        } catch (UnknownHostException ex) {
            Utils.logStream.Error(ex);
        }
        Utils.settings.dbUser = tf_dbUser.getText();
        Utils.settings.dbPass = tf_dbPassword.getText();
        Utils.settings.dbBase = (String) cbx_db.getSelectedItem();
    }

    private void reloadDB() {
        cbx_db.removeAllItems();
        var tmp_base = Utils.settings.dbBase;
        try {
            Utils.settings.dbBase = "";
            var databases = Utils.SQLrequest("show databases");
            while (databases.next()) {
                cbx_db.addItem(databases.getString(1));
            }
        } catch (SQLException e) {
            Utils.logStream.Error(e);
        }
        Utils.settings.dbBase = tmp_base;
        cbx_db.addItem("<rafraichir>");
        cbx_db.setSelectedIndex(-1);
    }

}
