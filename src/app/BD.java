package app;

@SuppressWarnings("serial")
public class BD extends Livre {
    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity in stock
     * @param auteur of the book
     */
    public BD(String title, double dailyPrice, int quantity, String auteur) {
        super(title, dailyPrice, quantity, auteur);
    }

    /**
     * @param id of the product
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity in stock
     * @param auteur of the book
     */
    public BD(String id, String title, double dailyPrice, int quantity, String auteur) {
        super(id, title, dailyPrice, quantity, auteur);
    }
}
