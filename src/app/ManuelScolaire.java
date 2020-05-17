package app;

@SuppressWarnings("serial")
public class ManuelScolaire extends Livre {
    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     * @param auteur of the book
     */
    public ManuelScolaire(String title, double dailyPrice, int quantity, String auteur) {
        super(title, dailyPrice, quantity, auteur);
    }

    //TODO doc
    public ManuelScolaire(String id, String title, double dailyPrice, int quantity, String auteur) {
        super(id, title, dailyPrice, quantity, auteur);
    }

}
