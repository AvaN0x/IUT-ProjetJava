package app;

import java.util.Calendar;

@SuppressWarnings("serial")
public class CD extends SupportNum {
    public Calendar releaseDate;

    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity in stock
     * @param releaseDate of the CD
     */
    public CD(String title, double dailyPrice, int quantity, Calendar releaseDate) {
        super(title, dailyPrice, quantity);
        this.releaseDate = releaseDate;
    }

    /**
     * @param id of the product
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity in stock
     * @param releaseDate of the CD
     */
    public CD(String id, String title, double dailyPrice, int quantity, Calendar releaseDate) {
        super(id, title, dailyPrice, quantity);
        this.releaseDate = releaseDate;
    }

    /**
     * @return the date of the release (format dd/mm/aaaa)
     */
    public String getOption1() {
        return ((releaseDate.get(Calendar.DAY_OF_MONTH) > 9) ? releaseDate.get(Calendar.DAY_OF_MONTH) : ("0" + releaseDate.get(Calendar.DAY_OF_MONTH))) + 
        "/" + ((releaseDate.get(Calendar.MONTH) > 8) ? (releaseDate.get(Calendar.MONTH) + 1) : ("0" + (releaseDate.get(Calendar.MONTH) + 1))) + 
        "/" + releaseDate.get(Calendar.YEAR);
    }
    
    /**
     * sets the date of the release
     * @param option1 the calendar of the release
     */
    public void setOption1(Object option1) {
        releaseDate = (Calendar) option1;
    }

}
