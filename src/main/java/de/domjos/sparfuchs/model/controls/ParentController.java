package de.domjos.sparfuchs.model.controls;

import de.domjos.sparfuchs.controller.MainController;
import de.domjos.sparfuchs.utils.general.Dialogs;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ParentController implements Initializable {
    protected MainController mainController;
    protected ResourceBundle resources;

    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.initContextHelp();
        this.initialize(resources);
    }

    public abstract void initialize(ResourceBundle resources);

    public void init(MainController mainController) {
        this.mainController = mainController;
    }

    protected void initContextHelp() {

    }

    protected void addContextHelp(Control control, String key) {
        control.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.F1) {
                Dialogs.printNotification(Alert.AlertType.INFORMATION, resources.getString(key), resources.getString(key), null);
            }
        });
    }
}
