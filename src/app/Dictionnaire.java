package app;

public class Dictionnaire extends Document {
    protected String langue;

    public Dictionnaire(String title, double dailyPrice, String langue) {
        super(title, dailyPrice);
        this.langue = langue.trim();
    }
}