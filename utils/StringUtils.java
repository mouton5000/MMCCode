package utils;

/**
 * A class regrouping some util functions for Strings
 * @author mouton
 *
 */
public class StringUtils {

	/**
	 * This function transform the StringBuilder s by adding n times the
	 * string pattern to it.
	 * @param s
	 * @param pattern
	 * @param n
	 */
	public static void mult(StringBuilder s, String pattern, int n) {
		for (int i = 0; i < n; i++)
			s.append(pattern);
	}
}
