package app;

@SuppressWarnings("serial")
public abstract class Livre extends Document {
    protected String auteur;

    public Livre(String title, double dailyPrice, int quantity, String auteur) {
        super(title, dailyPrice, quantity);
        this.auteur = auteur.trim();
    }
    public String getOption1() {
        return auteur;
    }
    public void setOption1(Object option1) {
        auteur = (String) option1;
    }

}
