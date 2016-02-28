package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * Iterator over every sublists of size k of an input list. 
 * 
 * Like any iterator, {@link #hasNext()} return true if there are
 * subLists you can still iterate. {@link #next()} return the next
 * subList to iterate. 
 * 
 * Finally, when {@link #next()} return a subList, you can get the
 * rest of the list by calling the {@link #getRest()} method.
 * 
 * @author mouton
 *
 * 
 * @param <T>
 */
public class CombinationIterator<T> implements Iterator<List<T>>  {

	/**
	 * Input list over which this instance iterates the subLists.
	 */
	private List<T> list;

	/**
	 * SubList previously returned by the {@link #next()} method.
	 */
	private ArrayList<T> prevComb;

	/**
	 * SubList which will be returned at the next call of {@link #next()} method.
	 */
	private ArrayList<T> comb;

	/**
	 * Array containing each index of the elements of the current iterated sublist.
	 * For example if the input list is [1 5 9 10], for which we iterate every subList
	 * of size 2. If the current subList is [5 9], positions will be [1 2].
	 */
	private int[] positions;

	/**
	 * Parameter determining the size of the iterated subLists.
	 */
	private int k;

	/**
	 * Size of the input list over wich we iterate every subList.
	 */
	private int n;

	/**
	 * Create an iterator over every subList of list of size sublistSize.
	 * @param list
	 * @param sublistsSize
	 */
	public CombinationIterator(List<T> list, int sublistsSize) {
		this.k = sublistsSize;
		this.n = list.size();
		this.list = list;
		positions = new int[k];
		comb = new ArrayList<T>(k);
		for (int i = 0; i < k; i++) {
			positions[i] = i;
			comb.add(list.get(i));
		}
	}

	/**
	 * Parameter that is true if there is an other subList to iterate.
	 */
	private boolean hasNext = true;

	@Override
	public boolean hasNext() {
		return hasNext;
	}


	@SuppressWarnings("unchecked")
	public List<T> next() throws NoSuchElementException {
		prevComb = (ArrayList<T>) comb.clone();

		if(!hasNext)
			throw new NoSuchElementException();

		int jindex = jindex();
		if (jindex == -1)
			hasNext = false;
		else {
			move(jindex);
		}
		return prevComb;
	}

	/**
	 * Check the position in the array {@link #positions}
	 * which index is index and returns true if this position 
	 * is not maximal.
	 * 
	 * If it is not, it means we can increase it and 
	 * add iterate a new subList.
	 */
	private boolean isMovable(int index) {
		return positions[index] != n - 1 - (k - 1 - index);
	}

	/**
	 * Increase the position in the array {@link #positions}
	 * which index is index. Then change every position of index
	 * greater than index :
	 * pos[index+j] = pos[index]+j
	 * 
	 * Finally, change {@link #comb} so that position and comb are 
	 * consistent.
	 * 
	 * @param index
	 */
	private void move(int index) {
		positions[index] += 1;
		comb.set(index, list.get(positions[index]));
		for (int j = index + 1; j < k; j++) {
			positions[j] = positions[index] + j - index;
			comb.set(j, list.get(positions[j]));
		}
	}

	/**
	 * @return the maximum index which returns true 
	 * when call {@link #isMovable(int)}.
	 */
	private int jindex(){
		int jindex = -1;
		for (int index = k - 1; index >= 0; index--) {
			if (isMovable(index)) {
				jindex = index;
				break;
			}
		}
		return jindex;
	}

	/**
	 * @return the subList of the list over which this
	 * iterator iterates the subLists, containing the element
	 * not in the last iterated subList. 
	 * @throws NoSuchElementException
	 */
	public List<T> getRest() throws NoSuchElementException{
		if (prevComb == null)
			throw new NoSuchElementException();
		ArrayList<T> ar = new ArrayList<T>(list);
		ar.removeAll(prevComb);
		return ar;
	}

	/**
	 * Not implemented!!!
	 */
	@Override
	public void remove() {

	}

	public static void main(String[] args) {
		ArrayList<Integer> ar = new ArrayList<Integer>();
		ar.add(1);
		ar.add(2);
		ar.add(3);
		ar.add(4);


		CombinationIterator<Integer> ci = new CombinationIterator<Integer>(ar, 2);
		while(ci.hasNext()){
			System.out.println(ci.next()+ " " +ci.getRest());
		}
	}

}
