package app;

import java.util.UUID;

public abstract class Produit {
    protected String id;
    protected String title;
    protected double dailyPrice;

    public Produit(String title, double dailyPrice) {
        this.id = "P" + UUID.randomUUID().toString();
        this.title = title.trim();
        this.dailyPrice = dailyPrice;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDailyPrice() {
        return this.dailyPrice;
    }

    public void setDailyPrice(double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

}
