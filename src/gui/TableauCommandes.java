package gui;

import java.util.ArrayList;
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
                return produits.get(rowIndex).getDateCreation();
            case 2:
                return produits.get(rowIndex).getReduction();
            case 3:
                return produits.get(rowIndex).getEmprunts();
                //TODO fix affichage
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
