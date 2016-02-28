package algorithms;

import instances.Grid;
import utils.BranchAndBoundAlgorithm;
import utils.BranchAndBoundNode;
import utils.Couple;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mouton on 05/02/16.
 */
public class BBGridAlgorithm extends GridAlgorithm {

    public BBGridAlgorithm(Grid grid) {
        super(grid);
    }

    @Override
    public void compute() {
        BBGA alg = new BBGA(this.grid);
        BBGN node = alg.compute();

        this.output = node.getOutput();


    }
}

class BBGA extends BranchAndBoundAlgorithm<BBGN>{

    Grid grid;

    BBGA(Grid grid){
        this.grid = grid;
    }

    @Override
    public BBGN getRoot() {
        return new BBGN(this.grid, new LinkedList<Integer>(), new LinkedList<Integer>(), this.grid.getDensity());
    }
}

class BBGN implements BranchAndBoundNode<BBGN>{

    private Grid grid;
    private List<Integer> lines;
    private List<Integer> columns;
    private List<Integer> mergeableLines;
    private List<Integer> mergeableColumns;
    private int density;

    private List<BBGN> children;

    BBGN(Grid grid, List<Integer> lines, List<Integer> columns, int density){
        this.grid = grid;
        this.lines = lines;
        this.columns = columns;
        this.density = density;

        this.mergeableLines = this.grid.mergeableLines();
        this.mergeableColumns = this.grid.mergeableColumns();

        this.children = new LinkedList<BBGN>();
    }

    @Override
    public void explore() {
        for(int line : mergeableLines){
            List<Integer> newLines = new LinkedList<Integer>(lines);
            newLines.add(line);
            List<Integer> newColumns = new LinkedList<Integer>(columns);
            Grid g = this.grid.mergeLine(line);
            children.add(new BBGN(g, newLines, newColumns, g.getDensity()));
        }
        for(int column : mergeableColumns){
            List<Integer> newColumns = new LinkedList<Integer>(columns);
            newColumns.add(column);
            List<Integer> newLines = new LinkedList<Integer>(lines);
            Grid g = this.grid.mergeColumn(column);
            children.add(new BBGN(g, newLines, newColumns, g.getDensity()));
        }
    }

    @Override
    public boolean isLeaf() {
        return this.mergeableColumns.isEmpty() && this.mergeableLines.isEmpty();
    }

    @Override
    public List<BBGN> getChildren() {
        return children;
    }

    @Override
    public int getLowerBound() {
        LinkedList<Couple<Integer,Integer>> borderLines = new LinkedList<Couple<Integer, Integer>>();
        Integer previous = null;
        Integer left = null;
        for(int line : mergeableLines){
            if(previous == null){
                previous = line;
                left = line;
                continue;
            }
            if(previous+2 < line){
                borderLines.add(new Couple<Integer, Integer>(left-1, previous+1));
                left = line;
            }
            previous = line;
        }

        LinkedList<Couple<Integer,Integer>> borderColumns = new LinkedList<Couple<Integer, Integer>>();
        previous = null;
        left = null;

        for(int column : mergeableColumns){
            if(previous == null){
                previous = column;
                left = column;
                continue;
            }
            if(previous+2 < column){
                borderColumns.add(new Couple<Integer, Integer>(left-1, previous+1));
                left = column;
            }
            previous = column;
        }

        int bound = this.density;
        int nbPoints;

        for(int bl = 0; bl < borderLines.size(); bl++){
            Couple<Integer,Integer> cl = borderLines.get(bl);
            for(int bc = 0; bc < borderColumns.size(); bc++) {
                Couple<Integer, Integer> cc = borderColumns.get(bc);
                nbPoints = 0;
                for (int l = cl.first; l <= cl.second; l++) {
                    for (int c = cc.first; c <= cc.second; c++) {
                        if (this.grid.hasPoint(l, c))
                            nbPoints++;
                    }
                }
                bound += 4 * nbPoints - 6 * Math.sqrt(nbPoints) + 2;
            }
        }

        return -1 * bound;
    }

    @Override
    public int getUpperBound() {
        return -1 * this.density;
    }

    @Override
    public int compareTo(BBGN bbgn) {
        int s1 = this.mergeableLines.size() + this.mergeableColumns.size();
        int s2 = bbgn.mergeableLines.size() + bbgn.mergeableColumns.size();

        int c1 = Integer.compare(s1, s2);
        if (c1 != 0)
            return c1;
        else {
            int c2 = Integer.compare(this.density, bbgn.density);
            if (c2 != 0)
                return c2;
            else {
                return -1;
            }
        }
    }

    public GridAlgorithmOutput getOutput(){
        return new GridAlgorithmOutput(this.grid, this.lines, this.columns, this.density);
    }

    @Override
    public String toString() {
        return this.grid.toString();
    }
}
