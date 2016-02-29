import algorithms.*;
import instances.Grid;
import instances.IndexOutOfBoundGridException;
import utils.FileManager;

import java.util.Arrays;

/**
 * Created by mouton on 25/02/16.
 */
public class Main {
    public static void main(String[] args){
        Grid g = new Grid(5,5);
        g.addPoint(2,0);
        g.addPoint(4,0);

        System.out.println(g);
        System.out.println(g.getDensity());

        GreedyAlgorithm gr = new GreedyAlgorithm(g);
        gr.compute();
        System.out.println(gr.getOutputGrid());
        System.out.println(gr.getOutputDensity());
    }
}
