package de.domjos.sparfuchs.model.general;

import de.domjos.sparfuchs.model.objects.BaseDescriptionObject;

public class Category extends BaseDescriptionObject implements DatabaseObject {

    public Category() {
        super();
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "categories";
    }
}
