package algorithms;

import instances.Grid;
import instances.UnmergeableGridException;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by mouton on 05/02/16.
 */
public class LCLApproximationAlgorithm extends GridAlgorithm{

    public LCLApproximationAlgorithm(Grid grid){
        super(grid);
    }

    @Override
    public void compute() {
        try {


            GridAlgorithmOutput tCL = this.computeCL();
            GridAlgorithmOutput tLC = this.computeLC();

            if(tCL.getDensity() > tLC.getDensity())
                this.output = tCL;
            else
                this.output = tLC;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private GridAlgorithmOutput computeLC() throws CloneNotSupportedException {
        List<Integer> mergedLines = new LinkedList<Integer>();
        List<Integer> mergedColumns = new LinkedList<Integer>();
        Grid g, g2, gClone;
        g = this.grid.simplify();
        gClone = (Grid) g.clone();

        outer : for(int line = gClone.getSizex()-2; line >= 0; line--){
            for(int column = 0; column < gClone.getSizey(); column++) {
                if(gClone.hasPoint(line, column) && gClone.hasPoint(line+1, column))
                    continue outer;
            }
            mergedLines.add(line);
            for(int column = 0; column < gClone.getSizey(); column++) {
                if(gClone.hasPoint(line+1, column))
                    gClone.addPoint(line, column);
            }
        }

        g = g.mergeLines(mergedLines);
        gClone = (Grid) g.clone();


        outer : for(int column = gClone.getSizey()-2; column >= 0; column--){
            for(int line = 0; line < gClone.getSizex(); line++) {
                if(gClone.hasPoint(line, column) && gClone.hasPoint(line, column+1))
                    continue outer;
            }
            mergedColumns.add(column);
            for(int line = 0; line < gClone.getSizex(); line++) {
                if(gClone.hasPoint(line, column+1))
                    gClone.addPoint(line, column);
            }
        }
        g2 = g.mergeColumns(mergedColumns);

        return new GridAlgorithmOutput(g2, mergedLines, mergedColumns, g2.getDensity());
    }

    private GridAlgorithmOutput computeCL() throws CloneNotSupportedException {
        List<Integer> mergedLines = new LinkedList<Integer>();
        List<Integer> mergedColumns = new LinkedList<Integer>();
        Grid g, g2, gClone;
        g = this.grid.simplify();
        gClone = (Grid) g.clone();

        outer : for(int column = gClone.getSizey()-2; column >= 0; column--){
            for(int line = 0; line < gClone.getSizex(); line++) {
                if(gClone.hasPoint(line, column) && gClone.hasPoint(line, column+1))
                    continue outer;
            }
            mergedColumns.add(column);
            for(int line = 0; line < gClone.getSizex(); line++) {
                if(gClone.hasPoint(line, column+1))
                    gClone.addPoint(line, column);
            }
        }

        g = g.mergeColumns(mergedColumns);
        gClone = (Grid) g.clone();

        outer : for(int line = gClone.getSizex()-2; line >= 0; line--){
            for(int column = 0; column < gClone.getSizey(); column++) {
                if(gClone.hasPoint(line, column) && gClone.hasPoint(line+1, column))
                    continue outer;
            }
            mergedLines.add(line);
            for(int column = 0; column < gClone.getSizey(); column++) {
                if(gClone.hasPoint(line+1, column))
                    gClone.addPoint(line, column);
            }
        }


        g2 = g.mergeLines(mergedLines);

        return new GridAlgorithmOutput(g2, mergedLines, mergedColumns, g2.getDensity());
    }

}
