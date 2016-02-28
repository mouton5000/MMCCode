package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 
 * @author mouton
 *
 * Implémente un itérateur sur tous les sous ensembles d'une liste.
 * Ces sous ensembles sont renvoyés par ordre croissant de taille.
 *  
 * @param <T>
 */
public class SubsetIterator<T> implements Iterator<List<T>> {

	private CombinationIterator<T> ci;
	private int k;
	private List<T> list;
	
	/**
	 * Correspond au numéro actuel de l'itération, car on peut calculer
	 * aisément le nombre d'itération maximal.
	 */
	private int iteration;
	private int maxIter;
	
	/**
	 * Crée un itérateur sur les sous ensembles de s .
	 * @param s
	 */
	public SubsetIterator(List<T> s) {
		this(s,true);
	}
	
	/**
	 * Crée un itérateur sur les sous ensembles de s. Le paramètre
	 * emptySet décide si oui ou non on itère sur les sous ensembles
	 * vide et plein de s.
	 * @param emptySets
	 * @param s
	 */
	public SubsetIterator(List<T> s, boolean emptySets) {
		this(s, emptySets, false);
	}
	
	/**
	 * Crée un itérateur sur les sous ensembles de s. Le paramètre
	 * emptySet décide si oui ou non on itère sur les sous ensembles
	 * vide et plein de s. middleDécide si oui ou non on itère uniquement
	 * la moitié des ensembles (l'autre moitié étant l'ensemble des restes).
	 * @param emptySets
	 * @param s
	 */
	public SubsetIterator(List<T> s, boolean emptySets, boolean middle) {
		list = s;
		k = emptySets?0:1;
		maxIter = (int)Math.pow(2, s.size()) - (emptySets?0:2);
		if(middle)
			maxIter /= 2;
		
		ci = new CombinationIterator<T>(s, k);
	}
	
	@Override
	public boolean hasNext() {
		return iteration < maxIter;
	}

	@Override
	public List<T> next() throws NoSuchElementException{
		iteration++;
		if(iteration == maxIter+1)
			throw new NoSuchElementException();
		if(ci.hasNext())
			return ci.next();
		else
		{
			k += 1;
			ci = new CombinationIterator<T>(list, k);
			return ci.next();
		}
	}

	/**
	 * Renvoie le reste de la liste dont on itère les sous ensembles.
	 * C'est à dire les éléments de cette liste dont on a retiré
	 * les éléments actuellements itérés.
	 * @return
	 * @throws NoSuchElementException
	 */
	public List<T> getRest() throws NoSuchElementException{
		return ci.getRest();
	}
	
	@Override
	public void remove() {
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> ar = new ArrayList<Integer>();
		ar.add(1);
		ar.add(2);
		ar.add(3);
		ar.add(4);


		SubsetIterator<Integer> si = new SubsetIterator<Integer>(ar,true, false);
		while(si.hasNext()){
			System.out.println(si.next()+ " " +si.getRest());
		}
	}

}

// TODO Relire
// TODO Refactor
// TODO Commenter
