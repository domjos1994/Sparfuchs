package de.domjos.sparfuchs.utils.general;

import de.domjos.sparfuchs.Sparfuchs;

import java.util.prefs.Preferences;

public class Globals {
    private Preferences preferences;
    private Database database;
    private boolean debug;

    public final static String PATH = "PATH";

    public Globals() {
        this.database = null;
        this.debug = false;
        this.preferences = Preferences.userRoot().node(Sparfuchs.class.getName());
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isEmpty(String key) {
        return this.preferences.get(key, "").equals("");
    }

    public boolean getSetting(String key, boolean def) {
        return this.preferences.getBoolean(key, def);
    }

    public double getSetting(String key, double def) {
        return this.preferences.getDouble(key, def);
    }

    public float getSetting(String key, float def) {
        return this.preferences.getFloat(key, def);
    }

    public int getSetting(String key, int def) {
        return this.preferences.getInt(key, def);
    }

    public long getSetting(String key, long def) {
        return this.preferences.getLong(key, def);
    }

    public byte[] getSetting(String key, byte[] def) {
        return this.preferences.getByteArray(key, def);
    }

    public String getSetting(String key, String def) {
        return this.preferences.get(key, def);
    }

    public void saveSetting(String key, Object value) {
        if(value instanceof Boolean) {
            this.preferences.putBoolean(key, (Boolean) value);
        } else if(value instanceof Double) {
            this.preferences.putDouble(key, (Double) value);
        } else if(value instanceof Float) {
            this.preferences.putFloat(key, (Float) value);
        } else if(value instanceof Integer) {
            this.preferences.putInt(key, (Integer) value);
        } else if(value instanceof Long) {
            this.preferences.putLong(key, (Long) value);
        } else if(value instanceof String) {
            this.preferences.put(key, (String) value);
        } else if(value instanceof byte[]) {
            this.preferences.putByteArray(key, (byte[]) value);
        } else {
            this.preferences.put(key, String.valueOf(value));
        }
    }

    public void deleteSetting(String key) {
        this.preferences.remove(key);
    }
}
