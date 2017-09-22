package jbadillo.dynamic;

import java.util.Arrays;

/**
 * Dinamic programming approach to Longest Common Subsequence
 * between to strings. O(m*n) in space and time.
 * @author jbadillo
 *
 */
public class LongestCommonSubSequence {

	public static final int UP_LEFT = 1;
	public static final int UP = 2;
	public static final int LEFT = 3;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(longestCommonSubSeq("asbedcfrgehitjkol", "msepcqreutvwox"));
		System.out.println(longestCommonSubSeq2("asbedcfrgehitjkol", "msepcqreutvwox"));
		
		System.out.println(longestCommonSubSeq("abc", "mammmbmmmmc"));
		System.out.println(longestCommonSubSeq2("abc", "mammmbmmmmc"));

	}

	static int longestCommonSubSeq(String s, String l){
		int[][] MS = new int[s.length()+1][l.length()+1];
		
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
				}
				//if different, max between taking one of S and one of L
				else{
					if(MS[i-1][j] > MS[i][j-1]){
						MS[i][j] = MS[i-1][j];
					}else{
						MS[i][j] = MS[i][j-1];
					}
				}
			}
		}
		
		
		return MS[s.length()][l.length()];
	}
	
	static String longestCommonSubSeq2(String s, String l){
		int[][] MS = new int[s.length()+1][l.length()+1];
		//moving matrix
		int[][] prev = new int[s.length()+1][l.length()+1];
		
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
					prev[i][j] = UP_LEFT;
				}
				//if different, max between taking one of S and one of L
				else{
					if(MS[i-1][j] > MS[i][j-1]){
						MS[i][j] = MS[i-1][j];
						prev[i][j] = UP;
					}else{
						MS[i][j] = MS[i][j-1];
						prev[i][j] = LEFT;
					}
				}
			}
		}
//		for (int i = 0; i < prev.length; i++) {
//			System.out.println(Arrays.toString(prev[i]));
//		}
		
		
		String c = ""; 
		for (int i = s.length(), j = l.length(); i > 0 && j > 0; ) {
			//if up left, take char
			if(prev[i][j] == UP_LEFT){
				c = s.charAt(i-1)+c;
				i--;
				j--;
			}
			//move up
			else if(prev[i][j] == UP){
				i--;
			}
			//move left
			else if(prev[i][j] == LEFT){
				j--;
			}
		}
		
		return c;
	}
}
