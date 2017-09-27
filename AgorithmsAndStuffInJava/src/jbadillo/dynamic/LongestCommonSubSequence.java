package jbadillo.dynamic;

/**
 * Dynamic programming approach to Longest Common Subsequence
 * between to strings. O(m*n) in time and memory
 * @author jbadillo
 *
 */
public class LongestCommonSubSequence {

	enum Move{UP_LEFT, UP, LEFT};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

	static String longestCommonSubSeq(String s, String l){
		int[][] MS = new int[s.length()+1][l.length()+1];
		//moving matrix
		Move[][] prev = new Move[s.length()+1][l.length()+1];
		
		//easy cases, row 0 and col 0
		for (int i = 0; i <=s.length(); i++) {
			MS[i][0] = 0;
		}
		for (int j = 0; j <=l.length(); j++) {
			MS[0][j] = 0;
		}
		
		//from top to bottom, right to left
		for (int i = 1; i <= s.length(); i++) {
			for (int j = 1; j <= l.length(); j++) {
				//if equal 1 plus previous longest sub sequence
				if(s.charAt(i-1) == l.charAt(j-1)){
					MS[i][j] = 1 + MS[i-1][j-1];
					
					//move up left
					prev[i][j] = Move.UP_LEFT;
				}
				//if different, max between taking one of S and one of L
				else{
					if(MS[i-1][j] > MS[i][j-1]){
						MS[i][j] = MS[i-1][j];
						prev[i][j] = Move.UP;
					}else{
						MS[i][j] = MS[i][j-1];
						prev[i][j] = Move.LEFT;
					}
				}
			}
		}
//		for (int i = 0; i < prev.length; i++) {
//			System.out.println(Arrays.toString(prev[i]));
//		}
		int length = MS[s.length()][l.length()];
		
		// Reconstruct string backwards
		char[] c = new char[length]; 
		for (int i = s.length(), j = l.length(), k = length - 1; i > 0 && j > 0; ) {
			//if up left, take char
			if(prev[i][j] == Move.UP_LEFT){
				c[k--] = s.charAt(i-1);
				i--;
				j--;
			}
			//move up
			else if(prev[i][j] == Move.UP){
				i--;
			}
			//move left
			else if(prev[i][j] == Move.LEFT){
				j--;
			}
		}
		
		return new String(c);
	}
}
