package app;

@SuppressWarnings("serial")
public class DVD extends SupportNum {
    protected String realisateur;

    public DVD(String title, double dailyPrice, int quantity, String realisateur) {
        super(title, dailyPrice, quantity);
        this.realisateur = realisateur;
    }

    public String getOption1() {
        return realisateur;
    }

    public void setOption1(Object option1) {
        realisateur = (String) option1;
    }

}
