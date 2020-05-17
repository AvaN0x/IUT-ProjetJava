package app;

@SuppressWarnings("serial")
public class ClientOccas extends Client {
    /**
     * @param nom    of the client
     * @param prenom of the client
     */
    public ClientOccas(String nom, String prenom) {
        super(nom, prenom);
    }

    // TODO doc
    public ClientOccas(String id, String nom, String prenom) {
        super(id, nom, prenom);
    }

}
