module Sparfuchs {
    requires java.desktop;
    requires java.sql;
    requires java.prefs;
    requires java.base;

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires sqlite.jdbc;

    opens de.domjos.sparfuchs.controller to javafx.controls, javafx.fxml, javafx.base;
    opens de.domjos.sparfuchs to javafx.controls, javafx.fxml;

    exports de.domjos.sparfuchs;
}