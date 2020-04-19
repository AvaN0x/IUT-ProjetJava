package app;

public class DVD extends SupportNum {
    protected String realisateur;

    public DVD(String title, double dailyPrice, String realisateur) {
        super(title, dailyPrice);
        this.realisateur = realisateur;
    }

}