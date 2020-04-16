package de.domjos.sparfuchs.model.account;

import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.objects.BaseDescriptionObject;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.util.LinkedList;

public class Account extends BaseDescriptionObject implements DatabaseObject {
    public ObjectProperty<BankDetail> bankDetail;
    public BooleanProperty isCash;
    public ListProperty<Transaction> transactions;
    public DoubleProperty startAmount;
    public DoubleProperty endAmount;

    public Account() {
        super();

        this.bankDetail = new SimpleObjectProperty<>(new BankDetail());
        this.isCash = new SimpleBooleanProperty(true);
        this.transactions = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.transactions.addListener((ListChangeListener<Transaction>) change -> {
            double start = this.startAmount.get();
            start += this.transactions.stream().mapToDouble(transaction -> transaction.amount.get()).sum();
            this.endAmount = new SimpleDoubleProperty(start);
        });
        this.startAmount = new SimpleDoubleProperty(0.0);
        this.endAmount = new SimpleDoubleProperty(0.0);
    }

    public double getEndAmount() {
        return this.endAmount.get();
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "accounts";
    }
}
