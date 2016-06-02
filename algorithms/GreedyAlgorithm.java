package algorithms;

import instances.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mouton on 24/02/16.
 */
public class GreedyAlgorithm extends GridAlgorithm{

    public GreedyAlgorithm(Grid grid) {
        super(grid);
    }

    @Override
    public void compute() {


        Grid current = this.grid.simplify();

        List<Integer> ml = current.mergeableLines();
        List<Integer> mc = current.mergeableColumns();

        List<Integer> lines = new ArrayList<Integer>();
        List<Integer> columns = new ArrayList<Integer>();

        int currentDensity = current.getDensity();

        while(!ml.isEmpty() || !mc.isEmpty()){
            boolean lineBetter = true;
            Integer bestLineColumn = null;
            int bestIncreaseDensity = -1;
            for(Integer line : ml){
                Integer testIncreaseDensity = GreedyAlgorithm.getLineDensityIncrease(current, line);
                if(testIncreaseDensity != null && testIncreaseDensity > bestIncreaseDensity){
                    bestIncreaseDensity = testIncreaseDensity;
                    bestLineColumn = line;
                }
            }
            for(Integer column : mc){
                Integer testIncreaseDensity = GreedyAlgorithm.getColumnDensityIncrease(current, column);
                if(testIncreaseDensity != null && testIncreaseDensity > bestIncreaseDensity){
                    bestIncreaseDensity = testIncreaseDensity;
                    bestLineColumn = column;
                    lineBetter = false;
                }
            }
            if(lineBetter) {
                lines.add(bestLineColumn);
                current = current.mergeLine(bestLineColumn);
            }
            else {
                columns.add(bestLineColumn);
                current = current.mergeColumn(bestLineColumn);
            }
            ml = current.mergeableLines();
            mc = current.mergeableColumns();
            currentDensity += bestIncreaseDensity;
        }

        lines = Grid.iterativeListToGlobalList(lines);
        columns = Grid.iterativeListToGlobalList(columns);

        output = new GridAlgorithmOutput(current, lines, columns, currentDensity);
    }

    public static Integer getLineDensityIncrease(Grid g, int line){
        Integer increase = 0;
        for(int column = 0; column < g.getSizey(); column++){
            if(line != 0 && g.hasPoint(line+1,column)){
                if(column != 0 && g.hasPoint(line-1, column-1))
                    increase++;
                if(column != g.getSizey()-1 && g.hasPoint(line-1, column+1))
                    increase++;
                if(g.hasPoint(line-1, column))
                    increase++;
            }
            if(line != g.getSizex()-2 && g.hasPoint(line, column)){
                if(column != 0 && g.hasPoint(line+2, column-1))
                    increase++;
                if(column != g.getSizey()-1 && g.hasPoint(line+2, column+1))
                    increase++;
                if(g.hasPoint(line+2, column))
                    increase++;
            }
        }
        return increase;
    }

    public static Integer getColumnDensityIncrease(Grid g, int column){
        Integer increase = 0;
        for(int line = 0; line < g.getSizex(); line++){
            if(column != 0 && g.hasPoint(line, column+1)){
                if(line != 0 && g.hasPoint(line-1, column-1))
                    increase++;
                if(line != g.getSizex()-1 && g.hasPoint(line+1, column-1))
                    increase++;
                if(g.hasPoint(line, column-1))
                    increase++;
            }
            if(column != g.getSizey()-2 && g.hasPoint(line, column)){
                if(line != 0 && g.hasPoint(line-1, column+2))
                    increase++;
                if(line != g.getSizex()-1 && g.hasPoint(line+1, column+2))
                    increase++;
                if(g.hasPoint(line, column+2))
                    increase++;
            }
        }
        return increase;
    }
}
