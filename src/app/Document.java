package app;

@SuppressWarnings("serial")
public abstract class Document extends Produit {

    public Document(String title, double dailyPrice, int quantity) {
        super(title, dailyPrice, quantity);
    }

}
