package gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.*;

import app.Commande;
import app.Produit;

@SuppressWarnings("serial")
public class TableauProduits extends AbstractTableModel implements IMyTableModel<Produit> {
    private List<Produit> produits;
    private HashMap<String, Integer> prodStock;
    private final String[] categories = new String[] { "Nom", "Prix / jour", "Catégorie", "Disponible", "Loués" };

    public TableauProduits() {
        super();
        produits = new ArrayList<Produit>();
    }

    public void setProdStock() {
        setProdStock(Calendar.getInstance());
    }

    public void setProdStock(Calendar date) {
        prodStock = new HashMap<String, Integer>();
        for (Produit prod : Utils.produits.getList()) {
            prodStock.put(prod.getId(), prod.getQuantity());
        }
        for (Commande c : Utils.commandes.getList()) {
            c.getProdUsedAt(date, prodStock);
        }
    }

    public HashMap<String, Integer> getProdStock() {
        return prodStock;
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
                return prodStock.get(produits.get(rowIndex).getId());
            case 4:
                return produits.get(rowIndex).getQuantity() - prodStock.get(produits.get(rowIndex).getId());
            default:
                return null;
        }
    }
 
    @Override
    public void add(Produit prod) {
        produits.add(prod);
 
        fireTableRowsInserted(produits.size() -1, produits.size() -1);
    }
 
    @Override
    public void remove(int rowIndex) {
        produits.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public Produit getItem(int index) {
        return produits.get(index);
    }

    @Override
    public List<Produit> getList() {
        return produits;
    }

    @Override
    public void setList(List<Produit> list) {
        produits = list;
    }

    @Override
    public void clear(){
        produits.clear();
    }

    public int getProductStock(String key) {
        return prodStock.get(key);
    }

}
