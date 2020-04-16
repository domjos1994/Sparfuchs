package de.domjos.sparfuchs.utils.general;

public class Globals {
    private Database database;
    private boolean debug;

    public Globals() {
        this.database = null;
        this.debug = false;
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
}
