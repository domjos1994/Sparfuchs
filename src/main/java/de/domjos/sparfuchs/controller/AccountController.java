package de.domjos.sparfuchs.controller;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.custom.CategoryStringConverter;
import de.domjos.sparfuchs.model.account.Account;
import de.domjos.sparfuchs.model.account.StandingOrder;
import de.domjos.sparfuchs.custom.ParentController;
import de.domjos.sparfuchs.model.general.Category;
import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.helper.Helper;
import de.domjos.sparfuchs.utils.validator.IBanValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.validation.ValidationSupport;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController extends ParentController {
    private @FXML Button cmdAccountAdd, cmdAccountRemove, cmdAccountSave;

    private @FXML TextField txtAccountTitle;
    private @FXML TextField txtAccountStart;
    private @FXML TextField txtAccountBankDetailBic;
    private @FXML TextField txtAccountBankDetailIBan;
    private @FXML CheckBox chkAccountCash;
    private @FXML TextArea txtAccountDescription;
    private @FXML Accordion accAccount;

    private @FXML Button cmdStandingOrderAdd, cmdStandingOrderRemove, cmdStandingOrderSave;
    private @FXML TableView<StandingOrder> tblStandingOrders;

    @Override
    public void initialize(ResourceBundle resources) {

        this.cmdAccountSave.setOnAction(event -> {
            try {
                this.mainController.accountProperty.get().ID.set(Main.GLOBALS.getDatabase().insertOrUpdateAccount(this.mainController.accountProperty.get(), this.mainController.personProperty.get()));
                if(this.mainController.tblMainAccounts.getSelectionModel().isEmpty()) {
                    this.mainController.tblMainAccounts.getItems().add(0, this.mainController.accountProperty.get());
                    this.mainController.tblMainAccounts.getSelectionModel().select(0);
                }
                Dialogs.printNotification(Alert.AlertType.INFORMATION, resources.getString("msg.saved"), resources.getString("msg.saved.content"), null);
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdAccountRemove.setOnAction(event -> {
            try {
                Main.GLOBALS.getDatabase().delete(this.mainController.accountProperty.get());
                Account account = new Account();
                int index = this.mainController.tblMainAccounts.getSelectionModel().getSelectedIndex();
                this.mainController.tblMainAccounts.getItems().set(index, account);
                this.mainController.tblMainAccounts.getSelectionModel().select(index);
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdAccountAdd.setOnAction(event -> {
            Account account = new Account();
            this.mainController.tblMainAccounts.getItems().add(0, account);
            this.mainController.tblMainAccounts.getSelectionModel().select(0);
        });

        this.cmdStandingOrderSave.setOnAction(event -> {
            try {
                for(StandingOrder standingOrder : this.tblStandingOrders.getItems()) {
                    Main.GLOBALS.getDatabase().insertOrUpdateStandingOrder(standingOrder, this.mainController.accountProperty.get());
                }
                this.reloadStandingOrders();
                Dialogs.printNotification(Alert.AlertType.INFORMATION, resources.getString("msg.saved"), resources.getString("msg.saved.content"), null);
                this.mainController.updateData();
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdStandingOrderRemove.setOnAction(event -> {
            try {
                if(!this.tblStandingOrders.getSelectionModel().isEmpty()) {
                    Main.GLOBALS.getDatabase().delete(this.tblStandingOrders.getSelectionModel().getSelectedItem());
                }
                this.reloadStandingOrders();
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdStandingOrderAdd.setOnAction(event -> {
            StandingOrder standingOrder = new StandingOrder();
            this.tblStandingOrders.getItems().add(0, standingOrder);
            this.tblStandingOrders.getSelectionModel().select(0);
        });
    }

    @Override
    public void init() {
        this.accAccount.setExpandedPane(this.accAccount.getPanes().get(0));

        // init table-view
        this.tblStandingOrders.getColumns().clear();
        TableColumn<StandingOrder, String> title = new TableColumn<>("Title");
        title.setText(this.resources.getString("accounts.title"));
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setEditable(true);
        title.setCellValueFactory(personStringCellDataFeatures -> personStringCellDataFeatures.getValue().title);
        this.tblStandingOrders.getColumns().add(title);
        TableColumn<StandingOrder, Date> start = new TableColumn<>("Start");
        start.setText(this.resources.getString("standingOrder.start"));
        start.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        start.setEditable(true);
        start.setCellValueFactory(val -> val.getValue().start);
        this.tblStandingOrders.getColumns().add(start);
        TableColumn<StandingOrder, Integer> days = new TableColumn<>("Days");
        days.setText(this.resources.getString("standingOrder.days"));
        days.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        days.setEditable(true);
        days.setCellValueFactory(val -> val.getValue().days.asObject());
        this.tblStandingOrders.getColumns().add(days);
        TableColumn<StandingOrder, Integer> months = new TableColumn<>("Months");
        months.setText(this.resources.getString("standingOrder.months"));
        months.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        months.setEditable(true);
        months.setCellValueFactory(val -> val.getValue().months.asObject());
        this.tblStandingOrders.getColumns().add(months);
        TableColumn<StandingOrder, Double> values = new TableColumn<>("Amount");
        values.setText(this.resources.getString("transactions.value"));
        values.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        values.setEditable(true);
        values.setCellValueFactory(val -> val.getValue().amount.asObject());
        this.tblStandingOrders.getColumns().add(values);
        TableColumn<StandingOrder, String> bic = new TableColumn<>("BIC");
        bic.setText("BIC");
        bic.setCellFactory(TextFieldTableCell.forTableColumn());
        bic.setEditable(true);
        bic.setCellValueFactory(val -> val.getValue().bankDetail.get().bic);
        this.tblStandingOrders.getColumns().add(bic);
        TableColumn<StandingOrder, String> iban = new TableColumn<>("IBAN");
        iban.setText("IBAN");
        iban.setCellFactory(TextFieldTableCell.forTableColumn());
        iban.setEditable(true);
        iban.setCellValueFactory(val -> val.getValue().bankDetail.get().iBan);
        this.tblStandingOrders.getColumns().add(iban);
        try {
            ObservableList<Category> categories = FXCollections.observableList(Main.GLOBALS.getDatabase().getCategories(""));
            TableColumn<StandingOrder, Category> category = new TableColumn<>("Category");
            category.setText(this.resources.getString("category"));
            category.setCellFactory(val -> new ComboBoxTableCell<>(new CategoryStringConverter(), categories));
            category.setEditable(true);
            category.setCellValueFactory(val -> val.getValue().category);
            this.tblStandingOrders.getColumns().add(category);
        } catch (Exception ex) {
            Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
        }
        TableColumn<StandingOrder, String> tags = new TableColumn<>("Tags");
        tags.setText(this.resources.getString("tags"));
        tags.setCellFactory(TextFieldTableCell.forTableColumn());
        tags.setEditable(true);
        tags.setCellValueFactory(val -> val.getValue().tags);
        this.tblStandingOrders.getColumns().add(tags);
        Helper.fitColumnsToWidth(this.tblStandingOrders);

        // init bindings
        this.mainController.accountProperty.addListener((observableValue, oldVal, newVal) -> {
            if(oldVal != null) {
                this.txtAccountTitle.textProperty().unbindBidirectional(oldVal.title);
                this.txtAccountDescription.textProperty().unbindBidirectional(oldVal.description);
                this.txtAccountStart.textProperty().unbindBidirectional(oldVal.startAmount.asString());
                this.chkAccountCash.selectedProperty().unbindBidirectional(oldVal.isCash);
                this.txtAccountBankDetailBic.textProperty().unbindBidirectional(oldVal.bankDetail.getValue().bic);
                this.txtAccountBankDetailIBan.textProperty().unbindBidirectional(oldVal.bankDetail.getValue().iBan);
            }
            if(newVal != null) {
                this.txtAccountTitle.textProperty().bindBidirectional(newVal.title);
                this.txtAccountDescription.textProperty().bindBidirectional(newVal.description);
                this.txtAccountStart.textProperty().bindBidirectional(newVal.startAmount, new NumberStringConverter());
                this.chkAccountCash.selectedProperty().bindBidirectional(newVal.isCash);
                this.txtAccountBankDetailBic.textProperty().bindBidirectional(newVal.bankDetail.getValue().bic);
                this.txtAccountBankDetailIBan.textProperty().bindBidirectional(newVal.bankDetail.getValue().iBan);

                this.tblStandingOrders.getItems().clear();
                this.tblStandingOrders.getItems().addAll(newVal.standingOrders);
            }
        });

        // init validation
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(this.txtAccountBankDetailIBan, new IBanValidator(this.resources));
    }

    private void reloadStandingOrders() {
        try {
            List<StandingOrder> standingOrders = Main.GLOBALS.getDatabase().getStandingOrders("account=" + this.mainController.accountProperty.get().ID.get());
            this.mainController.accountProperty.get().standingOrders.clear();
            this.mainController.accountProperty.get().standingOrders.addAll(standingOrders);
            this.tblStandingOrders.getItems().clear();
            this.tblStandingOrders.getItems().addAll(this.mainController.accountProperty.get().standingOrders);
        } catch (Exception ex) {
            Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
        }
    }
}
