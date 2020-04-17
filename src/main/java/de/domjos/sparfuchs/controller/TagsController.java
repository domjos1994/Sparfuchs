package de.domjos.sparfuchs.controller;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.custom.ParentController;

import de.domjos.sparfuchs.model.general.Category;
import de.domjos.sparfuchs.model.general.Tag;
import de.domjos.sparfuchs.utils.general.Dialogs;
import de.domjos.sparfuchs.utils.helper.Helper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ResourceBundle;

public class TagsController extends ParentController {
    // Categories
    private @FXML TableView<Category> tblCategories;
    private @FXML Button cmdCategoryAdd, cmdCategoryRemove, cmdCategorySave;

    // Tags
    private @FXML TableView<Tag> tblTags;
    private @FXML Button cmdTagAdd, cmdTagRemove, cmdTagSave;

    @Override
    public void initialize(ResourceBundle resources) {
        this.initTables();
        this.init();

        this.cmdCategoryAdd.setOnAction(event -> {
            Category category = new Category();
            this.tblCategories.getItems().add(0, category);
            this.tblCategories.getSelectionModel().select(0);
        });

        this.cmdTagAdd.setOnAction(event -> {
            Tag tag = new Tag();
            this.tblTags.getItems().add(0, tag);
            this.tblTags.getSelectionModel().select(0);
        });

        this.cmdCategoryRemove.setOnAction(event -> {
            try {
                Main.GLOBALS.getDatabase().delete(this.tblCategories.getSelectionModel().getSelectedItem());
                this.init();
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdTagRemove.setOnAction(event -> {
            try {
                Main.GLOBALS.getDatabase().delete(this.tblTags.getSelectionModel().getSelectedItem());
                this.init();
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdCategorySave.setOnAction(event -> {
            try {
                for(Category category : this.tblCategories.getItems()) {
                    Main.GLOBALS.getDatabase().insertOrUpdateCategory(category);
                }
                this.init();
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });

        this.cmdTagSave.setOnAction(event -> {
            try {
                for(Tag tag : this.tblTags.getItems()) {
                    Main.GLOBALS.getDatabase().insertOrUpdateTag(tag);
                }
                this.init();
            } catch (Exception ex) {
                Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
            }
        });
    }

    private void initTables() {
        TableColumn<Category, String> category = new TableColumn<>("Title");
        category.setText(this.resources.getString("accounts.title"));
        category.setEditable(true);
        category.setCellFactory(TextFieldTableCell.forTableColumn());
        category.setCellValueFactory(val -> val.getValue().title);
        this.tblCategories.getColumns().add(category);
        TableColumn<Category, String> description = new TableColumn<>("Description");
        description.setText(this.resources.getString("description"));
        description.setEditable(true);
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setCellValueFactory(val -> val.getValue().title);
        this.tblCategories.getColumns().add(description);
        Helper.fitColumnsToWidth(this.tblCategories);

        TableColumn<Tag, String> tags = new TableColumn<>("Title");
        tags.setText(this.resources.getString("accounts.title"));
        tags.setEditable(true);
        tags.setCellFactory(TextFieldTableCell.forTableColumn());
        tags.setCellValueFactory(val -> val.getValue().title);
        this.tblTags.getColumns().add(tags);
        Helper.fitColumnsToWidth(this.tblTags);
    }

    @Override
    public void init() {
        try {
            this.tblCategories.getItems().clear();
            this.tblCategories.getItems().addAll(Main.GLOBALS.getDatabase().getCategories(""));

            this.tblTags.getItems().clear();
            this.tblTags.getItems().addAll(Main.GLOBALS.getDatabase().getTags(""));
        } catch (Exception ex) {
            Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
        }
    }
}
