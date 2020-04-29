package app;

import java.io.Serializable;
import java.time.Duration;
import java.util.Calendar;
import java.util.UUID;

@SuppressWarnings("serial")
public class Emprunt implements Serializable {
    protected String id;
    protected Calendar dateDebut; // égale a la date de création de la commande
    protected Calendar dateFin;
    protected Produit produit;

    public Emprunt(Calendar dateDebut, Calendar dateFin, Produit produit) {
        this.id = "L" + UUID.randomUUID().toString();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.produit = produit;
        // produit.emprunter();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    public double getCost() {
        int amountOfDays = (int) Duration.between(dateDebut.toInstant(), dateFin.toInstant()).toDays();
        return Math.round(amountOfDays * produit.getDailyPrice() * 100) / 100.;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public Calendar getDateFin() {
        return this.dateFin;
    }
}
