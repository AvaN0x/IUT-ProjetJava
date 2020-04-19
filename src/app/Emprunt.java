package app;

import java.time.Duration;
import java.util.Calendar;

public class Emprunt {
    protected Calendar dateDebut; // égale a la date de création de la commande
    protected Calendar dateFin;
    protected Produit produit;

    public Emprunt(Calendar dateDebut, Calendar dateFin, Produit produit) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.produit = produit;
    }
    
    public double getCost() {
        int amountOfDays = (int) Duration.between(dateDebut.toInstant(), dateFin.toInstant()).toDays();
        return amountOfDays * produit.getDailyPrice();
    }
}