package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.Client;
import app.ClientFidele;
import app.ClientOccas;

@SuppressWarnings("serial")
public class UserDialog extends MyJDialog implements ActionListener {
    private JTextField tf_nom;
    private JTextField tf_prenom;
    private JCheckBox cb_fidel;
    
    private JButton btn_valider;
    private JButton btn_cancel;

    public UserDialog(Window owner) {
        super(owner, Utils.lang.new_user, new Dimension(200, 160));
    }

    public void initComponents() {
        var pnl_fields = new JPanel();
        pnl_fields.setLayout(new BoxLayout(pnl_fields, BoxLayout.PAGE_AXIS));

        var pnl_nom = new JPanel(new FlowLayout());
        var lbl_nom = new JLabel(Utils.lang.field_name + " :");
        tf_nom = new JTextField(10);
        pnl_nom.add(lbl_nom);
        pnl_nom.add(tf_nom);
        
        var pnl_prenom = new JPanel(new FlowLayout());
        var lbl_prenom = new JLabel(Utils.lang.field_first_name + " :");
        tf_prenom = new JTextField(10);
        pnl_prenom.add(lbl_prenom);
        pnl_prenom.add(tf_prenom);
        
        var pnl_fidel = new JPanel(new FlowLayout());
        cb_fidel = new JCheckBox(Utils.lang.field_loyal);
        cb_fidel.setToolTipText(Utils.lang.loyal_tooltip);
        pnl_fidel.add(cb_fidel);
        
        pnl_fields.add(pnl_nom);
        pnl_fields.add(pnl_prenom);
        pnl_fields.add(pnl_fidel);

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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            if(tf_nom.getText().trim().length() <= 0 || tf_prenom.getText().trim().length() <= 0 )
            {
                JOptionPane.showMessageDialog(this, Utils.lang.name_error, Utils.lang.error, JOptionPane.ERROR_MESSAGE);
                return;
            }
            Client client;
            if (cb_fidel.isSelected())
                client = new ClientFidele(tf_nom.getText(), tf_prenom.getText());
            else
                client = new ClientOccas(tf_nom.getText(), tf_prenom.getText());
            setVisible(false);
            var owner = (IMyUserDialogOwner) getOwner();
            owner.userDialogReturn(client);
            this.dispose();
        }
        else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (IMyUserDialogOwner) getOwner();
            owner.dialogReturn();
            this.dispose();
        }
    }
}
