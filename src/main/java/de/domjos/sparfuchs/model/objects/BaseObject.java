package de.domjos.sparfuchs.model.objects;

import javafx.beans.property.*;

import java.util.Date;

public class BaseObject {
    public IntegerProperty ID;
    public LongProperty timestamp;
    public StringProperty title;

    public BaseObject() {
        super();

        this.ID = new SimpleIntegerProperty(0);
        this.timestamp = new SimpleLongProperty(new Date().getTime());
        this.title = new SimpleStringProperty("");
    }
}
