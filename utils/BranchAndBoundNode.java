package utils;

import java.util.List;

/**
 * Created by mouton on 05/02/16.
 */
public interface BranchAndBoundNode<T extends BranchAndBoundNode> extends Comparable<T>{
    public void explore();
    public boolean isLeaf();
    public List<T> getChildren();
    public int getLowerBound();
    public int getUpperBound();
}
