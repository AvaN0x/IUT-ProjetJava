package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.*;
import app.Commande;

@SuppressWarnings("serial")
public class TableauCommandes extends AbstractTableModel implements IMyTableModel<Commande>{
    private List<Commande> commandes;

    private final String[] categories = new String[] { Utils.lang.field_client, Utils.lang.field_date_start, Utils.lang.field_reduc, Utils.lang.field_sum, Utils.lang.loans };

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
                return Utils.dateToString(commandes.get(rowIndex).getDateCreation());
            case 2:
                return commandes.get(rowIndex).getReduction() * 100 + " %";
            case 3:
                return commandes.get(rowIndex).getTotalCost() + " €";
            case 4:
                return commandes.get(rowIndex).getEmprunts().size();
            default:
                return null;
        }
    }
 
    @Override
    public void add(Commande commande) {
        commandes.add(commande);
 
        fireTableRowsInserted(commandes.size() -1, commandes.size() -1);
    }
 
    @Override
    public void remove(int rowIndex) {
        commandes.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public Commande getItem(int index) {
        return commandes.get(index);
    }

    @Override
    public List<Commande> getList() {
        return commandes;
    }
    @Override
    public void setList(List<Commande> list) {
        commandes = list;
    }

    @Override
    public void clear(){
        commandes.clear();
    }

}
