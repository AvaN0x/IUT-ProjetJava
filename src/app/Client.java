package app;

public abstract class Client {
    protected String id;
    protected String nom;
    protected String prenom;

    public Client(String nom, String prenom) {
        // TODO: Generate the ID
        this.nom = nom;
        this.prenom = prenom;
    }

    public Client(String id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
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
}
