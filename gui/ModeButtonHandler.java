package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by mouton on 25/02/16.
 */
public class ModeButtonHandler implements EventHandler<ActionEvent> {

    public boolean start;
    public GridDrawer drawer;

    public ModeButtonHandler(GridDrawer drawer) {
        this.start = true;
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent event) {
        drawer.gmph.setEdit(!start);
        drawer.gmmh.setEdit(!start);
        if(!start)
            drawer.reinit();
        start = !start;
    }
}
