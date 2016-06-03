package gui;

import instances.IndexOutOfBoundGridException;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import utils.Couple;

/**
 * Created by mouton on 23/02/16.
 */
public class GridMouseMovedHandler implements EventHandler<MouseEvent> {

    private GridDrawer drawer;
    private Pane root;

    private Group group;
    private Integer lastLine, lastColumn;

    private boolean edit;

    public GridMouseMovedHandler(GridDrawer drawer, Pane root) {
        this.drawer = drawer;
        this.group = new Group();
        root.getChildren().add(group);
        lastLine = null;
        lastColumn = null;
        this.edit = true;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();

        if(edit)
            handleEdit(x,y);
        else
            handlePlay(x,y);
    }

    public boolean hasChanged(Integer line, Integer column){
        boolean columnChanged = column == null && lastColumn != null || column != null && !column.equals(lastColumn);
        boolean lineChanged = line == null && lastLine != null || line != null && !line.equals(lastLine);

        return columnChanged || lineChanged;
    }

    public void handleEdit(int x, int y){
        Integer line = GridDrawer.getLine(y);
        if (line == null)
            return;

        Integer column = GridDrawer.getColumn(x);
        if (column == null)
            return;

        if(!hasChanged(line,column))
            return;

        removeShape();
        setCircleShape(line, column);

        lastLine = line;
        lastColumn = column;
    }

    public void handlePlay(int x, int y){
        Couple<Integer, Integer> lineColumn = GridDrawer.getLineColumn(x, y);
        Integer line = lineColumn.first;
        Integer column = lineColumn.second;

        if(!hasChanged(line,column))
            return;

        removeShape();
        if (line == null && column == null)
            return;
        else if (line == null)
            setColumnRectShape(column);
        else if (column == null)
            setLineRectShape(line);


        lastColumn = column;
        lastLine = line;
    }

    public void setLineRectShape(int line){
        try{
            boolean canMerge = drawer.currentGrid.canMergeLine(line-1);
            Shape shape = GridDrawer.getLineRectShape(line, !canMerge);
            shape.setOpacity(0.75);
            this.group.getChildren().add(shape);
        }
        catch(IndexOutOfBoundGridException e){

        }
    }

    public void setColumnRectShape(int column){
        try {
            boolean canMerge = drawer.currentGrid.canMergeColumn(column - 1);
            Shape shape = GridDrawer.getColumnRectShape(column, !canMerge);
            shape.setOpacity(0.75);
            this.group.getChildren().add(shape);
        }
        catch(IndexOutOfBoundGridException e){
        }
    }

    public void setCircleShape(int line, int column){
        Shape circle = GridDrawer.getCircleShape(line, column, drawer.currentColor);
        circle.setOpacity(0.5);

        Shape lineLeftRect = GridDrawer.getBorderLeftLineRectShape(line);
        Shape lineRightRect = GridDrawer.getBorderRightLineRectShape(line);
        Shape lineRect = GridDrawer.getFullLineRectShape(line);
        Shape columnTopRect = GridDrawer.getBorderTopColumnRectShape(column);
        Shape columnBottomRect = GridDrawer.getBorderBottomColumnRectShape(column);
        Shape columnRect = GridDrawer.getFullColumnRectShape(column);


        lineLeftRect.setOpacity(0.5);
        lineRightRect.setOpacity(0.5);
        lineRect.setOpacity(0.10);
        columnTopRect.setOpacity(0.5);
        columnBottomRect.setOpacity(0.5);
        columnRect.setOpacity(0.10);

        this.group.getChildren().add(circle);
        this.group.getChildren().add(lineLeftRect);
        this.group.getChildren().add(lineRightRect);
        this.group.getChildren().add(lineRect);
        this.group.getChildren().add(columnTopRect);
        this.group.getChildren().add(columnBottomRect);
        this.group.getChildren().add(columnRect);
    }

    public void removeShape(){
        this.group.getChildren().clear();
        lastColumn = null;
        lastLine = null;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        removeShape();
    }
}
