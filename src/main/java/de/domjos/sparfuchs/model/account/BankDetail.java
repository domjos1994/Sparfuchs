package de.domjos.sparfuchs.model.account;

import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.objects.BaseObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BankDetail extends BaseObject implements DatabaseObject {
    public StringProperty iBan, bic;

    public BankDetail() {
        super();

        this.iBan = new SimpleStringProperty("");
        this.bic = new SimpleStringProperty("");
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "bankDetails";
    }
}
