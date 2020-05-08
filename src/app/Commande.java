package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("serial")
public class Commande implements Serializable  {
    protected String id;
    protected Client client;
    protected Calendar dateCreation;
    protected double reduction;
    protected ArrayList<Emprunt> emprunts;

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
    
    public void addEmprunt(Calendar dateFin, Produit produit) {
        Emprunt emprunt = new Emprunt(dateCreation, dateFin, produit);
        emprunts.add(emprunt);
    }

    public double getTotalCostNoReduc() {
        double cost = 0;
        for(Emprunt e : emprunts) {
            cost += e.getCost();
        }
        return Math.round(cost * 100) / 100.;
    }
    public double getTotalCost() {
        return Math.round(getTotalCostNoReduc() * (1 - reduction) * 100) / 100.;
    }

    public boolean editable() {
        long oneDay = 86400000;
        if (Calendar.getInstance().getTimeInMillis() <= (getDateCreation().getTimeInMillis() + 2 * oneDay)) 
            return true;
        else
            return false;
    }

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

    public String getId() {
        return this.id;
    }

	public void setClient(Client client) {
        this.client = client;
        if (client instanceof ClientFidele)
            this.reduction = .1;
        else
            this.reduction = 0;

    }
    public void setDateCreation(Calendar dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void emptyEmprunts() {
        emprunts = new ArrayList<Emprunt>();
    }
}
