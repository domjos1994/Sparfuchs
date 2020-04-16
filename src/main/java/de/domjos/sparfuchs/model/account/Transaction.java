package de.domjos.sparfuchs.model.account;

import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.objects.BaseDescriptionObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Transaction extends BaseDescriptionObject implements DatabaseObject {
    public ObjectProperty<BankDetail> bankDetail;
    public DoubleProperty amount;

    public Transaction() {
        super();

        this.bankDetail = new SimpleObjectProperty<>(new BankDetail());
        this.amount = new SimpleDoubleProperty(0.0);
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "transactions";
    }
}
