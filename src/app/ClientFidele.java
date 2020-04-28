package app;

@SuppressWarnings("serial")
public class ClientFidele extends Client {
    public ClientFidele(String nom, String prenom) {
        super(nom, prenom);
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " - ƒ";
    }
}
