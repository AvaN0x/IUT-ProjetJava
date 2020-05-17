package app;

@SuppressWarnings("serial")
public class Roman extends Livre {
    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     * @param auteur of the book
     */
    public Roman(String title, double dailyPrice, int quantity, String auteur) {
        super(title, dailyPrice, quantity, auteur);
    }

}
