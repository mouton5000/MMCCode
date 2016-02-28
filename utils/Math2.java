package utils;

import java.util.Arrays;


public class Math2 {

	/**
	 * Return a random boolean.
	 * 
	 * @return
	 */
	public static boolean randomBoolean() {
		int i = randomInt(2);
		return i == 0;
	}

	/**
	 * @param a
	 * @param b
	 * @return a random integer between a included and b excluded.
	 */
	public static int randomInt(int a, int b) {
		if (a > b)
			return randomInt(b, a);
		return (int) (Math.random() * (b - a) + a);
	}

	/**
	 * 
	 * @param b
	 * @return a random integer between 0 included and b excluded.
	 */
	public static int randomInt(int b) {
		return randomInt(0, b);
	}

	/**
	 * @return a random integer between a included and b excluded.
	 */
	public static int randomInt() {
		return randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * @param a
	 * @param b
	 * @return uniformly any double between a included and b excluded.
	 */
	public static double uniform(double a, double b) {
		return Math.random() * (b - a) + a;
	}

	/**
	 * 
	 * @param mu
	 * @param sigma2
	 * @return a random double using normal law of parameters mu and sigma 2. 
	 */
	public static double norm(double mu, double sigma2) {
		double u1 = Math.random(), u2 = Math.random();
		return mu + Math.sqrt(sigma2) * Math.sqrt(-2 * Math.log(u1))
				* Math.cos(2 * Math.PI * u2);
	}

	/**
	 * 
	 * @param lambda
	 * @return a random double using exponential law of parameter lambda
	 */
	public static double exp(double lambda) {
		double u1 = Math.random();
		return -1 / lambda * Math.log(u1);
	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param norm
	 * @return the distance between coordinates (x1,y1) and (x2,y2) using
	 * the precised norm. (For example, if norm = 2, euclidean distance, if norm = Double.POSITIVE_INFINITY, 
	 * use infinity norm).
	 */
	public static double dist(int x1, int y1, int x2, int y2, Double norm) {
		if (norm <= 0)
			return -1;
		else {
			if (norm == Double.POSITIVE_INFINITY)
				return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
			else
				return Math.pow(
						Math.pow(Math.abs(x1 - x2), norm)
								+ Math.pow(Math.abs(y1 - y2), norm), 1 / norm);
		}
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the scalar products of two vectors (x1,y1) and (x2,y2)
	 */
	public static int scalarProduct(int x1, int y1, int x2, int y2) {
		return x1 * x2 + y1 * y2;
	}

	/**
	 * @param x
	 * @param y
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param prec
	 * @return true if the distance between (x,y) and the strait line 
	 * from (x1,y1) and (x2,y2) is less that prec. 
	 */
	public static boolean isOnSegment(int x, int y, int x1, int y1, int x2,
			int y2, double prec) {

		// Vecteur U directeur du segment
		int a = x2 - x1;
		int b = y2 - y1;

		// Vecteur U1 directeur de la droite entre P1 et P
		int a1 = x - x1;
		int b1 = y - y1;

		// Le point est prêt du segment si les produits scalaires U.U1 et -U.U2
		// sont positifs, et si la projection de P sur le segment est à une
		// distance inférieur
		// à prec.
		if (scalarProduct(a, b, a1, b1) > 0) {
			// Vecteur U2 directeur de la droite entre P2 et P
			int a2 = x - x2;
			int b2 = y - y2;
			if (scalarProduct(-a, -b, a2, b2) > 0) {
				double d = Math.abs(scalarProduct(-a1, -b1, -b, a))
						/ Math.sqrt(a * a + b * b);
				return d <= prec;
			}
		}
		return false;
	}
	
	/**
	 * @param k
	 * @return a random permutation of all possible permutations with k elements.
	 */
	public static int[] getRandomPermutation(int k){
		int[] perm = new int[k];
		for(int i = 0; i<k; i++) perm[i] = i;
		int swap, r;
		for(int i = 0; i<k-1; i++) {
			r = Math2.randomInt(k);
			swap = perm[i];
			perm[i] = perm[r];
			perm[r] = swap;
		}
		return perm;
	}
	
	/**
	 * 
	 * @param n
	 * @param k
	 * @return the binomial coefficient of n and k (n choose k) using an iterative method. 
	 */
	public static int binomial(int n, int k){
		if(k == 0 || k == n)
			return 1;
		double pasc = 1;
		for(int j = 1; j<=n-k; j++)
			pasc *= (k+j)/(double)j;
		return (int)pasc;
	}
	

	/**
	 * @param n
	 * @return the nth catalan number
	 */
	public static int catalanNumber(int n) {
		int fn = fact(n);
		return (fact(2*n)/(fn*fn))/(n+1);
	}
	
	/**
	 * @param n
	 * @return the factorial of n
	 */
	public static int fact(int n) {
		int f = 1;
		for(int i = 2;i<=n; i++){
			f *= i;
		}
		return f;
	}
	
	/**
	 * Compute the binary representation of i and put the result
	 * into the array binary. A bit 1 is represented by true, and 0
	 * by false.
	 * @param i
	 * @param binary
	 */
	public static void intToBin(int i, boolean[] binary){
		int index = 0;
		
		while(i!=0){
			binary[index++] = (i%2==1);
			i/=2;
			
			if(index == binary.length)
				return;
		}
		
		
		while(index != binary.length)
			binary[index++] = false;
	}
	
	
	public static void main(String[] args) {
		boolean[] b = new boolean[5];
		intToBin(31,b);
		
		System.out.println(Arrays.toString(b));
	}

}