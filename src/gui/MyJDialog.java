package gui;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public abstract class MyJDialog extends JDialog {

    public MyJDialog(Window owner, String title, Dimension size) {
        super(owner, title);
        setSize(size);
        setLocationRelativeTo(owner);

        setLayout(new BorderLayout());
        initComponents();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                quit();
            }
        });
    }

    /**
     * Quits the dialog
     */
    public void quit() {
        setVisible(false);
        var owner = (IMyDialogOwner) getOwner();
        owner.dialogReturn();
        this.dispose();
    }

    /**
     * Initialize the components
     */
    public abstract void initComponents();
}
