package gui;

import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.table.*;
import app.Commande;

public class TableauCommandes extends AbstractTableModel{
    private ArrayList<Commande> produits;

    private final String[] categories = new String[] { "Client", "Date de Création", "Réduction", "Emprunts" };

    public TableauCommandes(){
        super();

        produits = new ArrayList<Commande>();
    }

    public int getRowCount() {
        return produits.size();
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
                return produits.get(rowIndex).getClient();
            case 1:
                var dateCreation = produits.get(rowIndex).getDateCreation();
                return ((dateCreation.get(Calendar.DAY_OF_MONTH) > 9) ? dateCreation.get(Calendar.DAY_OF_MONTH) : ("0" + dateCreation.get(Calendar.DAY_OF_MONTH))) + 
                "/" + ((dateCreation.get(Calendar.MONTH) > 8) ? (dateCreation.get(Calendar.MONTH) + 1) : ("0" + (dateCreation.get(Calendar.MONTH) + 1))) + 
                "/" + dateCreation.get(Calendar.YEAR);
            case 2:
                return produits.get(rowIndex).getReduction();
            case 3:
                return produits.get(rowIndex).getEmprunts();
                //TODO fix affichage : supprimer la colonne et les afficher dans CommandeInfo.java (TODO)
            default:
                return null;
        }
    }
 
    public void add(Commande prod) {
        produits.add(prod);
 
        fireTableRowsInserted(produits.size() -1, produits.size() -1);
    }
 
    public void remove(int rowIndex) {
        produits.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

}
