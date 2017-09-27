package jbadillo.dynamic;

import java.util.Arrays;

public class LongestIncreasingSubSequence {
	
	
	public static void main(String[] args) {
		
	}
	
	public static int[] longestIncSubSec(int[] s){
		
		int[] L = new int[s.length];
		int[] prev= new int[s.length];
		
		//calculate L
		L[0] = 1; //base case
		
		for (int i = 1; i < L.length; i++) {
			
			//check max L, of any predecesor of Li
			int maxL = 0;
			
			for (int j = 0; j <i; j++) 
				//if is predecesor
				if(s[j] < s[i])
					//if Lj greater than max
					if(L[j] > maxL){
						maxL = L[j];
						//keep previous one
						prev[i] = j;
					}
			//Li
			L[i] = maxL+1;
		}
		
		
		//now get the max L
		int maxL = L[0];
		int posMax = 0;
		for (int i = 1; i < L.length; i++)
			if(L[i]>maxL){
				maxL=L[i];
				posMax = i;
			}
		
		//build the sequence
		int seq[] = new int[maxL];
		//last equals position of maximun
		int t = posMax;
		//backwards
		for (int i = maxL-1; i >=0; i--) {
			seq[i] = s[t];//value
			t = prev[t];//previous
		}
		
		return seq;
	}

}
