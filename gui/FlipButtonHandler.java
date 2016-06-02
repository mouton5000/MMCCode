package gui;

import instances.Grid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by mouton on 01/06/16.
 */
public class FlipButtonHandler implements EventHandler<ActionEvent> {

    public GridDrawer drawer;

    public FlipButtonHandler(GridDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent event) {
        Grid grid = this.drawer.grid;
        if(event.getSource().equals(this.drawer.flipHButton)){
            for(int line = 0; line < grid.getSizex() ; line++){
                for(int columnLeft = 0; columnLeft < grid.getSizey()/2 ; columnLeft++){
                    int columnRight = grid.getSizey()-1 - columnLeft;
                    boolean bleft = grid.hasPoint(line, columnLeft);
                    boolean bright = grid.hasPoint(line, columnRight);
                    if((bleft && !bright) || (!bleft && bright)) {
                        grid.invertPoint(line, columnLeft);
                        grid.invertPoint(line, columnRight);
                    }
                }
            }
        }
        else if(event.getSource().equals(this.drawer.flipVButton)){
            for(int column = 0; column < grid.getSizey() ; column++){
                for(int lineTop = 0; lineTop < grid.getSizex()/2 ; lineTop++){
                    int lineBottom = grid.getSizex()-1 - lineTop;
                    boolean btop = grid.hasPoint(lineTop, column);
                    boolean bbottom = grid.hasPoint(lineBottom, column);
                    if((btop && !bbottom) || (!btop && bbottom)) {
                        grid.invertPoint(lineTop, column);
                        grid.invertPoint(lineBottom, column);
                    }
                }
            }
        }
        this.drawer.reinit();
    }
}