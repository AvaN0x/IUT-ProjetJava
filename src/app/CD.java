package app;

import java.util.Calendar;

public class CD extends SupportNum {
    public Calendar releaseDate;

    public CD(String title, double dailyPrice, int quantity, Calendar releaseDate) {
        super(title, dailyPrice, quantity);
        this.releaseDate = releaseDate;
    }

}