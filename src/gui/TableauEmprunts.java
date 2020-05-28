package gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.table.*;
import app.Emprunt;

@SuppressWarnings("serial")
public class TableauEmprunts extends AbstractTableModel implements IMyTableModel<Emprunt> {
        private List<Emprunt> emprunts;
    
        private final String[] categories = new String[] { Utils.lang.field_name, Utils.lang.field_price, Utils.lang.field_categ, Utils.lang.field_date_end, Utils.lang.field_cost };
    
        public TableauEmprunts(){
            super();
            emprunts = new ArrayList<Emprunt>();
        }
        public TableauEmprunts(ArrayList<Emprunt> emprunts){
            super();
            this.emprunts = emprunts;
        }

    
        public int getRowCount() {
            return emprunts.size();
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
                    return emprunts.get(rowIndex).getProduit().getTitle();
                case 1:
                    return emprunts.get(rowIndex).getProduit().getDailyPrice() + " €";
                case 2:
                    return emprunts.get(rowIndex).getProduit().getClass().getSimpleName();
                case 3:
                    return Utils.dateToString(emprunts.get(rowIndex).getDateFin());
                case 4:
                    return emprunts.get(rowIndex).getCost() + " €";
                default:
                    return null;
            }
        }
     
        @Override
        public void add(Emprunt emprunt) {
            emprunts.add(emprunt);
     
            fireTableRowsInserted(emprunts.size() -1, emprunts.size() -1);
        }
     
        @Override
        public void remove(int rowIndex) {
            emprunts.remove(rowIndex);
     
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    
        @Override
        public Emprunt getItem(int index) {
            return emprunts.get(index);
        }

        @Override
        public List<Emprunt> getList() {
            return emprunts;
        }
        @Override
        public void setList(List<Emprunt> list) {
            emprunts = list;
        }

        @Override
        public void clear(){
            emprunts.clear();
        }

    }
    