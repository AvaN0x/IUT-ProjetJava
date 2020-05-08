package app;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public abstract class Produit implements Serializable {
    protected String id;
    protected String title;
    protected double dailyPrice;
    protected int quantity;

    public Produit(String title, double dailyPrice, int quantity) {
        this.id = "P" + UUID.randomUUID().toString();
        this.title = title.trim();
        this.dailyPrice = dailyPrice;
        this.quantity = quantity;
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += Math.abs(quantity);
    }


    @Override
    public String toString() {
        return "title='" + getTitle() + "'" +
            ", dailyPrice='" + getDailyPrice() + "'" +
            ", quantity='" + getQuantity() + "'";
    }

    public abstract Object getOption1();
    public abstract void setOption1(Object option1);
}
