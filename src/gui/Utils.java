package gui;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;
import app.Client;
import app.ClientFidele;
import app.Commande;
import app.Produit;

import java.sql.*;

public class Utils {
    static DefaultListModel<Client> clients;
    static TableauProduits produits;
    static TableauCommandes commandes;
    static Settings settings;

    static final String[][] produitsTypes = new String[][] {{"BD", "Auteur"}, 
                                                       {"Roman", "Auteur"}, 
                                                       {"Manuel Scolaire", "Auteur"}, 
                                                       {"Dictionnaire", "Langue"}, 
                                                       {"CD", "Date de sortie"}, 
                                                       {"DVD", "Réalisateur"}};

    static final String savingDir = "data/";

    static LogStream logStream = new LogStream("bin/buche.log");

    /**
     * To translate a calendar to a readable date
     * @param date to translate
     * @return the string with the format : dd/mm/aaaa
     */
    static String dateToString(Calendar date){
        return ((date.get(Calendar.DAY_OF_MONTH) > 9) ? date.get(Calendar.DAY_OF_MONTH) : ("0" + date.get(Calendar.DAY_OF_MONTH))) + 
                "/" + ((date.get(Calendar.MONTH) > 8) ? (date.get(Calendar.MONTH) + 1) : ("0" + (date.get(Calendar.MONTH) + 1))) + 
                "/" + date.get(Calendar.YEAR);
    }

    /**
     * Create a file if he doesn't exist (+ the parent folder)
     * @param file
     * @throws IOException
     */
	static void createFileIfNotExists(File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdir();
        if (file.exists())
            file.createNewFile();
    }
    
    /**
     * Saves the data
     */
    static void save(){
        try{
            var saveFile = new File(Utils.savingDir + "data.ser");
            Utils.createFileIfNotExists(saveFile);
            OutputStream fileStream = new FileOutputStream(saveFile);
            var output = new ObjectOutputStream(fileStream);
            
            output.writeObject(Utils.settings);
            if(Utils.settings.isLocal) {
                output.writeObject(Utils.commandes.getList());
                output.writeObject(Utils.produits.getList());
                output.writeObject(Utils.clients);

                output.close();
                Utils.logStream.Log("Data saved locally");
            } else {
                // TODOsets the orders with DB
                // TODOsets the products with DB
                // TODOsets the clients with DB
            }
        } catch (IOException ex) {
            Utils.logStream.Error(ex);
        }
    }

    /**
     * Load the data
     */
    @SuppressWarnings("unchecked") //! https://stackoverflow.com/a/509230/13257820
    static void load(){
        try{
            InputStream fileStream = new FileInputStream(new File(Utils.savingDir + "data.ser"));
            var input = new ObjectInputStream(fileStream);

            Utils.settings = (Settings) input.readObject();

            if(Utils.settings.isLocal) {
                Utils.commandes.setList((List<Commande>) input.readObject());

                Utils.produits.setList((List<Produit>) input.readObject());

                Utils.clients = (DefaultListModel<Client>) input.readObject();
            } else {
                
                ////T ODO gets the products with DB
                /*
                    var products = SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = "BD";
                    for(var produit : products)
                        produits.add(new BD(produit[0], produit[1], produit[2], produit[3], produit[4]));
                    products = SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = "Roman";
                    for(var produit : products)
                        produits.add(new Roman(produit[0], produit[1], produit[2], produit[3], produit[4]));
                    products = SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = "Manuel Scolaire";
                    for(var produit : products)
                        produits.add(new ManuelScolaire(produit[0], produit[1], produit[2], produit[3], produit[4]));
                    products = SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = "Dictionnaire";
                    for(var produit : products)
                        produits.add(new Dictionnaire(produit[0], produit[1], produit[2], produit[3], produit[4]));
                    products = SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = "CD";
                    for(var produit : products)
                        produits.add(new CD(produit[0], produit[1], produit[2], produit[3], produit[4]));
                    products = SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = "DVD";
                    for(var produit : products)
                        produits.add(new DVD(produit[0], produit[1], produit[2], produit[3], produit[4]));
                    */
                
                ////T ODO gets the clients with DB
                /*
                    var users = SELECT * FROM `clients`;
                    for(var client : users) {
                        if(client[3] == 1)
                            clients.addElement(new ClientFidele(client[0], client[1], client[2]));
                        else
                            clients.addElement(new ClientOccas(client[0], client[1], client[2]));
                    }
                */
                ////T ODO gets the orders with DB
                /*
                    var orders = SELECT * FROM `commandes`;
                    for (var commande : orders) {
                        Commande order;
                        for (var client : clients)
                            if(client.getId() == commande[1]) {
                                new Commande(commande[0], client, commande[2]);
                                break;
                            }
                        var emprunts = SELECT * FROM `emprunts` WHERE id-empr = {commande[3]};
                        for (var emprunt : emprunts)
                            for (var produit : produits)
                                if (produit.getId() == emprunt[3]){
                                    order.addEmprunt(emprunt[0], emprunt[2], produit)
                                    break;
                                }
                    }
                */
                }

            input.close();
            logStream.Log("Data loaded");
        } catch (IOException ex) {
            logStream.Error(ex);
        } catch (ClassNotFoundException ex) {
            logStream.Error(ex);
        }
    }
}

@SuppressWarnings("serial")
class Settings implements Serializable {
    public boolean isLocal;
    private String dbUrl;
    public String dbUser;
    public String dbPass;
    public Locale language;

    public Settings(){
        isLocal = true;
        language = Locale.getDefault();
        resetDB();
    }

    public String getdbUrl() {
        return "jdbc:mysql://" + dbUrl + "?useSSL=false";
    }

    public void setdbUrl(String url){
        dbUrl = url;
    }

    public void resetDB(){
        dbUrl = "localhost";
        dbUser = "root";
        dbPass = "";
    }
}
