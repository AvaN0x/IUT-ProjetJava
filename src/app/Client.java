package app;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public abstract class Client implements Serializable {
    private String id;
    private String nom;
    private String prenom;

    /**
     * @param nom of the client
     * @param prenom of the client
     */
    public Client(String nom, String prenom) {
        this.id = "U" + UUID.randomUUID().toString();
        this.nom = nom.trim().toUpperCase();
        prenom.trim();
        this.prenom = prenom.substring(0, 1).toUpperCase() + prenom.substring(1);
    }

    /**
     * @param id of the client
     * @param nom of the client
     * @param prenom of the client
     */
    public Client(String id, String nom, String prenom) {
        this.id = id;
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
     * @return the name
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the first name
     */
    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}
