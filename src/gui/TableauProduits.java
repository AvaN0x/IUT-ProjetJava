package gui;

import java.util.ArrayList;
import javax.swing.table.*;
import app.Produit;

public class TableauProduits extends AbstractTableModel{
    private ArrayList<Produit> produits;

    private final String[] categories = new String[] { "Nom", "Prix / jour", "Catégorie", "Disponible", "Loués" };

    public TableauProduits(){
        super();

        produits = new ArrayList<Produit>();
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
                return produits.get(rowIndex).getTitle();
            case 1:
                return produits.get(rowIndex).getDailyPrice() + " €";
            case 2:
                return produits.get(rowIndex).getClass().getSimpleName();
            case 3:
                return produits.get(rowIndex).getDispo();
            case 4:
                return produits.get(rowIndex).getQuantity() - produits.get(rowIndex).getDispo();
            default:
                return null;
        }
    }
 
    public void add(Produit prod) {
        produits.add(prod);
 
        fireTableRowsInserted(produits.size() -1, produits.size() -1);
    }
 
    public void remove(int rowIndex) {
        produits.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Produit getProduit(int index) {
        return produits.get(index);
    }
}
