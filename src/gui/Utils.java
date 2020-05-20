package gui;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
        logStream.Log(request, "SQL");
        Connection connect; Statement stmt;
        connect = DriverManager.getConnection(settings.getdbUrl(), settings.dbUser, settings.dbPass);
        stmt = connect.createStatement();
        return stmt.executeQuery(request);
    } 

    /**
     * Make a SQL update to the database indicated by the settings
     * @param request in SQL
     * @throws SQLException
     */
    static void SQLupdate(String request) throws SQLException{
        logStream.Log(request, "SQL");
        Connection connect; Statement stmt;
        connect = DriverManager.getConnection(settings.getdbUrl(), settings.dbUser, settings.dbPass);
        stmt = connect.createStatement();
        stmt.executeUpdate(request);
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
                try {
                    var types = new ArrayList<String>();
                    for (var type : produitsTypes) {
                        types.add(type[0].trim());
                    } 
                    for (var produit : produits.getList()) {
                        var products = SQLrequest("SELECT `id-prod` FROM `produits` WHERE `id-prod`=\""+produit.getId()+"\"");
                        products.next();
                        try { products.getString(1); } catch (SQLException e) {
                            SQLupdate(String.format("INSERT INTO `produits` (`id-prod`, `title`, `dailyPrice`, `quantity`, `option1`, `id-types`) VALUES (\"%s\", \"%s\", \""+produit.getDailyPrice()+"\", \"%d\", \"%s\", \"%d\")", produit.getId(), produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getName().substring(4))));
                        }
                    }
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    for (int i=0;i<clients.getSize();i++) {
                        var users = SQLrequest("SELECT `id-cli` FROM `clients` WHERE `id-cli`=\""+clients.get(i).getId()+"\"");
                        users.next();
                        try { users.getString(1); } catch (SQLException e) {
                            SQLupdate(String.format("INSERT INTO `clients` (`id-cli`, `nom`, `prenom`, `isFidel`) VALUES (\"%s\", \"%s\", \"%s\", \"%d\")", clients.get(i).getId(), clients.get(i).getNom(), clients.get(i).getPrenom(), (clients.get(i) instanceof ClientFidele?1:0)));
                        }
                    }
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    for (var commande : commandes.getList()){
                        var orders = SQLrequest("SELECT `id-com` FROM `commandes` WHERE `id-com`=\""+commande.getId()+"\"");
                        orders.next();
                        try { orders.getString(1); } catch (SQLException e) {
                            SQLupdate(String.format("INSERT INTO `commandes` (`id-com`, `id-cli`, `dateCreation`) VALUES (\"%s\",\"%s\",\"%s\")", commande.getId(), commande.getClient().getId(), commande.getDateCreation().get(Calendar.YEAR) + "/" + (commande.getDateCreation().get(Calendar.MONTH) + 1) + "/" +commande.getDateCreation().get(Calendar.DAY_OF_MONTH)));
                        }
                        for (var emprunt : commande.getEmprunts()){
                            var loans = SQLrequest("SELECT `id-com`, `id-empr` FROM `emprunts` WHERE `id-com`=\""+commande.getId()+"\" AND `id-empr`=\""+ emprunt.getId() +"\"");
                            loans.next();
                            try { loans.getString(1); } catch (SQLException e) {
                                SQLupdate(String.format("INSERT INTO `emprunts` (`id-com`, `id-empr`, `dateFin`, `id-prod`) VALUE (\"%s\",\"%s\",\"%s\",\"%s\")", commande.getId(), emprunt.getId(), emprunt.getDateFin().get(Calendar.YEAR) + "/" + (emprunt.getDateFin().get(Calendar.MONTH) + 1) + "/" +emprunt.getDateFin().get(Calendar.DAY_OF_MONTH) , emprunt.getProduit().getId()));
                            }
                        }
                    }
                } catch (SQLException e) {
                    logStream.Error(e);
                }
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

            if(settings.isLocal) {
                commandes.setList((List<Commande>) input.readObject());
            
                produits.setList((List<Produit>) input.readObject());
            
                clients = (DefaultListModel<Client>) input.readObject();
            }
            else {
                try{
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"BD\"");
                    while (products.next()){
                        var bd = new BD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5));
                        logStream.Log(bd.toString(), "SQL");
                        produits.add(bd);
                    }
                    logStream.Log("BD downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"Roman\"");
                    while (products.next()) {
                        var roman = new Roman(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5));
                        logStream.Log(roman.toString(), "SQL");
                        produits.add(roman);
                    }
                    logStream.Log("Roman downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                } 
                try {
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"Manuel Scolaire\"");
                    while (products.next()){
                        var prod = new ManuelScolaire(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5));
                        logStream.Log(prod.toString(), "SQL");
                        produits.add(prod);
                    }
                    logStream.Log("Manuel downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"Dictionnaire\"");
                    while (products.next()) {
                        var dico = new Dictionnaire(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5));
                        logStream.Log(dico.toString(), "SQL");
                        produits.add(dico);
                    }
                    logStream.Log("Dico downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"CD\"");
                    while (products.next()){
                        //TODO calendar from products.getString(5)
                        var cd = new CD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), Calendar.getInstance());
                        logStream.Log(cd.toString(), "SQL");
                        produits.add(cd);
                    }
                    logStream.Log("CD downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types` WHERE categ = \"DVD\"");
                    while (products.next()){
                        var dvd = new DVD(products.getString(1), products.getString(2), products.getDouble(3), products.getInt(4), products.getString(5));
                        logStream.Log(dvd.toString(), "SQL");
                        produits.add(dvd);
                    }
                    logStream.Log("DVD downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    var users = SQLrequest("SELECT * FROM `clients`");
                    while (users.next()){
                        Client cli;
                        if (users.getInt(3) == 1)
                            cli = new ClientFidele(users.getString(1), users.getString(2), users.getString(3));
                        else
                            cli = new ClientOccas(users.getString(1), users.getString(2), users.getString(3));
                        logStream.Log(cli.toString(), "SQL");
                        clients.addElement(cli);
                    }
                    logStream.Log("Clients downloaded");
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    var orders = SQLrequest("SELECT * FROM `commandes`");
                    while (orders.next()){
                        Commande order = null;
                        for (int i=0;i<clients.getSize();i++)
                            if(clients.get(i).getId() == orders.getString(2)) {
                                var cal = Calendar.getInstance();
                                cal.setTime(orders.getDate(3));
                                order = new Commande(orders.getString(1), clients.get(i), cal);
                                break;
                            }
                        var loans = SQLrequest("SELECT * FROM `emprunts` WHERE `id-empr` = \""+ orders.getString(4) + "\"");
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
        dbBase = "";
    }
}
