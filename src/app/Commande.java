package app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Commande {
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
    
    public boolean addEmprunt(Calendar dateFin, Produit produit) {
        if (produit.getDispo() > 0) {
            Emprunt emprunt = new Emprunt(dateCreation, dateFin, produit);
            emprunts.add(emprunt);
            produit.emprunter();
            return true;
        }
        else
            return false;
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

    // seulement pour les tests en console
    public void consoleWriteEmprunts() {
        System.out.println("Liste des emprunts de la commande :");
        for(Emprunt e : emprunts) {
            System.out.println(e.produit);
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
        for (Emprunt emprunt : emprunts)
            emprunt.getProduit().rendre();
        emprunts = new ArrayList<Emprunt>();
    }
}
