package app;

public class Dictionnaire extends Document {
    protected String langue;

    public Dictionnaire(String title, double dailyPrice, int quantity, String langue) {
        super(title, dailyPrice, quantity);
        this.langue = langue.trim();
    }

    public String getOption1() {
        return langue;
    }
}