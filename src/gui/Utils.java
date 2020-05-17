package gui;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.DefaultListModel;
import app.Client;

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
	public static void createFileIfNotExists(File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdir();
        if (file.exists())
            file.createNewFile();
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
    }

    public String getdbUrl() {
        return "jdbc:mysql://" + dbUrl + "?useSSL=false";
    }

    public void setdbUrl(String url){
        dbUrl = url;
    }
}
