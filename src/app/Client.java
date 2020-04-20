package app;

import java.util.UUID;

public abstract class Client {
    protected String id;
    protected String nom;
    protected String prenom;

    public Client(String nom, String prenom) {
        this.id = "U" + UUID.randomUUID().toString();
        this.nom = nom.trim();
        this.prenom = prenom.trim();
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
