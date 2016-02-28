package algorithms;

import instances.Grid;
import instances.UnmergeableGridException;
import utils.Couple;

import java.util.*;

/**
 * Created by mouton on 25/02/16.
 */
public class NeighborizationAlgorithm extends GridAlgorithm {


    public NeighborizationAlgorithm(Grid grid) {
        super(grid);
    }

    @Override
    public void compute() {


        List<Integer> lines = new LinkedList<Integer>();
        List<Integer> columns = new LinkedList<Integer>();


        Grid current = this.grid.simplify();
        List<Integer> mergeableLines = current.mergeableLines();
        List<Integer> mergeableColumns = current.mergeableColumns();

        while(!mergeableLines.isEmpty() || !mergeableColumns.isEmpty()){
            int bestNbPossibleNeighbours = -1;
            boolean lineBetter = true;
            Integer bestLineOrColumn = null;
            Grid best = null;

            for(Integer line : mergeableLines){
                Grid test = current.mergeLine(line);
                int testNbPossibleNeighbours = NeighborizationAlgorithm.numberOfNeighbourizable(test);
                if(testNbPossibleNeighbours > bestNbPossibleNeighbours){
                    bestNbPossibleNeighbours = testNbPossibleNeighbours;
                    best = test;
                    bestLineOrColumn = line;
                }
            }

            for(Integer column : mergeableColumns){
                Grid test = current.mergeColumn(column);
                int testNbPossibleNeighbours = NeighborizationAlgorithm.numberOfNeighbourizable(test);
                if(testNbPossibleNeighbours > bestNbPossibleNeighbours){
                    bestNbPossibleNeighbours = testNbPossibleNeighbours;
                    best = test;
                    lineBetter = false;
                    bestLineOrColumn = column;
                }
            }

            current = best;
            mergeableLines = current.mergeableLines();
            mergeableColumns = current.mergeableColumns();
            if(lineBetter)
                lines.add(bestLineOrColumn);
            else
                columns.add(bestLineOrColumn);
        }

        lines = Grid.iterativeListToGlobalList(lines);
        columns = Grid.iterativeListToGlobalList(columns);



        output = new GridAlgorithmOutput(current, lines, columns, current.getDensity());

    }

    private static int numberOfNeighbourizable(Grid g){
        List<PossibleNeighbours> possibleNeighbourss = NeighborizationAlgorithm.possibleNeighbours(g);

        int nb = 0;
        for (PossibleNeighbours possibleNeighbours : possibleNeighbourss) {
            Point pt1 = possibleNeighbours.pt1;
            Point pt2 = possibleNeighbours.pt2;

            if (neighbourizable(g, pt1, pt2))
                nb++;
        }
        return nb;
    }


    private static boolean neighbourizable(Grid g, Point pt1, Point pt2){
        int x1 = pt1.x;
        int x2 = pt2.x;
        int y1 = pt1.y;
        int y2 = pt2.y;

        if(x2 < x1){
            x2 = x1 + x2;
            x1 = x2 - x1;
            x2 = x2 - x1;
        }

        if(y2 < y1){
            y2 = y1 + y2;
            y1 = y2 - y1;
            y2 = y2 - y1;
        }

        Grid test1;

        for(int x = x1; x<x2; x++){
            List<Integer> lines = new LinkedList<Integer>();
            for(int xp = x1; xp<x2; xp++)
                if(xp != x)
                    lines.add(xp);

            try { test1 = g.mergeLines(lines); }
            catch(UnmergeableGridException e) {continue;}


            List<Integer> columns = new LinkedList<Integer>();
            for (int y = y1; y < y2; y++)
                columns.add(y);

            try {
                test1.mergeColumns(columns);
                return true;
            }
            catch(UnmergeableGridException e) {}


            for (int y = y1; y < y2; y++) {
                columns = new LinkedList<Integer>();
                for (int yp = y1; yp < y2; yp++)
                    if (yp != y)
                        columns.add(yp);

                try {
                    test1.mergeColumns(columns);
                    return true;
                }
                catch(UnmergeableGridException e) {}
            }
        }

        for(int y = y1; y < y2; y++) {
            List<Integer> columns = new LinkedList<Integer>();
            for (int yp = y1; yp < y2; yp++)
                if (yp != y)
                    columns.add(yp);

            try { test1 = g.mergeColumns(columns); }
            catch(UnmergeableGridException e) {continue;}


            List<Integer> lines = new LinkedList<Integer>();
            for (int x = x2-1; x >= x1; x--)
                lines.add(x);

            try { test1.mergeLines(lines);
                return true;}
            catch(UnmergeableGridException e) {}

        }
        return false;
    }

    private static List<PossibleNeighbours> possibleNeighbours(Grid g){
        List<PossibleNeighbours> visibleNeighbours = NeighborizationAlgorithm.visibleNeighbours(g);

        Set<PossibleNeighbours> possibleNeighbours = new HashSet<PossibleNeighbours>();
        for(PossibleNeighbours c : visibleNeighbours){
            Point pt1 = c.pt1;
            Point pt2 = c.pt2;

            if(!(pt1.x <= pt2.x+1 && pt1.y >= pt2.y-1 && pt1.y <= pt2.y+1))
                possibleNeighbours.add(c);
        }

        for(int i = 0; i<visibleNeighbours.size(); i++){
            for(int j = i+1; j<visibleNeighbours.size(); j++){
                PossibleNeighbours c1 = visibleNeighbours.get(i);
                PossibleNeighbours c2 = visibleNeighbours.get(j);

                Point pt11 = c1.pt1;
                Point pt12 = c1.pt2;

                Point pt21 = c2.pt1;
                Point pt22 = c2.pt2;

                if(pt12.equals(pt21) && pt11.x != pt22.x && pt11.y != pt22.y
                        && !(pt11.x >= pt22.x-1 && pt11.x <= pt22.x+1 && pt11.y >= pt22.y-1 && pt11.y <= pt22.y+1))
                    possibleNeighbours.add(new PossibleNeighbours(pt11, pt22));
                else if(pt11.equals(pt22) && pt12.x != pt21.x && pt12.y != pt21.y
                        && !(pt12.x >= pt21.x-1 && pt12.x <= pt21.x+1 && pt12.y >= pt21.y-1 && pt12.y <= pt21.y+1))
                    possibleNeighbours.add(new PossibleNeighbours(pt12, pt21));
            }
        }
        return new ArrayList<>(possibleNeighbours);
    }


    private static List<PossibleNeighbours> visibleNeighbours(Grid g){
        Map<Integer, List<Point>> pointsX = new HashMap<Integer, List<Point>>();
        Map<Integer, List<Point>> pointsY = new HashMap<Integer, List<Point>>();

        for(int x = 0; x<g.getSizex();x++){
            for(int y = 0; y<g.getSizey();y++){
                if(g.hasPoint(x,y)){
                    List<Point> line = pointsX.get(x);
                    if(line == null){
                        line = new ArrayList<Point>();
                        pointsX.put(x, line);
                    }
                    line.add(new NeighborizationAlgorithm.Point(x,y));

                    List<Point> column = pointsY.get(y);
                    if(column == null){
                        column = new ArrayList<Point>();
                        pointsY.put(y, column);
                    }
                    column.add(new Point(x,y));
                }
            }
        }

        List<PossibleNeighbours> visibleNeighbours = new ArrayList<PossibleNeighbours>();

        for(Map.Entry<Integer, List<Point>> pair : pointsX.entrySet()){
            Integer x = pair.getKey();
            List<Point> line = pair.getValue();

            for(int i = 0; i < line.size()-1; i++){
                Point pt = line.get(i);
                Point pt2 = line.get(i+1);
                visibleNeighbours.add(new PossibleNeighbours(pt, pt2));
            }


            for(Point pt : line){
                int y = pt.y;
                int maxY = g.getSizey();
                int nextMaxY = maxY;
                int countdown;

                for(int x2 = x-1; x2>=0;x2--){
                    List<Point> line2 = pointsX.get(x2);
                    if(line2 == null)
                        continue;
                    countdown = 2;
                    for(Point pt2 : line2){
                        int y2 = pt2.y;
                        if(y2 <= y)
                            continue;
                        if(y2 >= maxY || countdown == 0)
                            break;
                        visibleNeighbours.add(new PossibleNeighbours(pt, pt2));
                        nextMaxY = Math.min(y2+1,nextMaxY);
                        countdown--;
                    }
                    maxY = nextMaxY;
                }


                maxY = g.getSizey();
                nextMaxY = maxY;
                for(int x2 = x+1; x2<g.getSizex();x2++){
                    List<Point> line2 = pointsX.get(x2);
                    if(line2 == null)
                        continue;
                    countdown = 2;
                    for(Point pt2 : line2){
                        int y2 = pt2.y;
                        if(y2 <= y)
                            continue;
                        if(y2 >= maxY || countdown == 0)
                            break;
                        visibleNeighbours.add(new PossibleNeighbours(pt, pt2));
                        nextMaxY = Math.min(y2+1,nextMaxY);
                        countdown--;
                    }
                    maxY = nextMaxY;
                }

            }
        }

        for(List<Point> column : pointsY.values()) {
            for (int i = 0; i < column.size() - 1; i++){
                Point pt = column.get(i);
                Point pt2 = column.get(i+1);
                visibleNeighbours.add(new PossibleNeighbours(pt, pt2));
            }
        }

        return visibleNeighbours;
    }

    private static class Point{
        int x;
        int y;


        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return (x * 31) ^ y;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Point))
                return false;
            return this.x == ((Point) o).x && this.y == ((Point) o).y;
        }

        @Override
        public String toString() {
            return "("+x + " " +y+")";
        }
    }

    private static class PossibleNeighbours{
        Point pt1;
        Point pt2;

        public PossibleNeighbours(Point pt1, Point pt2) {
            if(pt1.x < pt2.x || (pt1.x == pt2.x && pt1.y < pt2.y)){
                this.pt1 = pt1;
                this.pt2 = pt2;
            }
            else {
                this.pt2= pt1;
                this.pt1 = pt2;
            }
        }

        @Override
        public int hashCode() {
            return pt1.hashCode() + pt2.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof PossibleNeighbours))
                return false;
            return this.pt1.equals(((PossibleNeighbours) o).pt1) && this.pt2.equals(((PossibleNeighbours) o).pt2);
        }
    }
}


