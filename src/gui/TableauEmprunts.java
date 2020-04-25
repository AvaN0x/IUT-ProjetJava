package gui;

import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.table.*;
import app.Emprunt;

public class TableauEmprunts extends AbstractTableModel {
        private ArrayList<Emprunt> emprunts;
    
private final String[] categories = new String[] { "Nom", "Prix / jour", "Catégorie", "Date de fin", "Cout" };
    
        public TableauEmprunts(){
            super();
    
            emprunts = new ArrayList<Emprunt>();
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
                    return emprunts.get(rowIndex).getProduit().getDailyPrice();
                case 2:
                    return emprunts.get(rowIndex).getProduit().getClass().getSimpleName();
                case 3:
                    var dateFin = emprunts.get(rowIndex).getDateFin();
                    return ((dateFin.get(Calendar.DAY_OF_MONTH) > 9) ? dateFin.get(Calendar.DAY_OF_MONTH) : ("0" + dateFin.get(Calendar.DAY_OF_MONTH))) + 
                        "/" + ((dateFin.get(Calendar.MONTH) > 8) ? (dateFin.get(Calendar.MONTH) + 1) : ("0" + (dateFin.get(Calendar.MONTH) + 1))) + 
                        "/" + dateFin.get(Calendar.YEAR);
                case 4:
                    return emprunts.get(rowIndex).getCost();
                default:
                    return null;
            }
        }
     
        public void add(Emprunt emprunt) {
            emprunts.add(emprunt);
     
            fireTableRowsInserted(emprunts.size() -1, emprunts.size() -1);
        }
     
        public void remove(int rowIndex) {
            emprunts.remove(rowIndex);
     
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    
        public Emprunt getEmprunt(int index) {
            return emprunts.get(index);
        }
    }
    