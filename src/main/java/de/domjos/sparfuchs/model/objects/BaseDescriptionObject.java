package de.domjos.sparfuchs.model.objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BaseDescriptionObject extends BaseObject {
    public StringProperty description;

    public BaseDescriptionObject() {
        super();

        this.description = new SimpleStringProperty();
    }
}
