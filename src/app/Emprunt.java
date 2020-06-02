package app;

import java.io.Serializable;
import java.time.Duration;
import java.util.Calendar;
import java.util.UUID;

@SuppressWarnings("serial")
public class Emprunt implements Serializable {
    private String id;
    private Calendar dateDebut; // égale a la date de création de la commande
    private Calendar dateFin;
    private Produit produit;

    /**
     * @param dateDebut of the loaning
     * @param dateFin of the loaning
     * @param produit concerned by the loaning
     */
    public Emprunt(Calendar dateDebut, Calendar dateFin, Produit produit) {
        this.id = "L" + UUID.randomUUID().toString();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.produit = produit;
    }

    /**
     * @param id of the products
     * @param dateDebut of the loaning
     * @param dateFin of the loaning
     * @param produit concerned by the loaning
     */
    public Emprunt(String id, Calendar dateDebut, Calendar dateFin, Produit produit) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.produit = produit;
    }


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return the cost
     */
    public double getCost() {
        int amountOfDays = (int) Duration.between(dateDebut.toInstant(), dateFin.toInstant()).toDays();
        return Math.round(amountOfDays * produit.getDailyPrice() * 100) / 100.;
    }

    /**
     * @return the product
     */
    public Produit getProduit() {
        return this.produit;
    }

    /**
     * @return the calendar of the end of the loaning
     */
    public Calendar getDateFin() {
        return this.dateFin;
    }
}
