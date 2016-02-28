package algorithms;

import instances.Grid;
import instances.UnmergeableGridException;
import utils.SubsetIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mouton on 04/02/16.
 */
public class EnumerationAlgorithm extends GridAlgorithm{

    public EnumerationAlgorithm(Grid grid){
        super(grid);
    }

    @Override
    public void compute(){
        Grid current = this.grid.simplify();

        List<Integer> lines = current.mergeableLines();

        SubsetIterator<Integer> linesIt = new SubsetIterator<Integer>(lines);

        GridAlgorithmOutput best = new GridAlgorithmOutput(null, null, null, -1);

        while(linesIt.hasNext()){
            try {
                List<Integer> subLines = linesIt.next();
                Grid g1 = current.mergeLines(subLines);
                List<Integer> columns = g1.mergeableColumns();
                SubsetIterator<Integer> columnsIt = new SubsetIterator<Integer>(columns);
                while(columnsIt.hasNext()){
                    try {
                        List<Integer> subColumns = columnsIt.next();
                        Grid g2 = g1.mergeColumns(subColumns);

                        int density = g2.getDensity();
                        if (density > best.getDensity()) {
                            best.setGrid(g2);
                            best.setDensity(density);
                            best.setLines(subLines);
                            best.setColumns(subColumns);

                        }
                    }
                    catch(UnmergeableGridException e){
                        continue;
                    }
                }
            }
            catch(UnmergeableGridException e){
                continue;
            }

        }

        this.output = best;

    }
}
