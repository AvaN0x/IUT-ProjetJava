package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;

import app.Emprunt;
import app.Produit;

@SuppressWarnings("serial")
public class EmpruntDialog extends JDialog implements ActionListener {
    private Produit produit;
    private Calendar dateCreation;

    private boolean dateFinValid;
    private Calendar dateFin;
    private JTextField tf_dateFin;
    private JLabel lbl_dateFinWarn;

    private JButton btn_valider;
    private JButton btn_cancel;

    public EmpruntDialog(Window owner, Produit produit, Calendar dateCreation) {
        super(owner, "Gestion vidéothèque - Nouvelle emprunt");
        setSize(200, 140);
        setLocationRelativeTo(null);

        this.produit = produit;
        this.dateCreation = dateCreation;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        var pnl_dateFin = new JPanel(new GridLayout(3, 1));
        var lbl_dateFin = new JLabel("Date de fin : ");
        tf_dateFin = new JTextField(10);
        dateFin = Calendar.getInstance();
        dateFin.set(Calendar.MILLISECOND, 0);
        dateFin.set(Calendar.SECOND, 0);
        dateFin.set(Calendar.MINUTE, 0);
        dateFin.set(Calendar.HOUR_OF_DAY, 0);
        dateFin.add(Calendar.DAY_OF_YEAR, 1);
        var defDate = new int[] { dateFin.get(Calendar.DATE), (dateFin.get(Calendar.MONTH) + 1), dateFin.get(Calendar.YEAR) };
        tf_dateFin.setText(
                    (defDate[0] < 10 ? "0" + defDate[0] : defDate[0]) + "/"
                + (defDate[1] < 10 ? "0" + defDate[1] : defDate[1]) + "/"
                + defDate[2]);
        dateFinValid = true;
        lbl_dateFinWarn = new JLabel("");
        lbl_dateFinWarn.setForeground(Color.RED);
        tf_dateFin.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void insertUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                Pattern regex = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}");
                Matcher m = regex.matcher(tf_dateFin.getText());
                if (m.matches()) {
                    String dateValue = tf_dateFin.getText();
                    String[] dateValueTab = dateValue.split("/");

                    int dateFinDay = Integer.parseInt(dateValueTab[0]);
                    int dateFinMonth = Integer.parseInt(dateValueTab[1]) - 1;
                    int dateFinYear = Integer.parseInt(dateValueTab[2]);
                    if (dateFinYear >= 1970 && dateFinMonth >= 0 && dateFinMonth <= 11) {
                        dateFin.set(Calendar.MONTH, dateFinMonth);
                        dateFin.set(Calendar.YEAR, dateFinYear);
                        if (dateFinDay >= dateFin.getActualMinimum(Calendar.DAY_OF_MONTH)
                                && dateFinDay <= dateFin.getActualMaximum(Calendar.DAY_OF_MONTH)) {    
                            dateFin.set(Calendar.DAY_OF_MONTH, dateFinDay);
                            if (dateFin.getTimeInMillis() > dateCreation.getTimeInMillis()) {
                                lbl_dateFinWarn.setText("");
                                dateFinValid = true;
                            } else {
                                lbl_dateFinWarn.setText("Date inférieur à la date de création");
                                dateFinValid = false;
                            }
                            checkBtnValider();
                        } else
                            notValid();
                    } else
                        notValid();
                } else
                    notValid();
            }

            private void notValid() {
                lbl_dateFinWarn.setText("Date non valide (dd/mm/yyyy)");
                dateFinValid = false;
            }
        });

        pnl_dateFin.add(lbl_dateFin);
        pnl_dateFin.add(tf_dateFin);
        pnl_dateFin.add(lbl_dateFinWarn);

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.setIcon(new ImageIcon(getClass().getResource(".\\icons\\ok.png")));
        btn_valider.addActionListener(this);
        btn_cancel = new JButton("Annuler");
        btn_cancel.setIcon(new ImageIcon(getClass().getResource(".\\icons\\no.png")));
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        add(pnl_dateFin, BorderLayout.CENTER);
        add(pnl_validate, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                quit();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_valider) {
            var emprunt = new Emprunt(dateCreation, dateFin, produit);
            setVisible(false);
            var owner = (IMyEmpruntDialogOwner) getOwner();
            owner.empruntDialogReturn(emprunt);
            dispose();
        } else if (e.getSource() == btn_cancel) {
            quit();
        }
    }

    public void checkBtnValider() {
        if (dateFinValid)
            btn_valider.setEnabled(true);
        else
            btn_valider.setEnabled(false);

    }

    private void quit() {
        setVisible(false);
        var owner = (IMyEmpruntDialogOwner) getOwner();
        owner.dialogReturn();
        dispose();
    }
}
