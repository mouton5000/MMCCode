package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by mouton on 01/06/16.
 */
public class EraseGridButtonHandler  implements EventHandler<ActionEvent> {

    public GridDrawer drawer;

    public EraseGridButtonHandler(GridDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent event) {
        drawer.grid = drawer.currentGrid;
        drawer.reinitDraw();
    }
}
