package de.domjos.sparfuchs.utils.helper;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.general.Globals;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class Helper {
    public final static String HIDDEN_PROJECT_DIR = ".sparFuchs";

    public static <T> void fitColumnsToWidth(TableView<T> tbl) {
        int intColumns = 0;
        for (TableColumn<T, ?> col : tbl.getColumns()) {
            if (col.isVisible()) {
                intColumns++;
            }
        }

        for (TableColumn<T, ?> col : tbl.getColumns()) {
            if (col.isVisible()) {
                col.prefWidthProperty().bind(tbl.widthProperty().divide(intColumns));
            }
        }

        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Opens a Dialog if saved path doesn't exists
     * and creates the path.
     */
    public static String initializePath() {
        String path = "";
        if(Main.GLOBALS.isEmpty(Globals.PATH)) {
            File files = Dialogs.printDirectoryChooser("");
            if(files!=null) {
                path = files.getAbsolutePath();
                Main.GLOBALS.saveSetting(Globals.PATH, files.getAbsolutePath());
                File file = new File(Main.GLOBALS.getSetting(Globals.PATH, "") + File.separatorChar + Helper.HIDDEN_PROJECT_DIR);
                if(!file.exists()) {
                    if(!file.mkdirs()) {
                        Platform.exit();
                    }
                }
            } else {
                Platform.exit();
            }
        } else {
            File file = new File(Main.GLOBALS.getSetting(Globals.PATH, "") + File.separatorChar + Helper.HIDDEN_PROJECT_DIR);
            path = file.getAbsolutePath();
            if(!file.exists()) {
                Main.GLOBALS.saveSetting(Globals.PATH, "");
                Helper.initializePath();
            }
        }
        return path;
    }
}
