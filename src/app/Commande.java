package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("serial")
public class Commande implements Serializable {
    private String id;
    private Client client;
    private Calendar dateCreation;
    private double reduction;
    private ArrayList<Emprunt> emprunts;

    /**
     * @param client       who made the order
     * @param dateCreation of the order
     */
    public Commande(Client client, Calendar dateCreation) {
        this.id = "C" + UUID.randomUUID().toString();
        this.client = client;
        this.dateCreation = dateCreation;
        if (client instanceof ClientFidele)
            this.reduction = .1;
        else
            this.reduction = 0;

        this.emprunts = new ArrayList<Emprunt>();
    }

    /**
     * @param id           of the order
     * @param client       who made the order
     * @param dateCreation of the order
     */
    public Commande(String id, Client client, Calendar dateCreation) {
        this.id = id;
        this.client = client;
        this.dateCreation = dateCreation;
        if (client instanceof ClientFidele)
            this.reduction = .1;
        else
            this.reduction = 0;

        this.emprunts = new ArrayList<Emprunt>();
    }


    /**
     * Create a new loaning
     * 
     * @param dateFin of the loaning
     * @param produit of the loaning
     */
    public void addEmprunt(Calendar dateFin, Produit produit) {
        Emprunt emprunt = new Emprunt(dateCreation, dateFin, produit);
        emprunts.add(emprunt);
    }

    /**
     * Create a new loaning
     * 
     * @param idEmprunt of the loaning
     * @param dateFin of the loaning
     * @param produit of the loaning
     */
    public void addEmprunt(String idEmprunt, Calendar dateFin, Produit produit) {
        Emprunt emprunt = new Emprunt(idEmprunt, dateCreation, dateFin, produit);
        emprunts.add(emprunt);
    }

    /**
     * @return the total cost w/o the reduction based on the client
     */
    public double getTotalCostNoReduc() {
        double cost = 0;
        for (Emprunt e : emprunts) {
            cost += e.getCost();
        }
        return Math.round(cost * 100) / 100.;
    }

    /**
     * @return the total cost
     */
    public double getTotalCost() {
        return Math.round(getTotalCostNoReduc() * (1 - reduction) * 100) / 100.;
    }

    /**
     * @return if the loaning is editable or not
     */
    public boolean editable() {
        long oneDay = 86400000;
        if (Calendar.getInstance().getTimeInMillis() <= (getDateCreation().getTimeInMillis() + 2 * oneDay))
            return true;
        return false;
    }

    /**
     * ? Help @Avan0x
     * TODO: Finish that doc
     * @param date
     * @param prodStock
     */
    public void getProdUsedAt(Calendar date, HashMap<String, Integer> prodStock) {
        if (date.getTimeInMillis() >= dateCreation.getTimeInMillis()) {
            for (Emprunt e : emprunts) {
                if (e.getDateFin().getTimeInMillis() > date.getTimeInMillis())
                    prodStock.put(e.getProduit().getId(), prodStock.get(e.getProduit().getId()) - 1);
            }
        }
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @return the dateCreation
     */
    public Calendar getDateCreation() {
        return dateCreation;
    }

    /**
     * @return the reduction
     */
    public double getReduction() {
        return reduction;
    }

    /**
     * @return the emprunts
     */
    public ArrayList<Emprunt> getEmprunts() {
        return emprunts;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param client who made the order
     */
    public void setClient(Client client) {
        this.client = client;
        if (client instanceof ClientFidele)
            this.reduction = .1;
        else
            this.reduction = 0;

    }

    /**
     * @param dateCreation of the corder
     */
    public void setDateCreation(Calendar dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Clears the loaning list
     */
    public void emptyEmprunts() {
        emprunts = new ArrayList<Emprunt>();
    }
}
