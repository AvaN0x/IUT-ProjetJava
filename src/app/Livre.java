package app;

public abstract class Livre extends Document {
    protected String auteur;

    public Livre(String title, double dailyPrice, int quantity, String auteur) {
        super(title, dailyPrice, quantity);
        this.auteur = auteur.trim();
    }
}