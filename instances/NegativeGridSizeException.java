package instances;

/**
 * Created by mouton on 04/02/16.
 */
public class NegativeGridSizeException extends RuntimeException {
    NegativeGridSizeException(int sizex, int sizey){
        super("Try to create a " + sizex + "x" + sizey + " grid." );
    }
}
