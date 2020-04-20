package tests;

import java.util.Calendar;

import app.*;

public class TestProduit {

    public static void main(String[] args) {
        ClientFidele cltf = new ClientFidele("nom", "prenom");

        Commande cde = new Commande(cltf);
        DVD dvd1 = new DVD("qqch", 1, 2, "qqun");
        DVD dvd2 = new DVD("qqch d'autre", 2, 1, "encore qqun");

        System.out.println(dvd1 + "\n" + dvd2);


        Calendar dateFin = Calendar.getInstance();
        dateFin.set(Calendar.YEAR, dateFin.get(Calendar.YEAR) + 1);
        cde.addEmprunt(dateFin, dvd1);
        cde.addEmprunt(dateFin, dvd2);

        System.out.println("Prix sans reduc = " + cde.getTotalCostNoReduc() + "\nPrix final = " + cde.getTotalCost());
        System.out.println(dvd1 + "\n" + dvd2);
    }
}
