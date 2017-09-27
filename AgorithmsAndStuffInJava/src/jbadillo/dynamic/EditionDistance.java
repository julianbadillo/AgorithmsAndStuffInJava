package jbadillo.dynamic;

import java.util.Arrays;

/**
 * Calculation of minimum edition distance 
 * between two strings using dynamic programming
 * O(n*m) in time and memory
 * 
 * @author jbadillo
 *
 */
public class EditionDistance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
	}

	public static int editionDistance(String s1, String s2) {
		
		int[][] dist = new int[s1.length()+1][s2.length()+1];
		//first row and column
		for (int i = 0; i < dist.length; i++) {
			dist[i][0]=i;
		}
		for (int i = 0; i < dist[0].length; i++) {
			dist[0][i]=i;
		}
		
		for (int i = 1; i <= s1.length(); i++) {
			for (int j = 1; j <= s2.length(); j++) {
				int d1,d2,d3;
				d1 = dist[i-1][j]+1;
				d2 = dist[i][j-1]+1;
				//if equal 0, else 1
				d3 = (s1.charAt(i-1)==s2.charAt(j-1)?0:1) + dist[i-1][j-1];
				//min of three
				if(d1 <= d2 && d1 <= d3)
					dist[i][j]=d1;
				else if(d2 <= d1 && d2 <= d3)
					dist[i][j]=d2;
				else if(d3 <= d1 && d3 <= d2)
					dist[i][j]=d3;
			}
		}
		
		for (int i = 0; i < dist.length; i++) {
			System.out.println(Arrays.toString(dist[i]));
		}
		
		return dist[s1.length()][s2.length()];
	}

}
