package app;

import java.util.ArrayList;
import java.util.Date;

public class Commande {
    protected String id;
    protected Date dateCreation;
    protected int reduction;    // peut-etre changer le type?
    protected ArrayList<Emprunt> emprunts;

}