package utils;

import java.util.*;

public class Collections2 {

	/**
	 * Source : http://eyalsch.wordpress.com/2010/04/01/random-sample
	 * @param col
	 * @param size
	 * @return a random subset of the set col which size is size.
	 */
	public static <T> Set<T> randomSubset(Set<T> col, int size) {
		if (size == 0) {
			return new HashSet<T>();
		}
		int s = col.size();
		int dif = s - size;
		if (dif == 0) {
			return new HashSet<T>(col);
		}
		if (dif >= 0 && dif < s) {
			HashSet<T> res = new HashSet<T>();
			int visited = 0;
			Iterator<T> it = col.iterator();
			T item;

			HighQualityRandom rand = new HighQualityRandom();

			while(size > 0){
				item = it.next();
				if(rand.nextDouble() < ((double)size)/(s-visited))
				{
					res.add(item);
					size--;
				}
			}
			return res;
		}
		return null;
	}

	/**
	 * Source : http://eyalsch.wordpress.com/2010/04/01/random-sample
	 * @param col
	 * @param size
	 * @return a random subset of the list col which size is size.
	 */
	public static <T> Set<T> randomSubset(List<T> col, int size) {
		if (size == 0) {
			return new HashSet<T>();
		}
		int s = col.size();
		int dif = s - size;
		if (dif == 0) {
			return new HashSet<T>(col);
		}
		if (dif >= 0 && dif < s) {
			HashSet<T> res = new HashSet<T>();
			T item;

			HighQualityRandom rand = new HighQualityRandom();

			for(int i= dif; i<s; i++){
				item = col.get(rand.nextInt(i+1));
				if(!res.add(item))
					res.add(col.get(i));
			}
			return res;
		}
		return null;
	}

	/**
	 * @param col
	 * @return a random element of the set col.
	 */
	public static <T> T randomElement(Set<T> col) {
		int s = col.size();
		if (s == 0)
			return null;
		int i = Math2.randomInt(s), j = 0;
		Iterator<T> it = col.iterator();
		T n = it.next();
		while (j++ < i) {
			n = it.next();
		}
		return n;
	}

	/**
	 * @param col
	 * @return a random element of the list col.
	 */
	public static <T> T randomElement(List<T> col) {
		int s = col.size();
		if (s == 0)
			return null;
		int i = Math2.randomInt(s);
		return col.get(i);
	}

	/**
	 * If ar is an array of n integers, between 0 and k-1, this method change ar 
	 * into an other array containings the next n-upplet of integers between 0 and k-1
	 * using the following iteration :
	 * - find the last index i for which ar[i] is different from k-1
	 * - increase ar[i] by 1, and set the following elements of ar to 0. 
	 * 
	 * @param ar
	 * @param k
	 * @return
	 */
	public static boolean next(int[] ar, int k) {
		for(int i = ar.length-1; i>=0; i--){
			if(ar[i] >= k-1)
				continue;

			ar[i]++;
			for(int j = i+1; j<ar.length; j++)
				ar[j] = 0;
			return true;

		}
		return false;
	}

	/**
	 * @param k
	 * @return an array containing every integers from 0 to k-1, in that order.
	 */
	public static ArrayList<Integer> range(int k) {
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for(int i = 0; i < k; i++)
			ar.set(i,i);
		return ar;
	}

	/**
	 * Source : http://eyalsch.wordpress.com/2010/04/01/random-sample
	 * @param k
	 * @param size
	 * @return a random sublist of the list 1..k which size is size.
	 */
	public static HashSet<Integer> randomSubrange(int k, int size) {
		if (size == 0) {
			return new HashSet<Integer>();
		}
		int dif = k - size;
		if (dif == 0) {
			return new HashSet<Integer>(range(k));
		}
		if (dif >= 0 && dif < k) {
			HashSet<Integer> res = new HashSet<Integer>();
			Integer item;

			HighQualityRandom rand = new HighQualityRandom();

			for(int i= dif; i<k; i++){
				item = rand.nextInt(i+1);
				if(!res.add(item))
					res.add(i);
			}
            return res;
		}
		return null;
	}

	/**
	 * Return a String containing the String representation of each element of ar (using {@link #toString()}
	 * method, each of those strings are linked with the String delimiter. For example, 
	 * join([1,2,3],"-") returns "1-2-3".
	 * 
	 * @param ar
	 * @param delimiter
	 * @return a String containing the String representation of each element of ar (using {@link #toString()}
	 * method, each of those strings are linked with the String delimiter.
	 */
	public static <T> String join(Collection<T> ar, String delimiter) {
		return join(ar.iterator(), delimiter);
	}

	/**
	 * Return a String containing the String representation of each element returned by the
	 * iterator iter (using {@link #toString()}
	 * method, each of those strings are linked with the String delimiter. For example,
	 * if iter returns successively 1, 2 and 3, join(iter,"-") returns "1-2-3".
	 *
	 * @param delimiter
	 * @return a String containing the String representation of each element returned by the
	 * iterator iter (using {@link #toString()}
	 * method, each of those strings are linked with the String delimiter.
	 */
	public static <T> String join(Iterator<T> iter, String delimiter) {
		return join(iter, delimiter, new Foncteur<T, String>() {
			@Override
			public String apply(T o) {
				return o.toString();
			}
		});
	}

	/**
	 * Return a String containing a String representation of each element of ar 
	 * each of those strings are linked with the String delimiter. The String
	 * representation is decided by the fonction fonc and not necessarily by
	 *  {@link #toString()} method.
	 * 
	 * For example, if f is a fonction which returns "i+1" when input is integer i, 
	 * join([1,2,3],"-", f) returns "2-3-4".
	 * 
	 * @param ar
	 * @param delimiter
	 * @return a String containing a String representation of each element of ar 
	 * each of those strings are linked with the String delimiter. The String
	 * representation is decided by the fonction fonc and not necessarily by
	 *  {@link #toString()} method.
	 */
	public static <T> String join(Collection<T> ar, String delimiter,
			Foncteur<T, String> fonc) {
		return join(ar.iterator(), delimiter, fonc);
	}

	/**
	 * Return a String containing a String representation of each element returned
	 * by the iterator iter, 
	 * each of those strings are linked with the String delimiter. The String
	 * representation is decided by the fonction fonc and not necessarily by
	 *  {@link #toString()} method.
	 * 
	 *  For example, if iter returns successively 1, 2 and 3, 
	 *  and f is a fonction which returns "i+1" when input is integer i, 
	 *  join(iter,"-", f) returns "2-3-4".
	 *
	 * @param delimiter
	 * @return a String containing a String representation of each element of ar 
	 * each of those strings are linked with the String delimiter. The String
	 * representation is decided by the fonction fonc and not necessarily by
	 *  {@link #toString()} method.
	 */
	public static <T> String join(Iterator<T> iter, String delimiter,
			Foncteur<T, String> fonc) {
		if (!iter.hasNext())
			return "";
		StringBuffer buffer = new StringBuffer(fonc.apply(iter.next()));
		while (iter.hasNext())
			buffer.append(delimiter).append(fonc.apply(iter.next()));
		return buffer.toString();
	}


	public static void main(String[] args) {
		System.out.println(Collections2.randomSubrange(1000,50));

	}

}
