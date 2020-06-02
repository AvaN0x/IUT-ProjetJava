package app;

@SuppressWarnings("serial")
public abstract class Livre extends Document {
    private String auteur;

    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     * @param auteur of the book
     */
    public Livre(String title, double dailyPrice, int quantity, String auteur) {
        super(title, dailyPrice, quantity);
        this.auteur = auteur.trim();
    }

    /**
     * @param id of the product
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     * @param auteur of the book
     */
    public Livre(String id, String title, double dailyPrice, int quantity, String auteur) {
        super(id, title, dailyPrice, quantity);
        this.auteur = auteur.trim();
    }

    /**
     * @return the author
     */
    public String getOption1() {
        return auteur;
    }

    /**
     * Sets the authors
     * @param option1 the author name
     */
    public void setOption1(Object option1) {
        auteur = (String) option1;
    }

}
