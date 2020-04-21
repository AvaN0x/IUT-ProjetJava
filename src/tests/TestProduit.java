package tests;

import java.util.Calendar;

import app.*;

public class TestProduit {

    public static void main(String[] args) {

        // test de creation de commande 
        ClientFidele cltf = new ClientFidele("nom", "prenom");

        Commande cde = new Commande(cltf);
        DVD dvd1 = new DVD("dvd1", 1, 2, "qqun");
        DVD dvd2 = new DVD("dvd2", 2, 1, "encore qqun");

        System.out.println("\nStock des objets : \n" + dvd1 + "\n" + dvd2);

        Calendar dateFin = Calendar.getInstance();
        dateFin.set(Calendar.YEAR, dateFin.get(Calendar.YEAR) + 1);
        cde.addEmprunt(dateFin, dvd1);
        cde.addEmprunt(dateFin, dvd2);
        cde.addEmprunt(dateFin, dvd1);

        System.out.println("\nPrix sans reduc = " + cde.getTotalCostNoReduc() + "\nPrix final = " + cde.getTotalCost());

        System.out.println("\nStock des objets : \n" + dvd1 + "\n" + dvd2);

        System.out.println();
        cde.consoleWriteEmprunts();

        // verification de date

        int d = 2;
        int m = 1;
        int y = 2020;

        if (y >= 1970 && m >= 0 && m <= 11) {
            Calendar dateCheck = Calendar.getInstance();
            dateCheck.set(Calendar.MONTH, m);
            dateCheck.set(Calendar.YEAR, y);
            if (d >= dateCheck.getActualMinimum(Calendar.DAY_OF_MONTH) && d <= dateCheck.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                dateCheck.set(Calendar.DAY_OF_MONTH, d);
                System.out.println("date valide");
                System.out.println(((dateCheck.get(Calendar.DAY_OF_MONTH) > 8) ? dateCheck.get(Calendar.DAY_OF_MONTH) : ("0" + dateCheck.get(Calendar.DAY_OF_MONTH))) + 
                                "/" + ((dateCheck.get(Calendar.MONTH) > 8) ? (dateCheck.get(Calendar.MONTH) + 1) : ("0" + (dateCheck.get(Calendar.MONTH) + 1))) + 
                                "/" + dateCheck.get(Calendar.YEAR));
            } else {
                System.out.println("date non valide");
            }
        } else {
            System.out.println("date non valide");
        }
    }
}
