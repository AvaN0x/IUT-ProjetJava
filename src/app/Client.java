package app;

import java.io.Serializable;
import java.util.UUID;

public abstract class Client implements Serializable {
    protected String id;
    protected String nom;
    protected String prenom;

    public Client(String nom, String prenom) {
        this.id = "U" + UUID.randomUUID().toString();
        this.nom = nom.trim().toUpperCase();
        prenom.trim();
        this.prenom = prenom.substring(0, 1).toUpperCase() + prenom.substring(1);
    }


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}
