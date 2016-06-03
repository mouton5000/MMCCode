package gui;

import instances.Grid;
import instances.GridT;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

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
        GridT<Color> grid = this.drawer.grid;
        if(event.getSource().equals(this.drawer.flipHButton)){
            for(int line = 0; line < grid.getSizex() ; line++){
                for(int columnLeft = 0; columnLeft < grid.getSizey()/2 ; columnLeft++){
                    int columnRight = grid.getSizey()-1 - columnLeft;
                    Color cleft = grid.getPoint(line, columnLeft);
                    Color cright = grid.getPoint(line, columnRight);
                    grid.addPoint(line, columnLeft, cright);
                    grid.addPoint(line, columnRight, cleft);
                }
            }
        }
        else if(event.getSource().equals(this.drawer.flipVButton)){
            for(int column = 0; column < grid.getSizey() ; column++){
                for(int lineTop = 0; lineTop < grid.getSizex()/2 ; lineTop++){
                    int lineBottom = grid.getSizex()-1 - lineTop;
                    Color ctop = grid.getPoint(lineTop, column);
                    Color cbottom = grid.getPoint(lineBottom, column);
                    grid.addPoint(lineTop, column, cbottom);
                    grid.addPoint(lineBottom, column, ctop);
                }
            }
        }
        this.drawer.reinit();
    }
}