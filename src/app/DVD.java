package app;

public class DVD extends SupportNum {
    protected String realisateur;

    public DVD(String title, double dailyPrice, int quantity, String realisateur) {
        super(title, dailyPrice, quantity);
        this.realisateur = realisateur;
    }

}