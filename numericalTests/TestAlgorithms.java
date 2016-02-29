package numericalTests;

import algorithms.EnumerationAlgorithm;
import algorithms.GreedyAlgorithm;
import algorithms.LCLApproximationAlgorithm;
import algorithms.NeighborizationAlgorithm;
import instances.Grid;
import instances.IndexOutOfBoundGridException;
import utils.FileManager;

import java.util.Arrays;

/**
 * Created by mouton on 29/02/16.
 */
public class TestAlgorithms {

    public static void main(String[] args) throws IndexOutOfBoundGridException {

        test5();

    }


    public static void test1(){

        double[] probs = {0.01,0.02,0.03,0.04,0.05,0.1,0.2,0.3};

        FileManager fm = new FileManager();
        fm.openWrite("test1Results.txt");

        fm.writeln("##############################");

        for(int size = 5; size <= 20; size+=5) {
            for (double p : probs) {
                for (int i = 0; i <= 50; i++) {
                    System.out.println(size+" "+p+" "+i);
                    Grid g = Grid.getRandomGrid(size, size, p);

                    long time;
                    time = System.currentTimeMillis();
                    EnumerationAlgorithm alg = new EnumerationAlgorithm(g);
                    alg.compute();
                    long algtime = System.currentTimeMillis()-time;

                    time = System.currentTimeMillis();
                    LCLApproximationAlgorithm algLCL = new LCLApproximationAlgorithm(g);
                    algLCL.compute();
                    long timelcl = System.currentTimeMillis()-time;

                    time = System.currentTimeMillis();
                    GreedyAlgorithm algGreed = new GreedyAlgorithm(g);
                    algGreed.compute();
                    long timeGreedy = System.currentTimeMillis()-time;

                    time = System.currentTimeMillis();
                    NeighborizationAlgorithm algNeigh = new NeighborizationAlgorithm(g);
                    algNeigh.compute();
                    long timeNeigh = System.currentTimeMillis()-time;


                    String s= size + " " +
                            p +" " +
                            i +" " +
                            algtime + " " +
                            alg.getOutputDensity() +" " +
                            timelcl +" "+
                            algLCL.getOutputDensity()+" "+
                            timeGreedy +" "+
                            algGreed.getOutputDensity()+" "+
                            timeNeigh +" "+
                            algNeigh.getOutputDensity();


                    fm.writeln(s);
                }
                fm.flush();
            }
        }
    }

    public static void test4(){

        FileManager fm = new FileManager();
        fm.openWrite("test2Results.txt");

        fm.writeln("# Size ExpectedDensity IndexOfInstance timeLCL DensityLCL timeGreedy DensityGreedy");

        int[] sizes = {100,200,500,1000,2000,5000,10000};
        double[] probs = {0.01,0.02,0.03,0.04,0.05,0.1,0.2,0.3};
        for(int size : sizes) {
            for (double p : probs) {
                for (int i = 0; i <= 50; i++) {

                    System.out.println(size+" "+p+" "+i);
                    Grid g = Grid.getRandomGrid(size, size, p);

                    long time;
                    time = System.currentTimeMillis();
                    LCLApproximationAlgorithm algLCL = new LCLApproximationAlgorithm(g);
                    algLCL.compute();
                    long timelcl = System.currentTimeMillis()-time;

                    time = System.currentTimeMillis();
                    GreedyAlgorithm algGreed = new GreedyAlgorithm(g);
                    algGreed.compute();
                    long timeGreedy = System.currentTimeMillis()-time;

                    String s= size + " " +
                            p +" " +
                            i +" " +
                            timelcl +" "+
                            algLCL.getOutputDensity()+" "+
                            timeGreedy +" "+
                            algGreed.getOutputDensity();

                    fm.writeln(s);

                }
                fm.flush();
            }
        }
    }

    public static void test5(){

        int[] sizes = {100,200,500,1000,2000,5000,10000};
        double[] probs = {0.01,0.02,0.03,0.04,0.05,0.1,0.2,0.3};
        for(int size : sizes) {
            for (double p : probs) {
                for (int i = 0; i <= 50; i++) {

                    System.out.println(size+" "+p+" "+i);
                    Grid g = Grid.getRandomGrid(size, size, p);

                    long time;
                    time = System.currentTimeMillis();
                    NeighborizationAlgorithm algLCL = new NeighborizationAlgorithm(g);
                    algLCL.compute();
                    long timelcl = System.currentTimeMillis()-time;

                    String s = size + " " +
                            p +" " +
                            i +" " +
                            timelcl +" "+
                            algLCL.getOutputDensity()+" ";

                    System.out.println(s);

                }
            }
        }
    }
}
