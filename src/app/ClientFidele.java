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

    @Override
    public String toString() {
        return nom + " " + prenom + " - ƒ";
    }
}
