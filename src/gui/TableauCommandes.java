package gui;

import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.table.*;
import app.Commande;

public class TableauCommandes extends AbstractTableModel{
    private ArrayList<Commande> commandes;

    private final String[] categories = new String[] { "Client", "Date de Création", "Réduction", "Cout total", "Emprunts" };

    public TableauCommandes(){
        super();

        commandes = new ArrayList<Commande>();
    }

    public int getRowCount() {
        return commandes.size();
    }
 
    public int getColumnCount() {
        return categories.length;
    }
 
    public String getColumnName(int columnIndex) {
        return categories[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return commandes.get(rowIndex).getClient();
            case 1:
                var dateCreation = commandes.get(rowIndex).getDateCreation();
                return ((dateCreation.get(Calendar.DAY_OF_MONTH) > 9) ? dateCreation.get(Calendar.DAY_OF_MONTH) : ("0" + dateCreation.get(Calendar.DAY_OF_MONTH))) + 
                "/" + ((dateCreation.get(Calendar.MONTH) > 8) ? (dateCreation.get(Calendar.MONTH) + 1) : ("0" + (dateCreation.get(Calendar.MONTH) + 1))) + 
                "/" + dateCreation.get(Calendar.YEAR);
            case 2:
                return commandes.get(rowIndex).getReduction() * 100 + " %";
            case 3:
                return commandes.get(rowIndex).getTotalCost() + " €";
            case 4:
                return commandes.get(rowIndex).getEmprunts();
                //TODO fix affichage : supprimer la colonne et les afficher dans CommandeInfo.java (TODO)
            default:
                return null;
        }
    }
 
    public void add(Commande prod) {
        commandes.add(prod);
 
        fireTableRowsInserted(commandes.size() -1, commandes.size() -1);
    }
 
    public void remove(int rowIndex) {
        commandes.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Commande getCommande(int index) {
        return commandes.get(index);
    }

}
