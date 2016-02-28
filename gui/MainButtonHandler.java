package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Shape;

/**
 * Created by mouton on 25/02/16.
 */
public class MainButtonHandler implements EventHandler<ActionEvent> {

    public boolean start;
    public GridDrawer drawer;

    public MainButtonHandler(GridDrawer drawer) {
        this.start = true;
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent event) {
        drawer.gmph.setEdit(!start);
        drawer.gmmh.setEdit(!start);
        if(start) {
            drawer.mainButton.setText("Reinit");
        }
        else{
            drawer.reinit();
        }
        start = !start;
    }
}
