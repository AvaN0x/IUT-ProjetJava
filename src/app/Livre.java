package app;

public abstract class Livre extends Document {
    protected String auteur;

    public Livre(String title, double dailyPrice, String auteur) {
        super(title, dailyPrice);
        this.auteur = auteur.trim();
    }
}