package gui;

import instances.Grid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by mouton on 01/06/16.
 */
public class SimplifyButtonHandler implements EventHandler<ActionEvent> {

    public GridDrawer drawer;

    public SimplifyButtonHandler(GridDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent event) {
        drawer.grid = drawer.grid.simplify();
        drawer.reinitDraw();
    }
}