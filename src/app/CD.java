package app;

import java.util.Calendar;

@SuppressWarnings("serial")
public class CD extends SupportNum {
    public Calendar releaseDate;

    public CD(String title, double dailyPrice, int quantity, Calendar releaseDate) {
        super(title, dailyPrice, quantity);
        this.releaseDate = releaseDate;
    }

    public String getOption1() {
        return ((releaseDate.get(Calendar.DAY_OF_MONTH) > 9) ? releaseDate.get(Calendar.DAY_OF_MONTH) : ("0" + releaseDate.get(Calendar.DAY_OF_MONTH))) + 
        "/" + ((releaseDate.get(Calendar.MONTH) > 8) ? (releaseDate.get(Calendar.MONTH) + 1) : ("0" + (releaseDate.get(Calendar.MONTH) + 1))) + 
        "/" + releaseDate.get(Calendar.YEAR);
    }
}
