package de.domjos.sparfuchs.utils.helper;

import de.domjos.sparfuchs.utils.general.Database;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class InitializationHelper {

    public static ResourceBundle initLanguage() {
        URL[] urls = new URL[]{InitializationHelper.class.getResource("/language/")};
        URLClassLoader classLoader = new URLClassLoader(urls);
        if(Locale.getDefault().getLanguage().equals(Locale.GERMAN.getLanguage())) {
            return ResourceBundle.getBundle("lang", Locale.GERMAN, classLoader);
        } else {
            return ResourceBundle.getBundle("lang");
        }
    }

    public static Database initDatabase() throws Exception {
        Database database = new Database();
        database.init();
        return database;
    }
}
