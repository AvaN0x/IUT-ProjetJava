package gui;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;
import java.io.File;

import javax.swing.*;

@SuppressWarnings("serial")
public class SettingsDialog extends MyJDialog implements ActionListener {
    private JComboBox<Locale> cbx_language;
    private JRadioButton rb_saveLocal;
    private JRadioButton rb_saveDB;
    private JTextField tf_dbUrl;
    private JTextField tf_dbUser;
    private JPasswordField pf_dbPassword;
    private JComboBox<String> cbx_db;
    private JButton btn_valider;
    private JButton btn_cancel;

    public SettingsDialog(Window owner) {
        super(owner, Utils.lang.settings, new Dimension(250, 290));
    }

    public void initComponents() {
        var pnl_settings = new JPanel();
        pnl_settings.setLayout(new BoxLayout(pnl_settings, BoxLayout.PAGE_AXIS));

        var pnl_language = new JPanel(new FlowLayout());
        var lbl_language = new JLabel(Utils.lang.settings_lang);
        pnl_language.add(lbl_language);
        cbx_language = new JComboBox<Locale>();
        var files = new File("lang").list();
        for (var file : files)
            cbx_language.addItem(new Locale(file.substring(0, 2), file.substring(3, 5)));
        cbx_language.setSelectedItem(Utils.settings.language);
        pnl_language.add(cbx_language);
        pnl_settings.add(pnl_language);

        var pnl_saveMethod = new JPanel();
        pnl_saveMethod.setLayout(new BoxLayout(pnl_saveMethod, BoxLayout.PAGE_AXIS));

        var lbl_saveMethod = new JLabel(Utils.lang.settings_save);
        pnl_saveMethod.add(lbl_saveMethod);

        var grp_save = new ButtonGroup();
        rb_saveLocal = new JRadioButton(Utils.lang.settings_local);
        if (Utils.settings.isLocal)
            rb_saveLocal.setSelected(true);
        rb_saveLocal.addActionListener(this);
        grp_save.add(rb_saveLocal);
        pnl_saveMethod.add(rb_saveLocal);

        rb_saveDB = new JRadioButton(Utils.lang.settings_db);
        if (!Utils.settings.isLocal)
            rb_saveDB.setSelected(true);
        rb_saveDB.addActionListener(this);
        grp_save.add(rb_saveDB);
        pnl_saveMethod.add(rb_saveDB);

        pnl_settings.add(pnl_saveMethod);

        var pnl_bdd = new JPanel();
        pnl_bdd.setLayout(new BoxLayout(pnl_bdd, BoxLayout.PAGE_AXIS));

        var pnl_url = new JPanel(new FlowLayout());
        var lbl_url = new JLabel(Utils.lang.settings_host);
        pnl_url.add(lbl_url);
        tf_dbUrl = new JTextField(10);
        tf_dbUrl.setText(Utils.settings.dbUrl.getHostAddress());
        if (Utils.settings.isLocal)
            tf_dbUrl.setEnabled(false);
        pnl_url.add(tf_dbUrl);
        pnl_bdd.add(pnl_url);

        var pnl_user = new JPanel(new FlowLayout());
        var lbl_user = new JLabel(Utils.lang.settings_login);
        pnl_user.add(lbl_user);
        tf_dbUser = new JTextField(10);
        tf_dbUser.setText(Utils.settings.dbUser);
        if (Utils.settings.isLocal)
            tf_dbUser.setEnabled(false);
        pnl_user.add(tf_dbUser);
        pnl_bdd.add(pnl_user);

        var pnl_pass = new JPanel(new FlowLayout());
        var lbl_pass = new JLabel(Utils.lang.settings_pass);
        pnl_pass.add(lbl_pass);
        pf_dbPassword = new JPasswordField(10);
        pf_dbPassword.setText(new String(Utils.settings.dbPass));
        if (Utils.settings.isLocal)
            pf_dbPassword.setEnabled(false);
        pnl_pass.add(pf_dbPassword);
        pnl_bdd.add(pnl_pass);

        var pnl_db = new JPanel(new FlowLayout());
        var lbl_db = new JLabel(Utils.lang.settings_base);
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
        btn_valider = new JButton(Utils.lang.validate);
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton(Utils.lang.cancel);
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
            pf_dbPassword.setEnabled(false);
            cbx_db.setEnabled(false);
            
            cbx_db.removeAllItems();
            cbx_db.addItem(Utils.lang.settings_reload);
        } else if (e.getSource() == rb_saveDB) {
            tf_dbUrl.setEnabled(true);
            tf_dbUser.setEnabled(true);
            pf_dbPassword.setEnabled(true);
            cbx_db.setEnabled(true);
            
            changeDBSettings();
            cbx_db.removeAllItems();
            cbx_db.addItem(Utils.lang.settings_reload);
        } else if (e.getSource() == btn_valider) {
            Utils.settings.isLocal = rb_saveLocal.isSelected();
            
            changeDBSettings();

            if(!Utils.settings.language.toString().equals(cbx_language.getSelectedItem().toString()))
                JOptionPane.showMessageDialog(this, Utils.lang.restart_lang, Utils.lang.connect_title, JOptionPane.INFORMATION_MESSAGE);
            Utils.settings.language = (Locale) cbx_language.getSelectedItem();

            if (!Utils.settings.isLocal) {
                JOptionPane.showMessageDialog(this, Utils.lang.connect_try, Utils.lang.connect_title, JOptionPane.INFORMATION_MESSAGE);
                setEnabled(false);
            }

            Utils.save();
            if (Utils.settings.isLocal) {
                Utils.settings.resetDB();
            }

            Utils.commandes.clear();
            Utils.clients.clear();
            Utils.produits.clear();
            MainWindow.requestLoading();
            setEnabled(true);

            quit();
        } else if (e.getSource() == btn_cancel) {
            quit();
        } else if (e.getSource() == cbx_db && cbx_db.getSelectedItem() == Utils.lang.settings_reload) {
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
        Utils.settings.dbPass = pf_dbPassword.getPassword();
        Utils.settings.dbBase = (String) cbx_db.getSelectedItem();
    }

    private void reloadDB() {
        cbx_db.removeAllItems();
        var tmp_base = Utils.settings.dbBase;
        try {
            Utils.settings.dbBase = "";
            var databases = Utils.SQLrequest("show databases");
            while (databases.next()) {
                if(!Arrays.asList(Utils.dbIgnored).contains(databases.getString(1)))
                    cbx_db.addItem(databases.getString(1));
            }
        } catch (SQLException e) {
            Utils.logStream.Error(e);
        }
        Utils.settings.dbBase = tmp_base;
        cbx_db.addItem(Utils.lang.settings_reload);
        cbx_db.setSelectedItem(Utils.settings.dbBase);
    }

}
