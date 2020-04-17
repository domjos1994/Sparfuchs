package de.domjos.sparfuchs.controller;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.custom.ParentController;
import de.domjos.sparfuchs.model.person.Person;
import de.domjos.sparfuchs.utils.general.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.ResourceBundle;

public class PersonController extends ParentController {
    private @FXML Button cmdPersonAdd, cmdPersonImageLoad, cmdPersonRemove, cmdPersonSave;

    private @FXML TextField txtPersonFirstName;
    private @FXML TextField txtPersonLastName;
    private @FXML DatePicker dpPersonBirthDate;
    private @FXML ImageView ivPersonImage;

    @Override
    public void initialize(ResourceBundle resources) {

        this.cmdPersonSave.setOnAction(event -> {
            try {
                this.mainController.personProperty.get().ID.set(Main.GLOBALS.getDatabase().insertOrUpdatePerson(this.mainController.personProperty.get()));
                if(this.mainController.tblMainPersons.getSelectionModel().isEmpty()) {
                    this.mainController.tblMainPersons.getItems().add(0, this.mainController.personProperty.get());
                    this.mainController.tblMainPersons.getSelectionModel().select(0);
                }
                Dialogs.printNotification(Alert.AlertType.INFORMATION, resources.getString("msg.saved"), resources.getString("msg.saved.content"), null);
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdPersonRemove.setOnAction(event -> {
            try {
                Main.GLOBALS.getDatabase().delete(this.mainController.personProperty.get());
                Person person = new Person();
                int index = this.mainController.tblMainPersons.getSelectionModel().getSelectedIndex();
                this.mainController.tblMainPersons.getItems().set(index, person);
                this.mainController.tblMainPersons.getSelectionModel().select(index);
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdPersonAdd.setOnAction(event -> {
            Person person = new Person();
            this.mainController.tblMainPersons.getItems().add(0, person);
            this.mainController.tblMainPersons.getSelectionModel().select(0);
        });

        this.cmdPersonImageLoad.setOnAction(event -> {
            try {
                File file = Dialogs.printSingleOpenFileChooser("", Collections.singletonList("Image:*.png|*.jpg|*.PNG|*.JPG"));
                if(file!=null) {
                    if(file.exists()) {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
                        this.ivPersonImage.setImage(new Image(byteArrayInputStream));
                        byteArrayInputStream.close();
                    }
                }
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });
    }

    @Override
    public void init() {
        // init bindings
        this.mainController.personProperty.addListener((observableValue, person, t1) -> {
            if(person != null) {
                this.txtPersonFirstName.textProperty().unbindBidirectional(person.firstName);
                this.txtPersonLastName.textProperty().unbindBidirectional(person.lastName);
                this.dpPersonBirthDate.valueProperty().unbindBidirectional(person.birthDate);
                this.ivPersonImage.imageProperty().unbindBidirectional(person.profileImage);
            }
            if(t1 != null) {
                this.txtPersonFirstName.textProperty().bindBidirectional(t1.firstName);
                this.txtPersonLastName.textProperty().bindBidirectional(t1.lastName);
                this.dpPersonBirthDate.valueProperty().bindBidirectional(t1.birthDate);
                this.ivPersonImage.imageProperty().bindBidirectional(t1.profileImage);
            }
        });
    }
}
