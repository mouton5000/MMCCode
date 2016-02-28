package instances;

/**
 * Created by mouton on 04/02/16.
 */
public class IndexOutOfBoundGridException extends RuntimeException {
    IndexOutOfBoundGridException(int sizex, int sizey, int line, int column){
        super("Try to access to coordinates (" + line +","+column+") in a " + sizex + "x" + sizey + " grid." );
    }
}
