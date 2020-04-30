package gui;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public abstract class MyJDialog extends JDialog {

    public MyJDialog(Window owner, String title, Dimension size) {
        super(owner, "Gestion vidéothèque - " + title);
        setSize(size);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        initComponents();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                quit();
            }
        });
    }

    public void quit() {
        setVisible(false);
        var owner = (IMyDialogOwner) getOwner();
        owner.dialogReturn();
        this.dispose();
    }

    public abstract void initComponents();
}
