package gui;

import algorithms.GridAlgorithm;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Collections;
import java.util.List;

/**
 * Created by mouton on 25/02/16.
 */
public class AlgorithmButtonHandler  implements EventHandler<ActionEvent> {

    public GridAlgorithm algorithm;
    public GridDrawer drawer;

    public AlgorithmButtonHandler(GridAlgorithm algorithm, GridDrawer drawer) {
        this.algorithm = algorithm;
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        drawer.reinit();

        ((ModeButtonHandler)drawer.modeButton.getOnAction()).start = false;
        drawer.modeButton.setSelected(true);

        drawer.gmmh.setEdit(false);
        drawer.gmph.setEdit(false);

        algorithm.compute();

        List<Integer> lines = drawer.grid.linesToSimplify();
        List<Integer> columns = drawer.grid.columnsToSimplify();

        Collections.sort(lines);
        Collections.reverse(lines);
        Collections.sort(columns);
        Collections.reverse(columns);

        List<Integer> lines2 = algorithm.getOutputLines();
        List<Integer> columns2 = algorithm.getOutputColumns();

        Collections.sort(lines2);
        Collections.reverse(lines2);
        Collections.sort(columns2);
        Collections.reverse(columns2);

        lines.addAll(lines2);
        columns.addAll(columns2);

        Timeline timeline = new Timeline();
        timeline.setOnFinished(new AlgoTimelineEventHandler(lines, columns, drawer));
        timeline.play();

    }

    private class AlgoTimelineEventHandler implements EventHandler<ActionEvent>{

        public List<Integer> lines;
        public List<Integer> columns;
        public GridDrawer drawer;

        public AlgoTimelineEventHandler(List<Integer> lines, List<Integer> columns, GridDrawer drawer) {
            this.lines = lines;
            this.columns = columns;
            this.drawer = drawer;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            if(!lines.isEmpty()){
                Integer line = lines.get(0);
                lines.remove(0);
                Timeline timeline = drawer.mergeLineTransition(line + 1);
                drawer.currentGrid = drawer.currentGrid.mergeLine(line);
                timeline.setOnFinished(new AlgoTimelineEventHandler(lines, columns, drawer));
                timeline.play();
            }
            else if(!columns.isEmpty()){
                Integer column = columns.get(0);
                columns.remove(0);
                Timeline timeline = drawer.mergeColumnTransition(column + 1);
                drawer.currentGrid = drawer.currentGrid.mergeColumn(column);
                timeline.setOnFinished(new AlgoTimelineEventHandler(lines, columns, drawer));
                timeline.play();
            }
        }
    }
}

