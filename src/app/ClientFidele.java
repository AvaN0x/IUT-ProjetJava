package app;

@SuppressWarnings("serial")
public class ClientFidele extends Client {
    /**
     * @param nom of the client
     * @param prenom of the client
     */
    public ClientFidele(String nom, String prenom) {
        super(nom, prenom);
    }

    /**
     * @param id of the client
     * @param nom of the client
     * @param prenom of the client
     */
    public ClientFidele(String id, String nom, String prenom) {
        super(id, nom, prenom);
    }


    @Override
    public String toString() {
        return nom + " " + prenom + " - ƒ";
    }
}
