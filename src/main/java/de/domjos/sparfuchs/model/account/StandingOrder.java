package de.domjos.sparfuchs.model.account;

import de.domjos.sparfuchs.model.general.Category;
import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.objects.BaseObject;
import javafx.beans.property.*;

import java.util.Date;

public class StandingOrder extends BaseObject implements DatabaseObject {
    public StringProperty title;
    public ObjectProperty<Date> start;
    public IntegerProperty days;
    public IntegerProperty months;
    public DoubleProperty amount;
    public ObjectProperty<BankDetail> bankDetail;
    public ObjectProperty<Category> category;
    public StringProperty tags;

    public StandingOrder() {
        super();

        this.title = new SimpleStringProperty();
        this.start = new SimpleObjectProperty<>();
        this.days = new SimpleIntegerProperty(0);
        this.months = new SimpleIntegerProperty(0);
        this.amount = new SimpleDoubleProperty(0.0);
        this.bankDetail = new SimpleObjectProperty<>(new BankDetail());
        this.category = new SimpleObjectProperty<>(new Category());
        this.tags = new SimpleStringProperty();
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "standingOrders";
    }
}
