package de.domjos.sparfuchs.controller;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.model.account.Account;
import de.domjos.sparfuchs.model.controls.ParentController;
import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.validator.IBanValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.ResourceBundle;

public class AccountController extends ParentController {
    private @FXML Button cmdAccountAdd, cmdAccountRemove, cmdAccountSave;

    private @FXML TextField txtAccountTitle;
    private @FXML TextField txtAccountStart;
    private @FXML TextField txtAccountBankDetailBic;
    private @FXML TextField txtAccountBankDetailIBan;
    private @FXML CheckBox chkAccountCash;
    private @FXML TextArea txtAccountDescription;

    @Override
    public void initialize(ResourceBundle resources) {

        this.cmdAccountSave.setOnAction(event -> {
            try {
                this.mainController.accountProperty.get().ID.set(Main.GLOBALS.getDatabase().insertOrUpdateAccount(this.mainController.accountProperty.get(), this.mainController.personProperty.get()));
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
    }

    public void init() {
        // init bindings
        this.mainController.accountProperty.addListener((observableValue, oldVal, newVal) -> {
            this.txtAccountTitle.textProperty().unbindBidirectional(oldVal.title);
            this.txtAccountTitle.textProperty().bindBidirectional(newVal.title);
            this.txtAccountDescription.textProperty().unbindBidirectional(oldVal.description);
            this.txtAccountDescription.textProperty().bindBidirectional(newVal.description);
            this.txtAccountStart.textProperty().unbindBidirectional(oldVal.startAmount.asString());
            this.txtAccountStart.textProperty().bindBidirectional(newVal.startAmount, new NumberStringConverter());
            this.chkAccountCash.selectedProperty().unbindBidirectional(oldVal.isCash);
            this.chkAccountCash.selectedProperty().bindBidirectional(newVal.isCash);
            this.txtAccountBankDetailBic.textProperty().unbindBidirectional(oldVal.bankDetail.getValue().bic);
            this.txtAccountBankDetailBic.textProperty().bindBidirectional(newVal.bankDetail.getValue().bic);
            this.txtAccountBankDetailIBan.textProperty().unbindBidirectional(oldVal.bankDetail.getValue().iBan);
            this.txtAccountBankDetailIBan.textProperty().bindBidirectional(newVal.bankDetail.getValue().iBan);
        });

        // init validation
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(this.txtAccountBankDetailIBan, new IBanValidator(this.resources));
    }
}
