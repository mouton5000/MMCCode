package instances;

import utils.Collections2;
import utils.StringUtils;

import java.util.*;

/**
 * Created by mouton on 03/02/16.
 */
public class Grid implements Cloneable{

    private int sizex;
    private int sizey;
    private boolean[][] grid;

    public Grid(int sizex, int sizey) throws NegativeGridSizeException{
        this.sizex = sizex;
        this.sizey = sizey;
        try{
            this.grid = new boolean[sizex][sizey];
        }
        catch (NegativeArraySizeException e){
            throw new NegativeGridSizeException(sizex, sizey);
        }
    }

    public int getSizex() {
        return sizex;
    }

    public int getSizey() {
        return sizey;
    }

    /**
     *
     * @param x entre 0 et sizex-1
     * @param y entre 0 et sizey-1
     * @throws IndexOutOfBoundGridException si x < 0 ou x > sizex-1 ou y < 0 ou y > sizey-1
     */
    public void addPoint(int x, int y) throws IndexOutOfBoundGridException {
        try {
            this.grid[x][y] = true;
        }
        catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundGridException(this.sizex, this.sizey, x, y);
        }
    }

    /**
     *
     * @param x entre 0 et sizex-1
     * @param y entre 0 et sizey-1
     * @throws IndexOutOfBoundGridException si x < 0 ou x > sizex-1 ou y < 0 ou y > sizey-1
     */
    public void removePoint(int x, int y) throws IndexOutOfBoundGridException {
        try {
            this.grid[x][y] = false;
        }
        catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundGridException(this.sizex, this.sizey, x, y);
        }
    }

    /**
     *
     * @param x entre 0 et sizex-1
     * @param y entre 0 et sizey-1
     * @throws IndexOutOfBoundGridException si x < 0 ou x > sizex-1 ou y < 0 ou y > sizey-1
     */
    public boolean invertPoint(int x, int y) throws IndexOutOfBoundGridException {
        try {
            return this.grid[x][y] = !this.grid[x][y];
        }
        catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundGridException(this.sizex, this.sizey, x, y);
        }
    }

    /**
     *
     * @param x : entre 0 et sizex-1
     * @param y : entre 0 et sizey-1
     * @return vrai si la grille possède un point en les coordonnées (x,y).
     * @throws IndexOutOfBoundGridException  si x < 0 ou x > sizex-1 ou y < 0 ou y > sizey-1
     */
    public boolean hasPoint(int x, int y) throws IndexOutOfBoundGridException {
        try {
            return this.grid[x][y];
        }
        catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundGridException(this.sizex, this.sizey, x, y);
        }
    }

    public static Grid getRandomGrid(int sizex, int sizey, double prob){
        Grid g = new Grid(sizex, sizey);
        for(int line = 0; line<sizex; line++)
            for(int column = 0; column<sizey; column++)
                if(Math.random() < prob)
                    g.addPoint(line,column);
        return g;
    }

    public static Grid getRandomGrid(int sizex, int sizey, int n){
        HashSet<Integer> subRange = Collections2.randomSubrange(sizex * sizey, n);
        Grid g = new Grid(sizex, sizey);
        for(Integer index : subRange){
            int line = index%sizex;
            int column = index/sizex;
            g.addPoint(line, column);
        }
        return g;
    }

    /**
     *
     * @return la liste des lignes qui peuvent être fusionnées avec la ligne suivante.
     */
    public List<Integer> mergeableLines(){
        ArrayList<Integer> lines = new ArrayList<Integer>();
        for(int x = 0; x<this.sizex-1; x++){
            if(this.canMergeLine(x))
                lines.add(x);
        }
        return lines;
    }

    /**
     *
     * @param line entre 0 et sizex-2
     * @return -1 si la ligne line peut être fusionnée avec la ligne suivante, et l'indice y de la première colonne
     * telle qu'il existe deux points dans la grille à l'intersection de cette colonne et des deux lignes qu'on cherche
     * à fusionner.
     * @throws IndexOutOfBoundGridException si line < 0 ou line > sizex-2
     */
    private int canMergeLineColumnIndex(int line) throws IndexOutOfBoundGridException {
        for(int y = 0; y < this.sizey; y++)
            if (this.hasPoint(line,y) & this.hasPoint(line+1,y))
                return y;
        return -1;
    }

    /**
     * @param line entre 0 et sizex-2
     * @return vrai si la ligne line peut être fusionnée avec la ligne suivante.
     * @throws IndexOutOfBoundGridException si line < 0 ou line > sizex-2
     */
    public boolean canMergeLine(int line) throws IndexOutOfBoundGridException {
        return canMergeLineColumnIndex(line) == -1;
    }

    /**
     *
     * @param line entre 0 et sizex-2
     * @return la grille résultante de la fusion de la ligne line et de la ligne suivante.
     * @throws IndexOutOfBoundGridException si line < 0 ou line > sizex-2
     * @throws UnmergeableColumnGridException si ces deux lignes ne peuvent être fusionnées.
     */
    public Grid mergeLine(int line) throws IndexOutOfBoundGridException {
        List<Integer> lines = Arrays.asList(line);
        return this.mergeLines(lines);
    }

    /**
     * @param lines liste d'entiers entre 0 et sizex-2
     * @return la grille résultante de la fusion de chaque ligne line de lines et de la ligne suivante.
     * @throws IndexOutOfBoundGridException si line < 0 ou line > sizex-2 pour un line de lines
     * @throws UnmergeableColumnGridException si deux lignes de lines ne peuvent être fusionnées.
     */
    public Grid mergeLines(Integer... lines){
        List<Integer> list = Arrays.asList(lines);
        return mergeLines(list);
    }

    /**
     * @param lines liste d'entiers entre 0 et sizex-2
     * @return la grille résultante de la fusion de chaque ligne line de lines et de la ligne suivante.
     * @throws IndexOutOfBoundGridException si line < 0 ou line > sizex-2 pour un line de lines
     * @throws UnmergeableColumnGridException si deux lignes de lines ne peuvent être fusionnées.
     */
    public Grid mergeLines(List<Integer> lines)
            throws IndexOutOfBoundGridException, UnmergeableColumnGridException {
        try {
            if (lines.size() == 0)
                return (Grid) (this.clone());
        }
        catch (CloneNotSupportedException e){
            return null;
        }

        Collections.sort(lines);

        int lastLine = lines.get(lines.size() - 1);
        if (lastLine > this.sizex - 2)
            throw new IndexOutOfBoundGridException(this.sizex, this.sizey, lastLine+1, 0);

        int offset = 0;
        int[] offsets = new int[this.sizex];
        for(int x = 0; x<this.sizex; x++){
            if(offset != lines.size() && x-1 == lines.get(offset))
                offset++;
            offsets[x] = offset;
        }

        Grid g = new Grid(this.sizex-lines.size(), this.sizey);
        for(int x = 0; x<this.sizex; x++)
            for(int y = 0; y<this.sizey; y++)
                if(this.hasPoint(x,y))
                    if(g.hasPoint(x-offsets[x],y))
                        throw new UnmergeableColumnGridException(lines, x, y);
                    else
                        g.addPoint(x-offsets[x],y);
        return g;
    }


    /**
     *
     * @return la liste des colonnes qui peuvent être fusionnées avec la colonne suivante.
     */
    public List<Integer> mergeableColumns(){
        ArrayList<Integer> columns = new ArrayList<Integer>();
        for(int y = 0; y<this.sizey-1; y++){
            if(this.canMergeColumn(y))
                columns.add(y);
        }
        return columns;
    }


    /**
     *
     * @param column entre 0 et sizey-2
     * @return -1 si la colonne column peut être fusionnée avec la colonne suivante, et l'indice x de la première ligne
     * telle qu'il existe deux points dans la grille à l'intersection de cette ligne et des deux colonnes qu'on cherche
     * à fusionner.
     * @throws IndexOutOfBoundGridException si column < 0 ou column > sizey-2
     */
    private int canMergeColumnLineIndex(int column) throws IndexOutOfBoundGridException {
        for(int x = 0; x < this.sizex; x++)
            if (this.hasPoint(x, column) & this.hasPoint(x, column + 1))
                return x;
        return -1;
    }

    /**
     *
     * @param column entre 0 et sizey-2
     * @return vrai si la colonne column peut être fusionnée avec la colonne suivante.
     * @throws IndexOutOfBoundGridException si column < 0 ou column > sizey-2
     */
    public boolean canMergeColumn(int column) throws IndexOutOfBoundGridException {
        return canMergeColumnLineIndex(column) == -1;
    }

    /**
     * @param column entre 0 et sizey-2
     * @return la grille résultante de la fusion de la colonne column et de la colonne suivante.
     * @throws IndexOutOfBoundGridException column < 0 ou column > sizey-2
     * @throws UnmergeableColumnGridException si ces deux colonnes ne peuvent être fusionnées.
     */
    public Grid mergeColumn(int column) throws IndexOutOfBoundGridException, UnmergeableColumnGridException {
        List<Integer> columns = Arrays.asList(column);
        return this.mergeColumns(columns);
    }

    /**
     * @param columns liste d'entiers entre 0 et sizey-2
     * @return la grille résultante de la fusion de chaque colonne column de columns et de la colonne suivante.
     * @throws IndexOutOfBoundGridException si column < 0 ou column > sizey-2 pour un column de columns
     * @throws UnmergeableColumnGridException si deux colonnes de columns ne peuvent être fusionnées.
     */
    public Grid mergeColumns(Integer... columns){
        List<Integer> list = Arrays.asList(columns);
        return mergeColumns(list);
    }

    /**
     *
     * @param columns liste d'entiers entre 0 et sizey-2
     * @return la grille résultante de la fusion de chaque colonne column de columns et de la colonne suivante.
     * @throws IndexOutOfBoundGridException si column < 0 ou column > sizey-2 pour un column de columns
     * @throws UnmergeableColumnGridException si deux colonnes de columns ne peuvent être fusionnées.
     */
    public Grid mergeColumns(List<Integer> columns)
            throws IndexOutOfBoundGridException, UnmergeableColumnGridException {
        try {
            if (columns.size() == 0)
                return (Grid) (this.clone());
        }
        catch (CloneNotSupportedException e){
            return null;
        }

        Collections.sort(columns);

        int lastColumn = columns.get(columns.size() - 1);
        if (lastColumn > this.sizey - 2)
            throw new IndexOutOfBoundGridException(this.sizex, this.sizey, 0, lastColumn+1);

        int offset = 0;
        int[] offsets = new int[this.sizey];
        for(int y = 0; y<this.sizey; y++){
            if(offset != columns.size() && y-1 == columns.get(offset))
                offset++;
            offsets[y] = offset;
        }

        Grid g = new Grid(this.sizex, this.sizey-columns.size());
        for(int x = 0; x<this.sizex; x++)
            for(int y = 0; y<this.sizey; y++)
                if(this.hasPoint(x,y))
                    if(g.hasPoint(x,y-offsets[y]))
                        throw new UnmergeableColumnGridException(columns, x, y);
                    else
                        g.addPoint(x,y-offsets[y]);
        return g;
    }


    /**
     *
     * @return la densité de la grille, ie la somme, pour chaque point, des voisins de ce point (verticalement,
     * horizontalement et diagonalement), divisée par deux.
     */
    public int getDensity() {
        int d = 0;
        for (int x = 0; x < this.sizex - 1; x++) {
            for (int y = 0; y < this.sizey; y++)
                if (this.hasPoint(x, y) && this.hasPoint(x + 1, y))
                    d++;
        }
        for (int x = 0; x < this.sizex; x++) {
            for (int y = 0; y < this.sizey - 1; y++) {
                if (this.hasPoint(x, y) && this.hasPoint(x, y + 1))
                    d++;
            }
        }
        for (int x = 0; x < this.sizex - 1; x++) {
            for (int y = 0; y < this.sizey - 1; y++) {
                if (this.hasPoint(x, y) && this.hasPoint(x+1, y + 1))
                    d++;
                if (this.hasPoint(x+1, y) && this.hasPoint(x, y + 1))
                    d++;
            }
        }
        return d;
    }

    public Grid simplify(){
        List<Integer> linesToMerge = linesToSimplify();
        List<Integer> columnsToMerge = columnsToSimplify();

        boolean empty = true;
        for(int line = 0; line < this.getSizex() ; line++){
            if(this.hasPoint(line, this.getSizey()-1)){
                empty = false;
                break;
            }
        }

        if(empty)
            columnsToMerge.add(this.getSizey()-2);

        empty = true;
        for(int column = 0; column < this.getSizey() ; column++){
            if(this.hasPoint(this.getSizex()-1, column)){
                empty = false;
                break;
            }
        }

        if(empty)
        linesToMerge.add(this.getSizex()-2);

        return this.mergeLines(linesToMerge).mergeColumns(columnsToMerge);

    }

    public List<Integer> linesToSimplify(){
        List<Integer> linesToMerge = new ArrayList<Integer>();

        outer : for(int line = 0; line < this.getSizex()-1; line++){
            for(int column = 0; column < this.getSizey(); column++){
                if(this.hasPoint(line,column))
                    continue outer;
            }
            linesToMerge.add(line);
        }

        return linesToMerge;
    }

    public List<Integer> columnsToSimplify(){
        List<Integer> columnsToMerge = new ArrayList<Integer>();


        outer : for(int column = 0; column < this.getSizey()-1; column++){
            for(int line = 0; line < this.getSizex(); line++){
                if(this.hasPoint(line,column))
                    continue outer;
            }
            columnsToMerge.add(column);
        }

        return columnsToMerge;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x < this.sizex; x++){
            StringUtils.mult(sb, "-", 1 + 4 * sizey);
            sb.append(System.lineSeparator());
            sb.append("|");
            for(int y = 0; y < this.sizey; y++)
                sb.append(" "+(this.hasPoint(x,y)?"x":" ")+" |");
            sb.append(System.lineSeparator());
        }
        StringUtils.mult(sb, "-", 1 + 4 * sizey);
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Grid g = (Grid)super.clone();
        g.grid = new boolean[sizex][sizey];
        for(int x = 0; x < this.sizex; x++)
            for(int y = 0; y < this.sizey; y++)
                if(this.hasPoint(x,y))
                    g.addPoint(x,y);
        return g;
    }

    /**
     *
     * @param list : une liste de lignes ou de colonnes à compacter. Sans perte de généralité, supposons qu'il s'agit
     *             de lignes. On considère qu'on a une grille G(0). On définit la grille G(i) par contraction de la
     *             ligne de la grille G(i-1) d'indice list(i-1).
     * @return la liste des lignes ou des colonnes de G(0) compactées par ce procédés. Autrement dit, la liste list
     * contient les numéros de lignes ou de colonnes des grilles obtenues après contraction successices, on souhaiterait
     * connaître le numéro des colonnes équivalentes dans G(0)
     */
    public static List<Integer> iterativeListToGlobalList(List<Integer> list){
        List<Integer> list2 = new ArrayList<Integer>(list);
        for(int i = list2.size()-2; i>=0 ; i--){
            Integer lineI = list2.get(i);
            for(int j = list2.size()-1; j>i ; j--){
                Integer lineJ = list2.get(j);
                if(lineI <= lineJ)
                    list2.set(j, lineJ+1);
            }
        }
        return list2;
    }

}
