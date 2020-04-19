package app;

import java.util.Date;

public class CD extends SupportNum {
    public Date releaseDate;

    public CD(String title, double dailyPrice, Date releaseDate) {
        super(title, dailyPrice);
        this.releaseDate = releaseDate;
    }

}