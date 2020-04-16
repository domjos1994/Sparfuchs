package de.domjos.sparfuchs;

import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.general.Globals;
import de.domjos.sparfuchs.utils.helper.InitializationHelper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {
    public static final Globals GLOBALS = new Globals();

    public void start(Stage stage) throws Exception {
        ResourceBundle lang = InitializationHelper.initLanguage();
        Main.GLOBALS.setDatabase(InitializationHelper.initDatabase());

        Dialogs.printFXML("/fxml/main.fxml", lang, "Sparfuchs", stage).show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
