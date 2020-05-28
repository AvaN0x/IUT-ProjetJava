package gui;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import org.javatuples.Pair;
import org.reflections.Reflections;

import app.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
    static DefaultListModel<Client> clients;
    static TableauProduits produits;
    static TableauCommandes commandes;
    static Settings settings;

    static final String savingDir = "data/";

    static final String[] dbIgnored = {"mysql","performance_schema","information_schema","test","phpmyadmin"};

    static LogStream logStream = new LogStream("bin/buche.log");

    static Lang lang;

    /**
     * To get all the types of products + theirs options. The return is sorted
     * @return all the types + their options
     */
    static List<Pair<Class<? extends Produit>, Field[]>> getTypes() {
        Reflections reflections = new Reflections("app");    
        var classes = reflections.getSubTypesOf(Produit.class);
        var result = new ArrayList<Pair<Class<? extends Produit>, Field[]>>();

        for (var class1 : classes) { // for each class of the package app
            if(!Modifier.isAbstract(class1.getModifiers())){ // if its not an abstract class
                var fields = class1.getDeclaredFields(); // get all the fields
                //TODO : better detection of book (no custom field)
                if(class1.getSuperclass() == Livre.class) // if it's a book
                    fields = Livre.class.getDeclaredFields(); // get all the fields of a book
                result.add(Pair.with(class1, fields));
            }
        }
        Collections.sort(result, (e1, e2) -> {
            return e1.getValue0().getSimpleName().compareTo(e2.getValue0().getSimpleName());
        });
        return result;
    }

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
        connect = DriverManager.getConnection(settings.getdbUrl(), settings.dbUser, new String(settings.dbPass));
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
        connect = DriverManager.getConnection(settings.getdbUrl(), settings.dbUser, new String(settings.dbPass));
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
                try {
                    var types = new ArrayList<String>();
                    for (var type : getTypes()) {
                        types.add(type.getValue0().getSimpleName());
                    } 
                    for (var produit : produits.getList()) {
                        var products = SQLrequest("SELECT `id-prod` FROM `produits` WHERE `id-prod`=\""+produit.getId()+"\"");
                        products.next();
                        try { products.getString(1); } catch (SQLException e) { // The product doesn't exist
                            try {
                                SQLupdate(String.format("INSERT INTO `produits` (`id-prod`, `title`, `dailyPrice`, `quantity`, `option1`, `id-types`) VALUES (\"%s\", \"%s\", \""+produit.getDailyPrice()+"\", \"%d\", \"%s\", \"%d\")", produit.getId(), produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getSimpleName())));
                            } catch (SQLIntegrityConstraintViolationException exc) {
                                try {
                                    SQLupdate(String.format("INSERT INTO `types` (`id-types`, `categ`) VALUES (\"%d\",\"%s\")", types.indexOf(produit.getClass().getSimpleName()), produit.getClass().getSimpleName()));
                                    SQLupdate(String.format("INSERT INTO `produits` (`id-prod`, `title`, `dailyPrice`, `quantity`, `option1`, `id-types`) VALUES (\"%s\", \"%s\", \""+produit.getDailyPrice()+"\", \"%d\", \"%s\", \"%d\")", produit.getId(), produit.getTitle(), produit.getQuantity(), produit.getOption1(), types.indexOf(produit.getClass().getSimpleName())));
                                } catch (SQLException ex) {
                                    logStream.Error(ex);
                                }
                            } catch (SQLException ex) {
                                logStream.Error(ex);
                            }
                        }
                    }
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    for (int i=0;i<clients.getSize();i++) {
                        var users = SQLrequest("SELECT `id-cli` FROM `clients` WHERE `id-cli`=\""+clients.get(i).getId()+"\"");
                        users.next();
                        try { users.getString(1); } catch (SQLException e) { // The client doesn't exist
                            try {
                                SQLupdate(String.format("INSERT INTO `clients` (`id-cli`, `nom`, `prenom`, `isFidel`) VALUES (\"%s\", \"%s\", \"%s\", \"%d\")", clients.get(i).getId(), clients.get(i).getNom(), clients.get(i).getPrenom(), (clients.get(i) instanceof ClientFidele ? 1 : 0)));    
                            }
                            catch (SQLException ex) {
                                logStream.Error(ex);
                            }
                        }
                    }
                } catch (SQLException e) {
                    logStream.Error(e);
                }
                try {
                    for (var commande : commandes.getList()){
                        var orders = SQLrequest("SELECT `id-com` FROM `commandes` WHERE `id-com`=\""+commande.getId()+"\"");
                        orders.next();
                        try { orders.getString(1); } catch (SQLException e) { // The order doesn't exist
                            try {
                                SQLupdate(String.format("INSERT INTO `commandes` (`id-com`, `id-cli`, `dateCreation`) VALUES (\"%s\",\"%s\",\"%s\")", commande.getId(), commande.getClient().getId(), commande.getDateCreation().get(Calendar.YEAR) + "/" + (commande.getDateCreation().get(Calendar.MONTH) + 1) + "/" +commande.getDateCreation().get(Calendar.DAY_OF_MONTH)));
                            }
                            catch (SQLException ex) {
                                logStream.Error(ex);
                            }
                        }
                        for (var emprunt : commande.getEmprunts()){
                            var loans = SQLrequest("SELECT `id-com`, `id-empr` FROM `emprunts` WHERE `id-com`=\""+commande.getId()+"\" AND `id-empr`=\""+ emprunt.getId() +"\"");
                            loans.next();
                            try { loans.getString(1); } catch (SQLException e) { // The loan doesn't exist
                                try {
                                    SQLupdate(String.format("INSERT INTO `emprunts` (`id-com`, `id-empr`, `dateFin`, `id-prod`) VALUE (\"%s\",\"%s\",\"%s\",\"%s\")", commande.getId(), emprunt.getId(), emprunt.getDateFin().get(Calendar.YEAR) + "/" + (emprunt.getDateFin().get(Calendar.MONTH) + 1) + "/" +emprunt.getDateFin().get(Calendar.DAY_OF_MONTH) , emprunt.getProduit().getId()));
                                }
                                catch (SQLException ex) {
                                    logStream.Error(ex);
                                }
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
    static Boolean load(){
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
                try {
                    var products = SQLrequest("SELECT * FROM `produits` NATURAL JOIN `types`");
                    while(products.next()){
                        var cls = (Class<? extends Produit>) Class.forName("app."+products.getString("categ"));
                        if(cls != CD.class) // TODO better cd generation
                            try {
                                produits.add(
                                    cls.getDeclaredConstructor(String.class, String.class, double.class, int.class, String.class).newInstance(products.getString("id-prod"), products.getString("title"), products.getDouble("dailyPrice"), products.getInt("quantity"), products.getString("option1"))
                                );
                            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e){
                                logStream.Error(e);
                            }
                        else
                            try{
                                var sdf = new SimpleDateFormat("dd/MM/yyyy");
                                var date = sdf.parse(products.getString("option1"));
                                var cal = Calendar.getInstance();
                                cal.setTime(date);
                                produits.add(new CD(products.getString("id-prod"), products.getString("title"), products.getDouble("dailyPrice"), products.getInt("quantity"), cal));
                            } catch (ParseException e){
                                logStream.Error(e);
                            }
                    }
                } catch (CommunicationsException e){
                    logStream.Log(e);
                    return null;
                } catch (SQLException e) {
                    logStream.Error(e);
                }

                try {
                    var users = SQLrequest("SELECT * FROM `clients`");
                    while (users.next()){
                        Class<? extends Client> cls = ClientOccas.class;
                        if (users.getBoolean("isFidel"))
                            cls = ClientFidele.class;
                            
                        try{
                            clients.addElement(cls.getDeclaredConstructor(String.class, String.class, String.class).newInstance(users.getString("id-cli"), users.getString("nom"), users.getString("prenom")));
                        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e){
                            logStream.Error(e);
                        }
                    }
                } catch (CommunicationsException e){
                    logStream.Log(e);
                    return null;
                } catch (SQLException e) {
                    logStream.Error(e);
                }

                try {
                    var orders = SQLrequest("SELECT * FROM `commandes`");
                    while (orders.next()){
                        Commande order = null;
                        for (int i=0;i<clients.getSize();i++){
                            if(clients.get(i).getId().equals(orders.getString("id-cli"))) {
                                var cal = Calendar.getInstance();
                                cal.setTime(orders.getDate("dateCreation"));
                                order = new Commande(orders.getString("id-com"), clients.get(i), cal);
                                break;
                            }
                        }
                        var loans = SQLrequest("SELECT * FROM `emprunts` WHERE `id-com` = \""+ order.getId() + "\"");
                        while(loans.next()){
                            for (var produit : produits.getList())
                                if (produit.getId().equals(loans.getString("id-prod"))){
                                    var cal = Calendar.getInstance();
                                    cal.setTime(loans.getDate("dateFin"));
                                    order.addEmprunt(loans.getString("id-empr"), cal, produit);
                                }
                        }
                        commandes.add(order);
                    }
                } catch (CommunicationsException e){
                    logStream.Log(e);
                    return null;
                } catch (SQLException e) {
                    logStream.Error(e);
                    return false;
                }
            }
            
            input.close();
            logStream.Log("Data loaded");
            return true;
        } catch (ClassNotFoundException | IOException ex) {
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
    public char[] dbPass;
    public String dbBase;
    public Locale language;

    public Settings(){
        isLocal = true;
        language = Locale.getDefault(); //new Locale("fr","FR");
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
        dbPass = new char[0];
        dbBase = "";
    }
}
