package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CommandeDialog extends JOptionPane implements ActionListener {
    private JButton btn_newUser;

    public CommandeDialog() {
        super("test - Nouvelle commande");
        setLocation(300, 200);
        setSize(600, 300);

        initComponents();
    }

    private void initComponents() {
        btn_newUser = new JButton(new ImageIcon(getClass().getResource("\\icons\\addUser.png")));
        btn_newUser.setToolTipText("Ajouter un client");
        btn_newUser.addActionListener(this);

        add(btn_newUser);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_newUser) {
            var userWindow = new UserDialog();
            System.out.println(userWindow.client);
        }
    }

}
