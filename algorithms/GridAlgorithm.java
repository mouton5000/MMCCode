package algorithms;

import instances.Grid;

import java.util.List;

/**
 * Created by mouton on 05/02/16.
 */
public abstract class GridAlgorithm {
    protected Grid grid;

    protected GridAlgorithmOutput output;


    protected GridAlgorithm(Grid grid){
        this.grid = grid;
    }

    public abstract void compute();

    public Grid getOutputGrid() {
        return output.getGrid();
    }

    public List<Integer> getOutputLines() {
        return output.getLines();
    }

    public List<Integer> getOutputColumns() {
        return output.getColumns();
    }

    public int getOutputDensity() {
        return output.getDensity();
    }
}

class GridAlgorithmOutput {
    private Grid grid;
    private List<Integer> lines;
    private List<Integer> columns;
    private int density;

    public GridAlgorithmOutput(Grid grid, List<Integer> outputLines, List<Integer> outputColumns, int outputDensity) {
        this.grid = grid;
        this.lines = outputLines;
        this.columns = outputColumns;
        this.density = outputDensity;
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Integer> getLines() {
        return lines;
    }

    public List<Integer> getColumns() {
        return columns;
    }

    public int getDensity() {
        return density;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setLines(List<Integer> lines) {
        this.lines = lines;
    }

    public void setColumns(List<Integer> columns) {
        this.columns = columns;
    }

    public void setDensity(int density) {
        this.density = density;
    }
}
