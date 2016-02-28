import algorithms.*;
import instances.Grid;
import instances.IndexOutOfBoundGridException;

import java.util.Arrays;

/**
 * Created by mouton on 25/02/16.
 */
public class Main {

    public static void main(String[] args) throws IndexOutOfBoundGridException {

        test1();

    }


    public static void test1(){

        for(int size = 5; size <= 15; size+=5) {
            for (double p = 0.1; p <= 0.5; p += 0.1) {
                for (int i = 0; i <= 50; i++) {
                    Grid g = Grid.getRandomGrid(size, size, p);

                    long time;

//                    time = System.currentTimeMillis();
//                    EnumerationAlgorithm alg = new EnumerationAlgorithm(g);
//                    alg.compute();
//                    long algtime = System.currentTimeMillis()-time;

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

                    System.out.println(
                            size + " " +
                                    p +" " +
                                    i +" " +
//                                  algtime + " " +
//                                  alg.getOutputDensity() +" " +
                                    timelcl +" "+
                                    algLCL.getOutputDensity()+" "+
                                    timeGreedy +" "+
                                    algGreed.getOutputDensity()+" "+
                                    timeNeigh +" "+
                                    algNeigh.getOutputDensity()+" "+
                                    " "
                    );

                }
            }
        }
    }

    public static void test2(){
        for(int i = 0 ; i<1; i++) {
            System.out.println(i);
            int n = 5;
            Grid g = Grid.getRandomGrid(n, n, 3);

            System.out.println(g);

            NeighborizationAlgorithm alg = new NeighborizationAlgorithm(g);
            alg.compute();


//            LCLApproximationAlgorithm alg2 = new LCLApproximationAlgorithm(g);
//            alg2.compute();
//
//            System.out.println(Grid.test);
        }

    }

    public static void test3(){
        int[] lclGreedyResults = new int[3];
        int[] lclNeighResults = new int[3];
        int[] greedyNeighResults = new int[3];

        for(int size = 1000; size <= 10000; size+=1000) {
            System.out.print(size+" ");
            for (int n = 100; n<=500; n+=100) {
                long sumLCL = 0;
                long sumGreed = 0;
                for (int i = 0; i <= 50; i++) {
                    Grid g = Grid.getRandomGrid(size, size, n);

                    long time;

//                    GreedyAlgorithm algGreed = new GreedyAlgorithm(g);
//                    time = System.currentTimeMillis();
//                    algGreed.compute();
//                    long timeGreedy = System.currentTimeMillis()-time;
//                    sumGreed += timeGreedy;
//                    int dGreed = algGreed.getOutputDensity();

                    LCLApproximationAlgorithm algLCL = new LCLApproximationAlgorithm(g);
                    time = System.currentTimeMillis();
                    algLCL.compute();
                    long timelcl = System.currentTimeMillis()-time;
                    sumLCL += timelcl;
                    int dlcl = algLCL.getOutputDensity();



//                    NeighborizationAlgorithm algNeigh = new NeighborizationAlgorithm(g);
//                    algNeigh.compute();
//                    int dNeigh = algNeigh.getOutputDensity();

//                    if(dlcl < dGreed)
//                        lclGreedyResults[0]++;
//                    else if(dlcl == dGreed)
//                        lclGreedyResults[1]++;
//                    else
//                        lclGreedyResults[2]++;

//                    if(dlcl < dNeigh)
//                        lclNeighResults[0]++;
//                    else if(dlcl == dNeigh)
//                        lclNeighResults[1]++;
//                    else
//                        lclNeighResults[2]++;
//
//                    if(dGreed < dNeigh)
//                        greedyNeighResults[0]++;
//                    else if(dGreed == dNeigh)
//                        greedyNeighResults[1]++;
//                    else
//                        greedyNeighResults[2]++;
                }
                System.out.print(sumLCL/50.0 + " "+sumGreed/50.0+" ");
            }
            System.out.println();
        }

        System.out.println("lcl VS greedy : " + Arrays.toString(lclGreedyResults));
//        System.out.println("lcl VS neigh : " + Arrays.toString(lclNeighResults));
//        System.out.println("greedy VS neigh : " + Arrays.toString(greedyNeighResults));
    }

}
