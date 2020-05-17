package gui;

import java.awt.*;
import java.awt.event.*;

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
    private JButton btn_valider;
    private JButton btn_cancel;

    public SettingsDialog(Window owner) {
        super(owner, "Paramètres", new Dimension(250, 270));
    }

    public void initComponents() {
        var pnl_settings = new JPanel();
        pnl_settings.setLayout(new BoxLayout(pnl_settings, BoxLayout.PAGE_AXIS));

        var pnl_language = new JPanel(new FlowLayout());
        var lbl_language = new JLabel("Langue :");
        pnl_language.add(lbl_language);
        cbx_language = new JComboBox<String>();
        //cbx_language.addItem("Default");
        cbx_language.addItem("Français");
        //cbx_language.addItem("English");
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
        if (Utils.settings.isLocal)
            tf_dbUrl.setEnabled(false);
        pnl_url.add(tf_dbUrl);
        pnl_bdd.add(pnl_url);

        var pnl_user = new JPanel(new FlowLayout());
        var lbl_user = new JLabel("Username :");
        pnl_user.add(lbl_user);
        tf_dbUser = new JTextField(10);
        if (Utils.settings.isLocal)
            tf_dbUser.setEnabled(false);
        pnl_user.add(tf_dbUser);
        pnl_bdd.add(pnl_user);

        var pnl_pass = new JPanel(new FlowLayout());
        var lbl_pass = new JLabel("Password :");
        pnl_pass.add(lbl_pass);
        tf_dbPassword = new JTextField(10);
        if (Utils.settings.isLocal)
            tf_dbPassword.setEnabled(false);
        pnl_pass.add(tf_dbPassword);
        pnl_bdd.add(pnl_pass);

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
        if (e.getSource() == rb_saveLocal){
            Utils.settings.isLocal = rb_saveLocal.isSelected();
            tf_dbUrl.setEnabled(false);
            tf_dbUser.setEnabled(false);
            tf_dbPassword.setEnabled(false);
        } else if (e.getSource() == rb_saveDB){
            Utils.settings.isLocal = rb_saveLocal.isSelected();
            tf_dbUrl.setEnabled(true);
            tf_dbUser.setEnabled(true);
            tf_dbPassword.setEnabled(true);
        } else if (e.getSource() == btn_valider) {
            if(!Utils.settings.isLocal){
                
                Utils.settings.resetDB();
            }
            Utils.save();
            quit();
        } else if (e.getSource() == btn_cancel) {
            quit();
        }
    }

}
