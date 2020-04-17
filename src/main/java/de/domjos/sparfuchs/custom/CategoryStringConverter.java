package de.domjos.sparfuchs.custom;

import de.domjos.sparfuchs.Main;
import de.domjos.sparfuchs.model.general.Category;
import de.domjos.sparfuchs.utils.general.Dialogs;
import javafx.util.StringConverter;

import java.util.List;

public class CategoryStringConverter extends StringConverter<Category> {
    @Override
    public String toString(Category object) {
        return object.title.get();
    }

    @Override
    public Category fromString(String string) {
        Category category = new Category();
        category.title.set(string);
        try {
            List<Category> categories = Main.GLOBALS.getDatabase().getCategories("title='" + string.trim() + "'");
            if(categories.isEmpty()) {
                return category;
            } else {
                return categories.get(0);
            }
        } catch (Exception ex) {
            Dialogs.printException(ex, Main.GLOBALS.isDebug(), null);
        }
        return category;
    }
}
