package app;

import java.util.Calendar;

public class CD extends SupportNum {
    public Calendar releaseDate;

    public CD(String title, double dailyPrice, Calendar releaseDate) {
        super(title, dailyPrice);
        this.releaseDate = releaseDate;
    }

}