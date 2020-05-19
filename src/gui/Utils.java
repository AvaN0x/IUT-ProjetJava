package gui;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;
import app.*;

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
     * Make a SQL request to the database indicated by the settings
     * @param request in SQL
     * @return the ResultSet of the resquest
     * @throws SQLException
     */
    static ResultSet SQLrequest(String request) throws SQLException{
        Connection connect; Statement stmt;
        connect = DriverManager.getConnection(settings.getdbUrl(), settings.dbUser, settings.dbPass);
        stmt = connect.createStatement();
        return stmt.executeQuery(request);
    } 
    
    /**
     * Saves the data
     * @return the success (or not) of the operation
     */
    static boolean save(){
        try{
            var saveFile = new File(savingDir + "data.ser");
            createFileIfNotExists(saveFile);
            OutputStream fileStream = new FileOutputStream(saveFile);
            var output = new ObjectOutputStream(fileStream);
            
            output.writeObject(Utils.settings);
            output.writeObject(commandes.getList());
            output.writeObject(produits.getList());
            output.writeObject(clients);
            
            output.close();
            logStream.Log("Data saved locally");

            if(!settings.isLocal) {
                // TODO sets with db
            }
        } catch (IOException ex) {
            logStream.Error(ex);
            return false;
        }
        return true;
    }

    /**
     * Load the data
     * @return the success (or not) of the operation
     */
    @SuppressWarnings("unchecked") //! https://stackoverflow.com/a/509230/13257820
    static boolean load(){
        try{
            InputStream fileStream = new FileInputStream(new File(Utils.savingDir + "data.ser"));
            var input = new ObjectInputStream(fileStream);

            settings = (Settings) input.readObject();

            if(!settings.isLocal) {
                commandes.setList((List<Commande>) input.readObject());
            
                produits.setList((List<Produit>) input.readObject());
            
                clients = (DefaultListModel<Client>) input.readObject();
            }
            else {
                try{
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"BD\"");
                    while (products.next())
                        produits.add(new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5)));
                    logStream.Log("BD downloaded");
                    
                    products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"Roman\"");
                    while (products.next())
                        produits.add(new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5)));
                    logStream.Log("Roman downloaded");
                    
                    products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"Manuel Scolaire\"");
                    while (products.next())
                        produits.add(new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5)));
                    logStream.Log("Manuel downloaded");
                    
                    products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"Dictionnaire\"");
                    while (products.next())
                        produits.add(new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5)));
                    logStream.Log("Dico downloaded");

                    products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"CD\"");
                    while (products.next())
                        produits.add(new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5)));
                    logStream.Log("CD downloaded");

                    products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"DVD\"");
                    while (products.next())
                        produits.add(new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5)));
                    logStream.Log("DVD downloaded");

                    var users = SQLrequest("SELECT * FROM `clients`");
                    while (users.next()){
                        if (users.getInt(3) == 1)
                            clients.addElement(new ClientFidele(users.getString(1), users.getString(2), users.getString(3)));
                            else
                            clients.addElement(new ClientOccas(users.getString(1), users.getString(2), users.getString(3)));
                    }
                    logStream.Log("Clients downloaded");

                    var orders = SQLrequest("SELECT * FROM `commandes`");
                    while (orders.next()){
                        Commande order = null;
                        for (int i=1;i<clients.getSize();i++)
                            if(clients.get(i).getId() == orders.getString(2)) {
                                var cal = Calendar.getInstance();
                                cal.setTime(orders.getDate(3));
                                order = new Commande(orders.getString(1), clients.get(i), cal);
                                break;
                            }
                        var loans = SQLrequest("SELECT * FROM `emprunts` WHERE id-empr = \""+ orders.getString(4) + "\"");
                        while(loans.next()){
                            for (var produit : produits.getList())
                                if (produit.getId() == loans.getString(4)) {
                                    var cal = Calendar.getInstance();
                                    cal.setTime(loans.getDate(3));
                                    order.addEmprunt(loans.getString(1), cal, produit);
                                }
                        }
                        logStream.Log("loans of " + orders.getString(1) + " downloaded");
                        commandes.add(order);
                    }
                    logStream.Log("Commandes downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                    return false;
                }
            }
            
            input.close();
            logStream.Log("Data loaded");
            return true;
        } catch (IOException ex) {
            logStream.Error(ex);
            return false;
        } catch (ClassNotFoundException ex) {
            logStream.Error(ex);
            return false;
        }
    }
}

@SuppressWarnings("serial")
class Settings implements Serializable {
    public boolean isLocal;
    public InetAddress dbUrl;
    public String dbUser;
    public String dbPass;
    public String dbBase;
    public Locale language;

    public Settings(){
        isLocal = true;
        language = Locale.getDefault();
        resetDB();
    }

    /**
     * To get the jdbc url to connect to an MySQL server
     * ! DO NOT USE WHEN YOU ONLY WANT THE ADRESS TO CONNECT
     * @return the jdbc url
     */
    public String getdbUrl() {
        return "jdbc:mysql://" + dbUrl.getHostAddress() + "/" + dbBase;
    }

    /**
     * To reset credentials of the DB
     */
    public void resetDB(){
        try {
            dbUrl = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            dbUrl = null;
        }
        dbUser = "root";
        dbPass = "";
    }
}
