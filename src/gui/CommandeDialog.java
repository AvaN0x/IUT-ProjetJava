package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;

import app.Client;
import app.Commande;

public class CommandeDialog extends JDialog implements ActionListener, ListSelectionListener {
    private JList<Client> l_clients;
    private JButton btn_newUser;
    private JButton btn_delUser;
    private JButton btn_valider;
    private JButton btn_cancel;

    private JLabel lbl_dateCreation;
    private JTextField tf_dateCreation;
    private JLabel lbl_dateCreationWarn;
    private boolean dateCreationValid;

    public CommandeDialog(Window owner) {
        super(owner, "test - Nouvelle commande");
        setLocation(300, 200);
        setSize(600, 300);

        initComponents();
    }

    private void initComponents() {
        var owner = (MainWindow) getOwner();
        setLayout(new BorderLayout());

        var pnl_clients = new JPanel(new FlowLayout());

        l_clients = new JList<Client>(owner.clients);
        l_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l_clients.setSelectedIndex(0);
        l_clients.addListSelectionListener(this);
        l_clients.setVisibleRowCount(5);
        var l_clientsScrollPane = new JScrollPane(l_clients);

        var pnl_clientsbtns = new JPanel(new GridLayout(2,1));
        btn_newUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\addUser.png")));
        btn_newUser.setToolTipText("Ajouter un client");
        btn_newUser.addActionListener(this);
        btn_delUser = new JButton(new ImageIcon(getClass().getResource(".\\icons\\deleteUser.png")));
        btn_delUser.setToolTipText("Supprimer un client sélectionné");
        btn_delUser.addActionListener(this);
        btn_delUser.setEnabled(false);
        pnl_clientsbtns.add(btn_newUser);
        pnl_clientsbtns.add(btn_delUser);

        pnl_clients.add(l_clientsScrollPane);
        pnl_clients.add(pnl_clientsbtns);

        var pnl_validate = new JPanel(new FlowLayout());
        btn_valider = new JButton("Valider");
        btn_valider.addActionListener(this);
        btn_valider.setEnabled(false);
        btn_cancel = new JButton("Annuler");
        btn_cancel.addActionListener(this);
        pnl_validate.add(btn_valider);
        pnl_validate.add(btn_cancel);

        var pnl_dateCreation = new JPanel(new GridLayout(2,1));
        lbl_dateCreation = new JLabel("Date de création : ");
        var pnl_dateCreationSelect = new Panel(new FlowLayout());
        tf_dateCreation = new JTextField(10);
        lbl_dateCreationWarn = new JLabel("");
        lbl_dateCreationWarn.setForeground(Color.RED);
        tf_dateCreation.getDocument().addDocumentListener(new DocumentListener() {
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
                Matcher m = regex.matcher(tf_dateCreation.getText());
                if (m.matches()) {
                    String dateValue = tf_dateCreation.getText();
                    String[] dateValueTab = dateValue.split("/");

                    int dateCheckDay = Integer.parseInt(dateValueTab[0]);
                    int dateCheckMonth = Integer.parseInt(dateValueTab[1]) - 1;
                    int dateCheckYear = Integer.parseInt(dateValueTab[2]);            

                    if (dateCheckYear >= 1970 && dateCheckMonth >= 0 && dateCheckMonth <= 11) {
                        Calendar dateCheck = Calendar.getInstance();
                        dateCheck.set(Calendar.MONTH, dateCheckMonth);
                        dateCheck.set(Calendar.YEAR, dateCheckYear);
                        if (dateCheckDay >= dateCheck.getActualMinimum(Calendar.DAY_OF_MONTH) && dateCheckDay <= dateCheck.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            dateCheck.set(Calendar.DAY_OF_MONTH, dateCheckDay);
                            lbl_dateCreationWarn.setText(""); 
                            dateCreationValid = true;
                        } else
                            notValid();
                    } else
                        notValid();       


                } else
                    notValid();
            }

            private void notValid() {
                lbl_dateCreationWarn.setText("Date non valide (dd/mm/yyyy)"); 
                dateCreationValid = false;   
            }
        });

        pnl_dateCreationSelect.add(lbl_dateCreation);
        pnl_dateCreationSelect.add(tf_dateCreation);

        pnl_dateCreation.add(pnl_dateCreationSelect);
        pnl_dateCreation.add(lbl_dateCreationWarn);

        add(pnl_dateCreation, BorderLayout.WEST);

        add(pnl_clients, BorderLayout.EAST);
        add(pnl_validate, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                var owner = (MainWindow) getOwner();
                owner.commandeDialogReturn();
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_newUser) {
            var userDialog = new UserDialog(this);
            userDialog.setVisible(true);
            this.setEnabled(false);
        } else if (e.getSource() == btn_delUser) {
            var owner = (MainWindow) getOwner();
            owner.clients.removeElement(l_clients.getSelectedValue());
        } else if (e.getSource() == btn_valider) {
            var commande = new Commande(l_clients.getSelectedValue());
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.commandeDialogReturn(commande);
            this.dispose();
        } else if (e.getSource() == btn_cancel) {
            setVisible(false);
            var owner = (MainWindow) getOwner();
            owner.commandeDialogReturn();
            this.dispose();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (l_clients.getSelectedIndex() == -1) {
                btn_delUser.setEnabled(false);
                checkBtnValider();
            } else {
                btn_delUser.setEnabled(true);
                checkBtnValider();
            }
        }
    }

    public void checkBtnValider() {
        if (l_clients.getSelectedIndex() != -1
            && dateCreationValid
            )
            btn_valider.setEnabled(true);
        else
            btn_valider.setEnabled(false);


    }

    public void userDialogReturn()
    {
        this.setEnabled(true);
        this.toFront();
    }
    public void userDialogReturn(Client client) {
        var owner = (MainWindow) getOwner();
        owner.clients.addElement(client);
        userDialogReturn();
    }

}
