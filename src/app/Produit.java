package app;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public abstract class Produit implements Serializable {
    protected String id;
    protected String title;
    protected double dailyPrice;
    protected int quantity;

    /**
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     */
    public Produit(String title, double dailyPrice, int quantity) {
        this.id = "P" + UUID.randomUUID().toString();
        this.title = title.trim();
        this.dailyPrice = dailyPrice;
        this.quantity = quantity;
    }

    /**
     * @param id of the product
     * @param title of the product
     * @param dailyPrice of the product
     * @param quantity of the product
     */
    public Produit(String id, String title, double dailyPrice, int quantity) {
        this.id = id;
        this.title = title.trim();
        this.dailyPrice = dailyPrice;
        this.quantity = quantity;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id of the product
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title of the product
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the daily price
     */
    public double getDailyPrice() {
        return this.dailyPrice;
    }

    /**
     * @param dailyPrice of the product
     */
    public void setDailyPrice(double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * @param quantity of the product
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @param quantity of the product
     */
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
