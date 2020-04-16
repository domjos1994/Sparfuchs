package de.domjos.sparfuchs.model.general;

import de.domjos.sparfuchs.model.objects.BaseObject;

public class Tag extends BaseObject implements DatabaseObject {

    public Tag() {
        super();
    }

    @Override
    public int getID() {
        return this.ID.get();
    }

    @Override
    public String getTable() {
        return "tags";
    }
}
