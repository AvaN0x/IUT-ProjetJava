package app;

@SuppressWarnings("serial")
public class Dictionnaire extends Document {
    protected String langue;

    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     * @param langue of the dictionary
     */
    public Dictionnaire(String title, double dailyPrice, int quantity, String langue) {
        super(title, dailyPrice, quantity);
        this.langue = langue.trim();
    }

    /**
     * @return the language of the dictionary
     */
    public String getOption1() {
        return langue;
    }

    /**
     * Sets the language of the dictionary
     * @param option1 language of the dictionary
     */
    public void setOption1(Object option1) {
        langue = (String) option1;
    }

}
