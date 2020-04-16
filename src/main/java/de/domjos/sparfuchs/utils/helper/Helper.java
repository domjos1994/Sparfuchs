package de.domjos.sparfuchs.utils.helper;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Helper {

    public static void fitColumnsToWidth(TableView tbl) {
        int intColumns = 0;
        for (Object col : tbl.getColumns()) {
            if (((TableColumn) col).isVisible()) {
                intColumns++;
            }
        }

        for (Object col : tbl.getColumns()) {
            if (((TableColumn) col).isVisible()) {
                ((TableColumn) col).prefWidthProperty().bind(tbl.widthProperty().divide(intColumns));
            }
        }

        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
