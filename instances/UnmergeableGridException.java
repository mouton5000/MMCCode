package instances;

import java.util.List;

/**
 * Created by mouton on 04/02/16.
 */
public abstract class UnmergeableGridException extends RuntimeException{
    UnmergeableGridException(String message){
        super(message);
    }
}

class UnmergeableLineGridException extends UnmergeableGridException {
    UnmergeableLineGridException(List<Integer> lines, int x, int y){
        super("Try to merge lines " + lines + ", " +
                "but the results contains two points at coodinates (" + x +", "+y+")");
    }
}

class UnmergeableColumnGridException extends UnmergeableGridException {
    UnmergeableColumnGridException(List<Integer> columns, int x, int y){
        super("Try to merge lines " + columns + ", " +
                "but the results contains two points at coodinates (" + x +", "+y+")");
    }
}