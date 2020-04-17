package de.domjos.sparfuchs.model.account;

import de.domjos.sparfuchs.model.general.Category;
import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.objects.BaseDescriptionObject;
import javafx.beans.property.*;

import java.util.Date;

public class Transaction extends BaseDescriptionObject implements DatabaseObject {
    public ObjectProperty<BankDetail> bankDetail;
    public DoubleProperty amount;
    public ObjectProperty<Date> date;
    public BooleanProperty system;
    public ObjectProperty<Category> category;

    public Transaction() {
        super();

        this.bankDetail = new SimpleObjectProperty<>(new BankDetail());
        this.amount = new SimpleDoubleProperty(0.0);
        this.date = new SimpleObjectProperty<>();
        this.system = new SimpleBooleanProperty(false);
        this.category = new SimpleObjectProperty<>(new Category());
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
