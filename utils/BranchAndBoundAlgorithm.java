package utils;

import java.util.*;

/**
 * Created by mouton on 05/02/16.
 */
public abstract class BranchAndBoundAlgorithm<T extends BranchAndBoundNode<T>> {

    public abstract T getRoot();


    public T compute(){
        T root = this.getRoot();

        TreeSet<T> toExplore = new TreeSet<T>();
        toExplore.add(root);

        T bestLeaf = root;
        int upperBound = root.getUpperBound();

        while(!toExplore.isEmpty()){
            T node = toExplore.pollFirst();
            node.explore();
            boolean isLeaf = node.isLeaf();

            if(node.getUpperBound() < upperBound){
                upperBound = node.getUpperBound();
                if(isLeaf)
                    bestLeaf = node;
            }

            if(!isLeaf)
                for(T bbn : node.getChildren()){
                    if(bbn.getLowerBound() > upperBound)
                        continue;
                    toExplore.add(bbn);
                }
        }
        return bestLeaf;

    }

    public static void main(String[] args){

        ArrayList<Integer> weights = new ArrayList<Integer>(Arrays.asList(1,5,7,3,4,6,2));

        class TestBBN implements BranchAndBoundNode<TestBBN> {

            int index;
            LinkedList<Integer> values;

            TestBBN leftChild;
            TestBBN rightChild;

            public TestBBN(int index, LinkedList<Integer> values){
                this.values = values;
                this.index = index;
            }

            @Override
            public void explore() {
                LinkedList<Integer> leftValues = new LinkedList<Integer>(values);
                LinkedList<Integer> rightValues = new LinkedList<Integer>(values);

                int value = weights.get(index+1);
                rightValues.add(value);

                leftChild = new TestBBN(index + 1, leftValues);
                rightChild = new TestBBN(index + 1, rightValues);
            }

            @Override
            public boolean isLeaf() {
                return values.size() == 3 || index == weights.size()-1;
            }

            @Override
            public List<TestBBN> getChildren() {
                return Arrays.asList(leftChild, rightChild);
            }

            @Override
            public int getLowerBound() {
                int s = 0;
                for(int i : values)
                    s += i;
                Iterator<Integer> it = weights.listIterator(index+1);
                int max = Integer.MIN_VALUE;
                while(it.hasNext()){
                    max = Math.max(max, it.next());
                }

                int v1 = 3 - values.size();
                int v2 = weights.size() - index;
                int v = Math.min(v1, v2);

                return -1 * (s + max * v);
            }

            @Override
            public int getUpperBound() {
                int s = 0;
                for(int i : values)
                    s += i;
                Iterator<Integer> it = weights.listIterator(index+1);
                int min = Integer.MAX_VALUE;
                while(it.hasNext()){
                    min = Math.min(min, it.next());
                }

                int v1 = 3 - values.size();
                int v2 = weights.size() - index;
                int v = Math.min(v1, v2);

                return -1 * (s + min * v);
            }

            @Override
            public int compareTo(TestBBN testBBN) {
                return Integer.compare(this.getUpperBound(), testBBN.getUpperBound());
            }

            @Override
            public String toString() {
                return "("  + index + " " + values.toString() + ")";
            }
        }

        class BBN extends BranchAndBoundAlgorithm<TestBBN>{

            @Override
            public TestBBN getRoot() {
                LinkedList<Integer> l = new LinkedList<Integer>();
                return new TestBBN(-1, l);
            }
        }

        BBN alg = new BBN();

        TestBBN node = alg.compute();
    }

}

