package app;

@SuppressWarnings("serial")
public abstract class Document extends Produit {
    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     */
    public Document(String title, double dailyPrice, int quantity) {
        super(title, dailyPrice, quantity);
    }

}
