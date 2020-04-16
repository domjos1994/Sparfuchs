package de.domjos.sparfuchs.model.person;

import de.domjos.sparfuchs.model.account.Account;
import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.objects.BaseObject;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.LinkedList;

public class Person extends BaseObject implements DatabaseObject {
    public StringProperty firstName, lastName;
    public ListProperty<Account> accounts;
    public ObjectProperty<LocalDate> birthDate;
    public ObjectProperty<Image> profileImage;

    public Person() {
        super();

        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");

        this.accounts = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));

        this.birthDate = new SimpleObjectProperty<>(null);
        this.profileImage = new SimpleObjectProperty<>(null);
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "persons";
    }
}
