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

    public Commande(Client client) {
        this(client, Calendar.getInstance());
    }

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
        return cost;
    }
    public double getTotalCost() {
        return getTotalCostNoReduc() * (1 - reduction);
    }
}