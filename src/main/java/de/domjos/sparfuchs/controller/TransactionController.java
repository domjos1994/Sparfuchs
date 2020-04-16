package de.domjos.sparfuchs.controller;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.model.account.Transaction;
import de.domjos.sparfuchs.model.controls.ParentController;
import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.helper.Helper;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.util.ResourceBundle;

public class TransactionController extends ParentController {
    public @FXML TableView<Transaction> tblTransactions;

    private @FXML Button cmdTransactionAdd, cmdTransactionRemove, cmdTransactionSave;

    @Override
    public void initialize(ResourceBundle resources) {

        this.cmdTransactionSave.setOnAction(event -> {
            try {
                this.tblTransactions.getSelectionModel().getSelectedItem().ID.set(Main.GLOBALS.getDatabase().insertOrUpdateTransaction(this.tblTransactions.getSelectionModel().getSelectedItem(), this.mainController.accountProperty.get()));
                Dialogs.printNotification(Alert.AlertType.INFORMATION, resources.getString("msg.saved"), resources.getString("msg.saved.content"), null);
                this.mainController.accountProperty.get().transactions.clear();
                this.mainController.accountProperty.get().transactions.addAll(this.tblTransactions.getItems());
                this.mainController.tblMainAccounts.getItems().set(this.mainController.tblMainAccounts.getSelectionModel().getSelectedIndex(), this.mainController.accountProperty.get());
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdTransactionRemove.setOnAction(event -> {
            try {
                Main.GLOBALS.getDatabase().delete(this.tblTransactions.getSelectionModel().getSelectedItem());
                Transaction transaction = new Transaction();
                int index = this.tblTransactions.getSelectionModel().getSelectedIndex();
                this.tblTransactions.getItems().set(index, transaction);
                this.tblTransactions.getSelectionModel().select(index);
                this.mainController.accountProperty.get().transactions.clear();
                this.mainController.accountProperty.get().transactions.addAll(this.tblTransactions.getItems());
                this.mainController.tblMainAccounts.getItems().set(this.mainController.tblMainAccounts.getSelectionModel().getSelectedIndex(), this.mainController.accountProperty.get());
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdTransactionAdd.setOnAction(event -> {
            Transaction transaction = new Transaction();
            this.tblTransactions.getItems().add(0, transaction);
            this.tblTransactions.getSelectionModel().select(0);
        });
    }


    public void init() {
        // init table-view
        this.tblTransactions.getColumns().clear();
        TableColumn<Transaction, String> title = new TableColumn<>("Title");
        title.setText(this.resources.getString("accounts.title"));
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setEditable(true);
        title.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().title);
        this.tblTransactions.getColumns().add(title);
        TableColumn<Transaction, Double> value = new TableColumn<>("Value");
        value.setText(this.resources.getString("transactions.value"));
        value.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        value.setEditable(true);
        value.setCellValueFactory(val -> val.getValue().amount.asObject());
        this.tblTransactions.getColumns().add(value);
        TableColumn<Transaction, String> bic = new TableColumn<>("BIC");
        bic.setText("BIC");
        bic.setCellFactory(TextFieldTableCell.forTableColumn());
        bic.setEditable(true);
        bic.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().bankDetail.get().bic);
        this.tblTransactions.getColumns().add(bic);
        TableColumn<Transaction, String> iban = new TableColumn<>("IBAN");
        iban.setText("IBAN");
        iban.setCellFactory(TextFieldTableCell.forTableColumn());
        iban.setEditable(true);
        iban.setCellValueFactory(personStringCellDataFeatures -> {
            this.fillWithColor();
            return personStringCellDataFeatures.getValue().bankDetail.get().iBan;
        });
        this.tblTransactions.getColumns().add(iban);
        Helper.fitColumnsToWidth(this.tblTransactions);

        // init bindings
        this.tblTransactions.itemsProperty().bind(this.mainController.accountProperty.getValue().transactions);
    }

    private void fillWithColor() {
        int i = 0;
        for (Node n: this.tblTransactions.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                TableRow<Transaction> row = (TableRow<Transaction>) n;
                if (this.tblTransactions.getItems().get(i).amount.get()>0) {
                    row.setStyle("-fx-background-color: green;");
                } else {
                    row.setStyle("-fx-background-color: red;");
                }
                i++;
                if (i == this.tblTransactions.getItems().size()) {
                    break;
                }
            }
        }
    }
}
