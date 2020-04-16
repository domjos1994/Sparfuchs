package de.domjos.sparfuchs.controller;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.model.account.Account;
import de.domjos.sparfuchs.model.account.Transaction;
import de.domjos.sparfuchs.model.controls.ParentController;
import de.domjos.sparfuchs.model.person.Person;

import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.helper.Helper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainController extends ParentController {

    private @FXML PersonController personController;
    private @FXML AccountController accountController;
    private @FXML TransactionController transactionController;

    public @FXML TableView<Person> tblMainPersons;
    public @FXML TableView<Account> tblMainAccounts;

    private @FXML TabPane tbpMain;
    private @FXML Tab tbMainPersons, tbMainAccounts, tbMainTransactions;

    private @FXML Button cmdMainAccount, cmdMainTransaction, cmdMainPerson;

    public @FXML Label lblMainMessages;
    public @FXML ProgressBar pbMainProgress;

    public ObjectProperty<Person> personProperty = new SimpleObjectProperty<>(new Person());
    public ObjectProperty<Account> accountProperty = new SimpleObjectProperty<>(new Account());

    @Override
    public void initialize(ResourceBundle resourceBundle) {
        this.init();

        this.cmdMainAccount.setOnAction(event -> {
            if(this.tblMainPersons.getSelectionModel().isEmpty()) {
                this.personProperty.setValue(new Person());
                this.accountProperty.setValue(new Account());
            } else {
                this.personProperty.setValue(this.tblMainPersons.getSelectionModel().getSelectedItem());

                if(this.tblMainAccounts.getSelectionModel().isEmpty()) {
                    this.accountProperty.setValue(new Account());
                } else {
                    this.accountProperty.setValue(this.tblMainAccounts.getSelectionModel().getSelectedItem());
                }
            }

            this.tbpMain.getSelectionModel().select(this.tbMainAccounts);
        });
        this.cmdMainPerson.setOnAction(event -> {
            if(this.tblMainPersons.getSelectionModel().isEmpty()) {
                this.personProperty.setValue(new Person());
            } else {
                this.personProperty.setValue(this.tblMainPersons.getSelectionModel().getSelectedItem());
            }

            this.tbpMain.getSelectionModel().select(this.tbMainPersons);
        });
        this.cmdMainTransaction.setOnAction(event -> {
            if(this.tblMainPersons.getSelectionModel().isEmpty()) {
                this.personProperty.setValue(null);
                this.accountProperty.setValue(null);
            } else {
                this.personProperty.setValue(this.tblMainPersons.getSelectionModel().getSelectedItem());

                if(this.tblMainAccounts.getSelectionModel().isEmpty()) {
                    this.accountProperty.setValue(null);
                } else {
                    this.accountProperty.setValue(this.tblMainAccounts.getSelectionModel().getSelectedItem());
                }
            }

            this.tbpMain.getSelectionModel().select(this.tbMainTransactions);
        });

        this.tblMainAccounts.getSelectionModel().selectedItemProperty().addListener((observableValue, account, t1) -> {
            if(!this.tblMainAccounts.getSelectionModel().isEmpty()) {
                this.transactionController.tblTransactions.getItems().clear();
                if(this.tblMainAccounts.getSelectionModel().getSelectedItem().transactions.get()!=null) {
                    this.transactionController.tblTransactions.getItems().addAll(this.tblMainAccounts.getSelectionModel().getSelectedItem().transactions);
                }

                this.accountProperty.setValue(this.tblMainAccounts.getSelectionModel().getSelectedItem());
            }
        });

        this.tblMainPersons.getSelectionModel().selectedItemProperty().addListener((observableValue, account, t1) -> {
            if(!this.tblMainPersons.getSelectionModel().isEmpty()) {
                this.tblMainAccounts.getItems().clear();
                this.tblMainAccounts.getItems().addAll(this.tblMainPersons.getSelectionModel().getSelectedItem().accounts);

                this.personProperty.setValue(this.tblMainPersons.getSelectionModel().getSelectedItem());
            }
            this.cmdMainAccount.setDisable(this.tblMainPersons.getSelectionModel().isEmpty());
        });
    }

    private void init() {
        // init controllers
        this.personController.init(this);
        this.accountController.init(this);
        this.transactionController.init(this);
        this.personController.init();
        this.accountController.init();
        this.transactionController.init();

        // init default-tab
        this.tbpMain.getSelectionModel().select(this.tbMainTransactions);

        // init table-views
        this.tblMainPersons.getColumns().clear();
        TableColumn<Person, String> firstName = new TableColumn<>("FirstName");
        firstName.setText(this.resources.getString("persons.firstName"));
        firstName.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().firstName);
        this.tblMainPersons.getColumns().add(firstName);
        TableColumn<Person, String> lastName = new TableColumn<>("LastName");
        lastName.setText(this.resources.getString("persons.lastName"));
        lastName.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().lastName);
        this.tblMainPersons.getColumns().add(lastName);
        Helper.fitColumnsToWidth(this.tblMainPersons);

        this.tblMainAccounts.getColumns().clear();
        TableColumn<Account, String> title = new TableColumn<>("Title");
        title.setText(this.resources.getString("accounts.title"));
        title.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().title);
        this.tblMainAccounts.getColumns().add(title);
        TableColumn<Account, String> endAmount = new TableColumn<>("EndAmount");
        endAmount.setText(this.resources.getString("accounts.end"));
        endAmount.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().endAmount.asString());
        this.tblMainAccounts.getColumns().add(endAmount);
        Helper.fitColumnsToWidth(this.tblMainAccounts);

        // init bindings
        this.tblMainAccounts.itemsProperty().bind(this.personProperty.getValue().accounts);

        // set defaults
        this.cmdMainAccount.setDisable(true);

        this.initData();
    }

    private void initData() {
        try {
            this.tblMainPersons.getItems().clear();
            this.tblMainPersons.getItems().addAll(Main.GLOBALS.getDatabase().getPersons(""));
        } catch (Exception ex) {
            Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
        }
    }

    @SuppressWarnings("unused")
    private void fillWithRandomData() {
        this.tblMainPersons.getItems().clear();
        Random random = new Random(1000000);
        for(int i = 0; i<=10; i++) {
            Person person = new Person();
            person.firstName.setValue(UUID.randomUUID().toString());
            person.lastName.setValue(UUID.randomUUID().toString());
            for(int j = 0; j<=10; j++) {
                Account account = new Account();
                account.startAmount.setValue(random.nextDouble());
                account.title.setValue(UUID.randomUUID().toString());
                for(int k = 0; k<=10; k++) {
                    Transaction transaction = new Transaction();
                    transaction.title.setValue(UUID.randomUUID().toString());
                    transaction.amount.setValue(random.nextDouble());
                    account.transactions.add(transaction);
                }
                person.accounts.add(account);
            }
            this.tblMainPersons.getItems().add(person);
        }
    }
}
