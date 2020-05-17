package app;

@SuppressWarnings("serial")
public class DVD extends SupportNum {
    protected String realisateur;

    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     * @param realisateur of the DVD
     */
    public DVD(String title, double dailyPrice, int quantity, String realisateur) {
        super(title, dailyPrice, quantity);
        this.realisateur = realisateur;
    }

    /**
     * @return the film maker
     */
    public String getOption1() {
        return realisateur;
    }

    /**
     * Sets the film maker
     * @param option1 the film maker's name
     */
    public void setOption1(Object option1) {
        realisateur = (String) option1;
    }

}
